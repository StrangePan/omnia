package omnia.data.structure.rx;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toImmutableSet;
import static omnia.data.stream.Collectors.toSet;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import omnia.data.iterate.MappingIterator;
import omnia.data.iterate.WrapperIterator;
import omnia.data.structure.Set;
import omnia.data.structure.UndirectedGraph;
import omnia.data.structure.UnorderedPair;
import omnia.data.structure.immutable.ImmutableSet;
import omnia.data.structure.immutable.ImmutableUndirectedGraph;
import omnia.data.structure.mutable.MutableSet;

public final class ObservableUndirectedGraphImpl<E> implements ObservableUndirectedGraph<E> {

  private ImmutableUndirectedGraph<E> currentState = ImmutableUndirectedGraph.empty();
  private final Subject<MutationEvent> mutationEventSubject = PublishSubject.create();
  private final ObservableChannels observableChannels = new ObservableChannels();

  @Override
  public ObservableChannels observe() {
    return observableChannels;
  }

  @Override
  public void replace(E original, E replacement) {
    requireNonNull(original);
    requireNonNull(replacement);
    mutateState(
        currentState -> true, // execute anyway and throw exception if replacing nonexistent node
        currentState -> currentState.toBuilder().replaceNode(original, replacement).build(),
        (previousState, currentState) ->
            Stream.concat(
                Stream.concat(
                    previousState.nodeOf(original)
                        .map(UndirectedGraph.Node::edges)
                        .orElse(Set.empty())
                        .stream()
                        .map(UndirectedGraph.Edge::endpoints)
                        .map(pair ->
                            UnorderedPair.of(pair.first().element(), pair.second().element()))
                        .map(RemoveEdgeFromGraph::new),
                    Stream.of(original).map(RemoveNodeFromGraph::new)),
                Stream.concat(
                    Stream.of(replacement).map(AddNodeToGraph::new),
                    currentState.nodeOf(replacement)
                        .map(UndirectedGraph.Node::edges)
                        .orElse(Set.empty())
                        .stream()
                        .map(UndirectedGraph.Edge::endpoints)
                        .map(pair ->
                            UnorderedPair.of(pair.first().element(), pair.second().element()))
                        .map(AddEdgeToGraph::new)))
                    .collect(toSet()));
  }

  @Override
  public void addEdge(E first, E second) {
    requireNonNull(first);
    requireNonNull(second);
    UnorderedPair<E> newEdge = UnorderedPair.of(first, second);
    mutateState(
        currentState -> currentState.edges().stream().map(UndirectedGraph.Edge::endpoints).noneMatch(currentEdge -> Objects.equals(currentEdge, newEdge)),
        currentState -> currentState.toBuilder().addEdge(first, second).build(),
        (previousState, currentState) ->
            ImmutableSet.of(new AddEdgeToGraph<>(UnorderedPair.of(first, second))));
  }

  @Override
  public boolean removeEdge(E first, E second) {
    UnorderedPair<E> endpoints = UnorderedPair.of(first, second);
    return mutateState(
        currentState ->
            currentState.edges()
                .stream()
                .map(UndirectedGraph.Edge::endpoints)
                .map(ObservableUndirectedGraphImpl::unwrap)
                .anyMatch(pair -> Objects.equals(pair, endpoints)),
        currentState -> currentState.toBuilder().removeEdge(first, second).build(),
        (previousState, currentState) -> ImmutableSet.of(new RemoveEdgeFromGraph<>(endpoints)));
  }

  @Override
  public Optional<Node<E>> nodeOf(E element) {
    return getState().nodeOf(element).map(node -> getOrCreateNode(node.element()));
  }

  @Override
  public MutableSet<E> contents() {
    return new MutableSet<E>() {
      @Override
      public void add(E element) {
        ObservableUndirectedGraphImpl.this.add(element);
      }

      @Override
      public boolean remove(E element) {
        return ObservableUndirectedGraphImpl.this.remove(element);
      }

      @Override
      public void clear() {
        ObservableUndirectedGraphImpl.this.clear();
      }

      @Override
      public Iterator<E> iterator() {
        return ObservableUndirectedGraphImpl.this.iterator();
      }

      @Override
      public boolean contains(Object element) {
        return ObservableUndirectedGraphImpl.this.contains(element);
      }

      @Override
      public int count() {
        return ObservableUndirectedGraphImpl.this.count();
      }

      @Override
      public boolean isPopulated() {
        return ObservableUndirectedGraphImpl.this.isPopulated();
      }

      @Override
      public Stream<E> stream() {
        return ObservableUndirectedGraphImpl.this.stream();
      }
    };
  }

