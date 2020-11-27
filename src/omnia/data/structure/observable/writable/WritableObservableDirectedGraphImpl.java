package omnia.data.structure.observable.writable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import omnia.data.stream.Streams;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableDirectedGraph;
import omnia.data.structure.immutable.ImmutableSet;
import omnia.data.structure.observable.ObservableDirectedGraph;
import omnia.data.structure.tuple.Couplet;

final class WritableObservableDirectedGraphImpl<E> implements WritableObservableDirectedGraph<E> {

  private volatile ImmutableDirectedGraph<E> state;
  private final FlowableProcessor<MutationEvent<E>> mutationEventProcessor =
      PublishProcessor.<MutationEvent<E>>create().toSerialized();

  WritableObservableDirectedGraphImpl() {
    state = ImmutableDirectedGraph.empty();
  }

  WritableObservableDirectedGraphImpl(DirectedGraph<E> original) {
    state = ImmutableDirectedGraph.copyOf(original);
  }

  @Override
  public void addNode(E item) {
    mutateState(
        currentState -> !currentState.contents().contains(item),
        currentState -> currentState.toBuilder().addNode(item).build(),
        (previousState, newState) -> ImmutableSet.of(AddNodeToGraph.create(item)));
  }

  @Override
  public void replaceNode(E original, E replacement) {
    mutateState(
        currentState -> {
          if (!currentState.contents().contains(original)) {
            throw new IllegalArgumentException(
                "cannot replace a non-existent node. original=" + original);
          }
          if (currentState.contents().contains(replacement)) {
            throw new IllegalArgumentException(
                "cannot replace a node with an already existing node. replacement=" + replacement);
          }
          return true;
        },
        currentState -> currentState.toBuilder().replaceNode(original, replacement).build(),
        (previousState, newState) ->
            Streams.concat(
                previousState.nodeOf(original)
                    .stream()
                    .map(DirectedNode::edges)
                    .flatMap(Set::stream)
                    .map(DirectedEdge::endpoints)
                    .map(couplet -> couplet.map(DirectedNode::item))
                    .map(RemoveEdgeFromGraph::create),
                previousState.nodeOf(original)
                    .stream()
                    .map(DirectedNode::item)
                    .map(RemoveNodeFromGraph::create),
                newState.nodeOf(replacement)
                    .stream()
                    .map(DirectedNode::item)
                    .map(AddNodeToGraph::create),
                newState.nodeOf(replacement)
                    .stream()
                    .map(DirectedNode::edges)
                    .flatMap(Set::stream)
                    .map(DirectedEdge::endpoints)
                    .map(couplet -> couplet.map(DirectedNode::item))
                    .map(AddEdgeToGraph::create))
                .collect(toSet()));
  }

  @Override
  public boolean removeUnknownTypedNode(Object item) {
    return mutateState(
        currentState -> currentState.contents().containsUnknownTyped(item),
        currentState -> currentState.toBuilder().removeUnknownTypedNode(item).build(),
        (previousState, newState) ->
            Streams.concat(
                previousState.nodeOf(item).stream()
                    .map(DirectedNode::edges)
                    .flatMap(Set::stream)
                    .map(DirectedEdge::endpoints)
                    .map(couplet -> couplet.map(DirectedNode::item))
                    .map(RemoveEdgeFromGraph::create),
                previousState.nodeOf(item).stream()
                    .map(DirectedNode::item)
                    .map(RemoveNodeFromGraph::create))
                .collect(toSet()));
  }

  @Override
  public void addEdge(E from, E to) {
    mutateState(
        currentState -> currentState.edgeOf(from, to).isEmpty(),
        currentState -> currentState.toBuilder().addEdge(from, to).build(),
        (previousState, newState) ->
            newState.edgeOf(from, to).stream()
                .map(DirectedEdge::endpoints)
                .map(couplet -> couplet.map(DirectedNode::item))
                .map(AddEdgeToGraph::create)
                .collect(toSet()));
  }

  @Override
  public boolean removeEdge(E from, E to) {
    return removeEdgeUnknownTyped(from, to);
  }

  @Override
  public boolean removeEdgeUnknownTyped(Object from, Object to) {
    return mutateState(
        currentState -> currentState.edgeOf(from, to).isPresent(),
        currentState -> currentState.toBuilder().removeEdgeUnknownEdge(from, to).build(),
        (previousState, newState) ->
            previousState.edgeOf(from, to).stream()
                .map(DirectedEdge::endpoints)
                .map(couplet -> couplet.map(DirectedNode::item))
                .map(RemoveEdgeFromGraph::create)
                .collect(toSet()));
  }

  private boolean mutateState(
      Predicate<? super ImmutableDirectedGraph<E>> shouldChange,
      Function<? super ImmutableDirectedGraph<E>, ? extends ImmutableDirectedGraph<E>> mutateState,
      BiFunction<? super ImmutableDirectedGraph<E>, ? super ImmutableDirectedGraph<E>, ? extends Set<GraphOperation<E>>> mutationOperations) {
    ImmutableDirectedGraph<E> previousState;
    ImmutableDirectedGraph<E> nextState;
    synchronized (this) {
      previousState = state;
      if (!shouldChange.test(previousState)) {
        return false;
      }
      nextState = requireNonNull(mutateState.apply(previousState));
      state = nextState;
      Set<GraphOperation<E>> operations = mutationOperations.apply(previousState, nextState);
      mutationEventProcessor.onNext(
          new MutationEvent<>() {
            @Override
            public DirectedGraph<E> state() {
              return nextState;
            }

            @Override
            public Set<GraphOperation<E>> operations() {
              return operations;
            }
          });
    }
    return true;
  }