  @Override
  public MutableSet<Node<E>> nodes() {
    return new MutableSet<>() {
      @Override
      public void add(Node<E> element) {
        if (element.graph != ObservableUndirectedGraphImpl.this) {
          throw new IllegalArgumentException("tried to add a node from another graph");
        }
        ObservableUndirectedGraphImpl.this.add(element.element);
      }

      @Override
      public boolean remove(Node<E> element) {
        return element.graph == ObservableUndirectedGraphImpl.this
            && ObservableUndirectedGraphImpl.this.remove(element.element);
      }

      @Override
      public void clear() {
        ObservableUndirectedGraphImpl.this.clear();
      }

      @Override
      public Iterator<Node<E>> iterator() {
        return new MappingIterator<>(
            ObservableUndirectedGraphImpl.this.iterator(),
            ObservableUndirectedGraphImpl.this::getOrCreateNode);
      }

      @Override
      public boolean contains(Object element) {
        return element instanceof ObservableUndirectedGraphImpl.Node
            && ((ObservableUndirectedGraphImpl.Node<?>) element).graph == ObservableUndirectedGraphImpl.this
            && ObservableUndirectedGraphImpl.this.contains(((ObservableUndirectedGraphImpl.Node<?>) element).element);
      }

      @Override
      public int count() {
        return ObservableUndirectedGraphImpl.this.count();
      }

      @Override
      public boolean isPopulated() {
        return ObservableUndirectedGraphImpl.this.isPopulated();
      }

      @Override
      public Stream<Node<E>> stream() {
        return ObservableUndirectedGraphImpl.this.stream()
            .map(ObservableUndirectedGraphImpl.this::getOrCreateNode);
      }
    };
  }

  @Override
  public MutableSet<Edge<E>> edges() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(E element) {
    mutateState(
        currentState -> !currentState.contains(element),
        currentState -> currentState.toBuilder().addNode(element).build(),
        (previousState, currentState) -> ImmutableSet.of(new AddNodeToGraph<>(element)));
  }

  @Override
  public boolean remove(E element) {
    return mutateState(
        currentState -> currentState.contains(element),
        currentState -> currentState.toBuilder().removeNode(element).build(),
        (previousState, currentState) ->
            Stream.concat(
                Stream.of(element).map(RemoveNodeFromGraph::new),
                previousState.nodeOf(element)
                    .map(UndirectedGraph.Node::edges)
                    .orElse(Set.empty())
                    .stream()
                    .map(UndirectedGraph.Edge::endpoints)
                    .map(pair -> UnorderedPair.of(pair.first().element(), pair.second().element()))
                    .map(RemoveEdgeFromGraph::new))
                .collect(toSet()));
  }

  @Override
  public void clear() {
    mutateState(
        state -> true,
        state -> ImmutableUndirectedGraph.empty(),
        (previousState, currentState) ->
            Stream.concat(
                previousState.nodes().stream()
                    .map(UndirectedGraph.Node::element)
                    .map(RemoveNodeFromGraph::new),
                previousState.edges().stream()
                    .map(UndirectedGraph.Edge::endpoints)
                    .map(pair -> UnorderedPair.of(pair.first().element(), pair.second().element()))
                    .map(RemoveEdgeFromGraph::new))
                .collect(toSet()));
  }

  private boolean mutateState(
      Predicate<ImmutableUndirectedGraph<E>> shouldMutate,
      Function<ImmutableUndirectedGraph<E>, ImmutableUndirectedGraph<E>> mutator,
      BiFunction<ImmutableUndirectedGraph<E>, ImmutableUndirectedGraph<E>, Set<GraphOperation<E>>> mutationsGenerator) {
    synchronized(this) {
      ImmutableUndirectedGraph<E> previousState = currentState;
      if (!shouldMutate.test(previousState)) {
        return false;
      }
      ImmutableUndirectedGraph<E> newState = mutator.apply(previousState);
      currentState = newState;
      mutationEventSubject.onNext(
          new MutationEvent(newState, mutationsGenerator.apply(previousState, newState)));
      return true;
    }
  }

  private ImmutableUndirectedGraph<E> getState() {
    synchronized(this) {
      return currentState;
    }
  }

  @Override
  public Iterator<E> iterator() {
    return new WrapperIterator<E>(getState().iterator()) {
      @Override
      public void remove() {
        ObservableUndirectedGraphImpl.this.remove(current());
        onRemove();
      }
    };
  }

  @Override
  public boolean contains(Object element) {
    return getState().contains(element);
  }

  @Override
  public int count() {
    return getState().count();
  }

  @Override
  public boolean isPopulated() {
    return getState().isPopulated();
  }

  @Override
  public Stream<E> stream() {
    return getState().stream();
  }

  private static final class Node<E> implements ObservableUndirectedGraph.Node<E> {

    private final ObservableUndirectedGraphImpl<E> graph;
    private final E element;

    private Node(ObservableUndirectedGraphImpl<E> graph, E element) {
      this.graph = graph;
      this.element = element;
    }

    @Override
    public void replaceValue(E element) {
      graph.replace(this.element, element);
    }

    @Override
    public void remove() {
      graph.remove(this.element);
    }

    @Override
    public E element() {
      return element;
    }

    @Override
    public MutableSet<Edge<E>> edges() {
      return new MutableSet<>() {

        @Override
        public Stream<Edge<E>> stream() {
          return immutableCounterpart()
              .stream()
              .map(UndirectedGraph.Edge::endpoints)
              .map(pair -> UnorderedPair.of(pair.first().element(), pair.second().element()))
              .map(graph::getOrCreateEdge);
        }

        @Override
        public int count() {
          return immutableCounterpart().count();
        }

        @Override
        public boolean isPopulated() {
          return immutableCounterpart().isPopulated();
        }

        @Override
        public boolean contains(Object element) {
          return element instanceof Edge
              && immutableCounterpart().stream()
                  .map(UndirectedGraph.Edge::endpoints)
                  .anyMatch(pair -> Objects.equals(pair, ((Edge<?>) element).endpoints()));
        }

        @Override
        public Iterator<Edge<E>> iterator() {
          return new MappingIterator<>(
              immutableCounterpart().iterator(),
              edge ->
                  graph.getOrCreateEdge(
                      UnorderedPair.of(
                          edge.endpoints().first().element(),
                          edge.endpoints().second().element())));
        }

        @Override
        public void add(Edge<E> edge) {
          if (edge.graph != graph) {
            throw new IllegalArgumentException("attempt to add edge from foreign graph");
          }
          UnorderedPair<E> unwrappedEndpoints = unwrap(edge.endpoints());
          if (!unwrappedEndpoints.contains(Node.this.element)) {
            throw new IllegalArgumentException("attempt to add edge for other nodes");
          }
          graph.mutateState(
              currentState ->
                  unwrappedEndpoints.contains(Node.this.element)
                      && currentState.contains(edge.endpoints().first())
                      && currentState.contains(edge.endpoints().second())
                      && currentState.edges().stream()
                          .map(UndirectedGraph.Edge::endpoints)
                          .map(ObservableUndirectedGraphImpl::unwrap)
                          .noneMatch(pair -> Objects.equals(pair, unwrappedEndpoints)),
              currentState ->
                  currentState.toBuilder()
                      .addEdge(unwrappedEndpoints.first(), unwrappedEndpoints.second())
                      .build(),
              (previousState, currentState) ->
                  ImmutableSet.of(new AddEdgeToGraph<>(unwrappedEndpoints)));
        }

        @Override
        public boolean remove(Edge<E> edge) {
          UnorderedPair<E> unwrappedEndpoints = unwrap(edge.endpoints());
          return graph.mutateState(
              currentState ->
                  unwrappedEndpoints.contains(Node.this.element)
                      && currentState.contains(edge.endpoints().first())
                      && currentState.contains(edge.endpoints().second())
                      && currentState.edges().stream()
                          .map(UndirectedGraph.Edge::endpoints)
                          .map(ObservableUndirectedGraphImpl::unwrap)
                          .anyMatch(pair -> Objects.equals(pair, unwrappedEndpoints)),
              currentState ->
                  currentState.toBuilder()
                      .removeEdge(unwrappedEndpoints.first(), unwrappedEndpoints.second())
                      .build(),
              (previousState, currentState) ->
                  ImmutableSet.of(new RemoveEdgeFromGraph<>(unwrappedEndpoints)));
        }

        @Override
        public void clear() {
          graph.mutateState(
              currentState ->
                  currentState.edges().stream()
                      .map(UndirectedGraph.Edge::endpoints)
                      .map(ObservableUndirectedGraphImpl::unwrap)
                      .anyMatch(pair -> pair.contains(Node.this.element)),
              currentState -> {
                ImmutableUndirectedGraph.Builder<E> builder = currentState.toBuilder();
                currentState.edges().stream()
                    .map(UndirectedGraph.Edge::endpoints)
                    .map(ObservableUndirectedGraphImpl::unwrap)
                    .filter(pair -> pair.contains(Node.this.element))
                    .forEach(pair -> builder.removeEdge(pair.first(), pair.second()));
                return builder.build();
              },
              (previousState, currentState) ->
                previousState.edges().stream()
                    .map(UndirectedGraph.Edge::endpoints)
                    .map(ObservableUndirectedGraphImpl::unwrap)
                    .filter(pair -> pair.contains(Node.this.element))
                    .map(RemoveEdgeFromGraph::new)
                    .collect(toImmutableSet()));
        }

        private Set<? extends UndirectedGraph.Edge<E>> immutableCounterpart() {
          return graph.getState().nodeOf(element).get().edges();
        }
      };
    }