  @Override
  public Optional<? extends DirectedNode<E>> nodeOf(Object item) {
    return getState().nodeOf(item);
  }

  @Override
  public Optional<? extends DirectedEdge<E>> edgeOf(Object from, Object to) {
    return getState().edgeOf(from, to);
  }

  @Override
  public Set<E> contents() {
    return getState().contents();
  }

  @Override
  public Set<? extends DirectedNode<E>> nodes() {
    return getState().nodes();
  }

  @Override
  public Set<? extends DirectedEdge<E>> edges() {
    return getState().edges();
  }

  @Override
  public ObservableDirectedGraph<E> toReadOnly() {
    return new ObservableDirectedGraph<>() {
      @Override
      public ObservableChannels<E> observe() {
        return WritableObservableDirectedGraphImpl.this.observe();
      }

      @Override
      public Optional<? extends DirectedNode<E>> nodeOf(Object item) {
        return WritableObservableDirectedGraphImpl.this.nodeOf(item);
      }

      @Override
      public Optional<? extends DirectedEdge<E>> edgeOf(E from, E to) {
        return WritableObservableDirectedGraphImpl.this.edgeOf(from, to);
      }

      @Override
      public Set<? extends DirectedNode<E>> nodes() {
        return WritableObservableDirectedGraphImpl.this.nodes();
      }

      @Override
      public Set<? extends DirectedEdge<E>> edges() {
        return WritableObservableDirectedGraphImpl.this.edges();
      }

      @Override
      public Set<E> contents() {
        return WritableObservableDirectedGraphImpl.this.contents();
      }
    };
  }

  @Override
  public ObservableDirectedGraph.ObservableChannels<E> observe() {
    return new ObservableDirectedGraph.ObservableChannels<>() {
      @Override
      public Flowable<DirectedGraph<E>> states() {
        return Flowable.create(
            emitter -> {
              synchronized (this) {
                emitter.onNext(getState());
                emitter.setDisposable(
                    mutationEventProcessor.map(MutationEvent::state)
                        .subscribe(emitter::onNext, emitter::onError, emitter::onComplete));
              }
            },
            BackpressureStrategy.LATEST);
      }

      @Override
      public Flowable<MutationEvent<E>> mutations() {
        return Flowable.create(
            emitter -> {
               synchronized (this) {
                 ImmutableDirectedGraph<E> state = getState();
                 Set<GraphOperation<E>> operations =
                     Streams.concat(
                         state.nodes().stream()
                             .map(DirectedNode::item)
                             .map(AddNodeToGraph::create),
                         state.edges().stream()
                             .map(DirectedEdge::endpoints)
                             .map(couplet -> couplet.map(DirectedNode::item))
                             .map(AddEdgeToGraph::create))
                         .collect(toImmutableSet());
                 emitter.onNext(new MutationEvent<>() {
                   @Override
                   public DirectedGraph<E> state() {
                     return state;
                   }

                   @Override
                   public Set<GraphOperation<E>> operations() {
                     return operations;
                   }
                 });
                 emitter.setDisposable(
                     mutationEventProcessor
                         .subscribe(emitter::onNext, emitter::onError, emitter::onComplete));
               }
            },
            BackpressureStrategy.BUFFER);
      }
    };
  }

  private ImmutableDirectedGraph<E> getState() {
    synchronized (this) {
      return state;
    }
  }

  interface MutationEvent<E> extends ObservableDirectedGraph.MutationEvent<E> {

    @Override
    DirectedGraph<E> state();

    @Override
    Set<GraphOperation<E>> operations();
  }

  private static abstract class NodeOperation<E> implements ObservableDirectedGraph.NodeOperation<E> {

    private final E item;

    private NodeOperation(E item) {
      this.item = requireNonNull(item);
    }

    @Override
    public final E item() {
      return item;
    }
  }

  private static final class AddNodeToGraph<E> extends NodeOperation<E> implements ObservableDirectedGraph.AddNodeToGraph<E> {
    private AddNodeToGraph(E item) {
      super(item);
    }

    static <E> AddNodeToGraph<E> create(E item) {
      return new AddNodeToGraph<>(item);
    }
  }

  private static final class RemoveNodeFromGraph<E> extends NodeOperation<E> implements ObservableDirectedGraph.RemoveNodeFromGraph<E> {
    private RemoveNodeFromGraph(E item) {
      super(item);
    }

    static <E> RemoveNodeFromGraph<E> create(E item) {
      return new RemoveNodeFromGraph<>(item);
    }
  }

  private static abstract class EdgeOperation<E> implements ObservableDirectedGraph.EdgeOperation<E> {
    private final Couplet<E> endpoints;

    private EdgeOperation(Couplet<E> endpoints) {
      this.endpoints = requireNonNull(endpoints);
    }

    @Override
    public final Couplet<E> endpoints() {
      return endpoints;
    }
  }

  private static final class AddEdgeToGraph<E> extends EdgeOperation<E> implements ObservableDirectedGraph.AddEdgeToGraph<E> {
    private AddEdgeToGraph(Couplet<E> endpoints) {
      super(endpoints);
    }

    static <E> AddEdgeToGraph<E> create(Couplet<E> endpoints) {
      return new AddEdgeToGraph<>(endpoints);
    }
  }

  private static final class RemoveEdgeFromGraph<E> extends EdgeOperation<E> implements ObservableDirectedGraph.RemoveEdgeFromGraph<E> {
    private RemoveEdgeFromGraph(Couplet<E> endpoints) {
      super(endpoints);
    }

    static <E> RemoveEdgeFromGraph<E> create(Couplet<E> endpoints) {
      return new RemoveEdgeFromGraph<>(endpoints);
    }
  }
}