    @Override
    public MutableSet<? extends ObservableUndirectedGraph.Node<E>> neighbors() {
      return new MutableSet<>() {
        @Override
        public void add(ObservableUndirectedGraph.Node<E> element) {
          graph.addEdge(Node.this.element(), element.element());
        }

        @Override
        public boolean remove(ObservableUndirectedGraph.Node<E> element) {
          return graph.removeEdge(Node.this.element, element.element());
        }

        @Override
        public void clear() {
          Node.this.edges().clear();
        }

        @Override
        public Iterator<ObservableUndirectedGraph.Node<E>> iterator() {
          throw new UnsupportedOperationException();
        }

        @Override
        public boolean contains(Object element) {
          return graph.edges().stream()
              .map(UndirectedGraph.Edge::endpoints)
              .map(ObservableUndirectedGraphImpl::unwrap)
              .map(pair -> Objects.equals(Node.this.element, pair.first()) ? pair.second() : pair.first())
              .anyMatch(neighbor -> Objects.equals(neighbor, element));
        }

        @Override
        public int count() {
          return Node.this.edges().count();
        }

        @Override
        public boolean isPopulated() {
          return graph.edges().stream()
              .map(UndirectedGraph.Edge::endpoints)
              .map(ObservableUndirectedGraphImpl::unwrap)
              .anyMatch(pair -> pair.contains(Node.this.element));
        }

        @Override
        public Stream<ObservableUndirectedGraph.Node<E>> stream() {
          throw new UnsupportedOperationException();
        }
      };
    }
  }

  private static <E> UnorderedPair<E> unwrap(
      UnorderedPair<? extends UndirectedGraph.Node<E>> pair) {
    return UnorderedPair.of(pair.first().element(), pair.second().element());
  }

  private static final class Edge<E> implements ObservableUndirectedGraph.Edge<E> {

    private final ObservableUndirectedGraphImpl<E> graph;
    private final UnorderedPair<E> endpoints;

    private Edge(ObservableUndirectedGraphImpl<E> graph, UnorderedPair<E> endpoints) {
      this.graph = graph;
      this.endpoints = endpoints;
    }

    @Override
    public void remove() {
      graph.mutateState(
          currentState ->
              currentState.edges().stream()
                  .map(UndirectedGraph.Edge::endpoints)
                  .map(ObservableUndirectedGraphImpl::unwrap)
                  .anyMatch(pair -> Objects.equals(pair, endpoints)),
          currentState ->
              currentState.toBuilder().removeEdge(endpoints.first(), endpoints.second()).build(),
          (previousState, currentState) -> ImmutableSet.of(new RemoveEdgeFromGraph<>(endpoints)));
    }

    @Override
    public UnorderedPair<ObservableUndirectedGraph.Node<E>> endpoints() {
      return UnorderedPair.of(
          graph.getOrCreateNode(endpoints.first()), graph.getOrCreateNode(endpoints.second()));
    }
  }

  private Node<E> getOrCreateNode(E element) {
    return new Node<>(this, element);
  }

  private Edge<E> getOrCreateEdge(UnorderedPair<E> pair) {
    return new Edge<>(this, pair);
  }

  private class ObservableChannels
      extends GenericObservableChannels<UndirectedGraph<E>, MutationEvent>
      implements ObservableUndirectedGraph.ObservableChannels<E> {
    private ObservableChannels() {
      super(
          Flowable.<UndirectedGraph<E>>create(
              flowableEmitter -> {
                flowableEmitter.onNext(getState());
                flowableEmitter.onComplete();
              },
              BackpressureStrategy.BUFFER)
              .concatWith(
                  mutationEventSubject.toFlowable(BackpressureStrategy.BUFFER)
                      .map(MutationEvent::state)),
          Flowable.<MutationEvent>create(
              flowableEmitter -> {
                flowableEmitter.onNext(generateMutationEventForNewSubscription());
                flowableEmitter.onComplete();
              },
              BackpressureStrategy.BUFFER)
              .concatWith(
                  mutationEventSubject.toFlowable(BackpressureStrategy.BUFFER)));
    }
  }

  private class MutationEvent
      extends GenericMutationEvent<UndirectedGraph<E>, Set<GraphOperation<E>>>
      implements ObservableUndirectedGraph.MutationEvent<E> {
    private MutationEvent(UndirectedGraph<E> state, Set<GraphOperation<E>> operations) {
      super(state, operations);
    }
  }

  private MutationEvent generateMutationEventForNewSubscription() {
    UndirectedGraph<E> state = getState();
    return new MutationEvent(
        state,
        Stream.concat(
            state.nodes().stream()
                .map(UndirectedGraph.Node::element)
                .map(AddNodeToGraph::new),
            state.edges().stream()
                .map(UndirectedGraph.Edge::endpoints)
                .map(pair -> UnorderedPair.of(pair.first().element(), pair.second().element()))
                .map(AddEdgeToGraph::new))
            .collect(toSet()));
  }

  private static class NodeOperation<E> implements ObservableUndirectedGraph.NodeOperation<E> {

    private final E item;

    protected NodeOperation(E item) {
      this.item = item;
    }

    @Override
    public E item() {
      return item;
    }
  }

  private static class EdgeOperation<E> implements ObservableUndirectedGraph.EdgeOperation<E> {

    private final UnorderedPair<E> pair;

    protected EdgeOperation(UnorderedPair<E> pair) {
      this.pair = pair;
    }

    @Override
    public UnorderedPair<E> endpoints() {
      return pair;
    }
  }

  private static final class AddNodeToGraph<E> extends NodeOperation<E>
      implements ObservableUndirectedGraph.AddNodeToGraph<E> {
    private AddNodeToGraph(E item) {
      super(item);
    }
  }

  private static final class RemoveNodeFromGraph<E> extends NodeOperation<E>
      implements ObservableUndirectedGraph.RemoveNodeFromGraph<E> {
    private RemoveNodeFromGraph(E item) {
      super(item);
    }
  }

  private static final class AddEdgeToGraph<E> extends EdgeOperation<E>
      implements ObservableUndirectedGraph.AddEdgeToGraph<E> {
    private AddEdgeToGraph(UnorderedPair<E> pair) {
      super(pair);
    }
  }

  private static final class RemoveEdgeFromGraph<E> extends EdgeOperation<E>
      implements ObservableUndirectedGraph.RemoveEdgeFromGraph<E> {
    private RemoveEdgeFromGraph(UnorderedPair<E> pair) {
      super(pair);
    }
  }
}
