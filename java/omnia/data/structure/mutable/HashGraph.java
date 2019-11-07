package omnia.data.structure.mutable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toSet;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import omnia.data.iterate.FilterIterator;
import omnia.data.iterate.MappingIterator;
import omnia.data.iterate.WrapperIterator;
import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableUnorderedPair;

public final class HashGraph<E> implements MutableGraph<E> {

  private final MutableSet<E> elements = new HashSet<>();
  private final MutableSet<ImmutableUnorderedPair<E>> edges = new HashSet<>();

  @Override
  public void add(E item) {
    requireNonNull(item);
    elements.add(item);
  }

  @Override
  public boolean remove(E item) {
    requireNonNull(item);
    removeEdgesForNode(item);
    return elements.remove(item);
  }

  @Override
  public void replace(E original, E replacement) {
    requireNonNull(original);
    requireNonNull(replacement);
    if (!elements.contains(original)) {
      throw new IllegalArgumentException(
          "tried to replace an element that does not already exist in the graph. original="
              + original
              + " replacement="
              + replacement);
    }
    elements.remove(original);
    Set<ImmutableUnorderedPair<E>> edges = edgesOf(original).collect(toSet());
    Set<E> neighbors = neighborsOf(original).collect(toSet());
    edges.forEach(this.edges::remove);
    elements.add(replacement);
    neighbors.stream()
        .map(neighbor -> new ImmutableUnorderedPair<>(neighbor, replacement))
        .forEach(this.edges::add);
  }

  @Override
  public void clear() {
    elements.clear();
    edges.clear();
  }

  private void removeEdgesForNode(E item) {
    edgesOf(item).collect(toSet()).forEach(edges::remove);
  }

  private Stream<ImmutableUnorderedPair<E>> edgesOf(E element) {
    return edges.stream().filter(pair -> pair.contains(element));
  }

  private Stream<E> neighborsOf(E element) {
    return edgesOf(element)
        .map(pair -> pair.first().equals(element) ? pair.second() : pair.first());
  }

  private MutableNode<E> getOrCreateNode(E element) {
    return new MutableNode<>(this, element);
  }

  private MutableEdge<E> getOrCreateEdge(ImmutableUnorderedPair<E> pair) {
    return new MutableEdge<>(this, pair);
  }

  @Override
  public void addEdge(E first, E second) {
    requireNonNull(first);
    requireNonNull(second);
    {
      boolean containsFirst = elements.contains(first);
      boolean containsSecond = elements.contains(second);
      if (!containsFirst || !containsSecond) {
        throw new IllegalArgumentException(
            "tried to create an edge edge for at least one nonexistent node. first="
                + first + "(" + (containsFirst ? "exists" : "does not exist") + ")"
                + " second="
                + second + "(" + (containsSecond ? "exists" : "does not exist") + ")");
      }
    }
    edges.add(new ImmutableUnorderedPair<>(first, second));
  }

  @Override
  public boolean removeEdge(E first, E second) {
    requireNonNull(first);
    requireNonNull(second);
    return edges.remove(new ImmutableUnorderedPair<>(first, second));
  }

  @Override
  public Optional<MutableNode<E>> nodeOf(E element) {
    requireNonNull(element);
    return elements.contains(element) ? Optional.of(getOrCreateNode(element)) : Optional.empty();
  }

  @Override
  public MutableSet<E> contents() {
    return new MutableSet<>() {

      @Override
      public Stream<E> stream() {
        return elements.stream();
      }

      @Override
      public int count() {
        return elements.count();
      }

      @Override
      public boolean isPopulated() {
        return elements.isPopulated();
      }

      @Override
      public boolean contains(Object element) {
        return elements.contains(element);
      }

      @Override
      public Iterator<E> iterator() {
        return HashGraph.this.iterator();
      }

      @Override
      public void add(E element) {
        elements.add(element);
      }

      @Override
      public boolean remove(E element) {
        return elements.remove(element);
      }

      @Override
      public void clear() {
        HashGraph.this.clear();
      }
    };
  }

  @Override
  public MutableSet<MutableNode<E>> nodes() {
    return new MutableSet<>() {
      @Override
      public Stream<MutableNode<E>> stream() {
        return elements.stream().map(HashGraph.this::getOrCreateNode);
      }

      @Override
      public void add(MutableNode<E> element) {
        requireNonNull(element);
        requireSameGraph(HashGraph.this, element.graph);
        HashGraph.this.add(element.element());
      }

      @Override
      public boolean remove(MutableNode<E> element) {
        requireNonNull(element);
        requireSameGraph(HashGraph.this, element.graph);
        return HashGraph.this.remove(element.element());
      }

      @Override
      public void clear() {
        HashGraph.this.clear();
      }

      @Override
      public Iterator<MutableNode<E>> iterator() {
        return new MappingIterator<>(HashGraph.this.iterator(), HashGraph.this::getOrCreateNode);
      }

      @Override
      public boolean contains(Object node) {
        return node instanceof MutableNode<?>
            && ((MutableNode<?>) node).graph == HashGraph.this
            && elements.contains(((MutableNode<?>) node).element);
      }

      @Override
      public int count() {
        return elements.count();
      }

      @Override
      public boolean isPopulated() {
        return elements.isPopulated();
      }
    };
  }

  @Override
  public MutableSet<MutableEdge<E>> edges() {
    return new MutableSet<>() {

      @Override
      public Stream<MutableEdge<E>> stream() {
        return edges.stream().map(HashGraph.this::getOrCreateEdge);
      }

      @Override
      public int count() {
        return edges.count();
      }

      @Override
      public boolean isPopulated() {
        return edges.isPopulated();
      }

      @Override
      public boolean contains(Object edge) {
        return edge instanceof MutableEdge<?>
            && ((MutableEdge<?>) edge).graph == HashGraph.this
            && edges.contains(((MutableEdge<?>) edge).pair);
      }

      @Override
      public Iterator<MutableEdge<E>> iterator() {
        return new MappingIterator<>(edges.iterator(), HashGraph.this::getOrCreateEdge);
      }

      @Override
      public void add(MutableEdge<E> edge) {
        requireNonNull(edge);
        requireSameGraph(HashGraph.this, edge.graph);
        addEdge(edge.pair.first(), edge.pair.second());
      }

      @Override
      public boolean remove(MutableEdge<E> edge) {
        return edge.graph == HashGraph.this
            && edges.remove(
                new ImmutableUnorderedPair<>(edge.pair.first(), edge.pair.second()));
      }

      @Override
      public void clear() {
        edges.clear();
      }
    };
  }

  @Override
  public boolean contains(Object element) {
    return elements.contains(element);
  }

  @Override
  public int count() {
    return elements.count();
  }

  @Override
  public boolean isPopulated() {
    return elements.isPopulated();
  }

  @Override
  public Iterator<E> iterator() {
    return new WrapperIterator<>(elements.iterator()) {
      @Override
      public void remove() {
        super.remove();
        removeEdgesForNode(current());
      }
    };
  }

  @Override
  public Stream<E> stream() {
    return elements.stream();
  }

  public static final class MutableNode<E> implements MutableGraph.MutableNode<E> {

    private final HashGraph<E> graph;
    private final E element;

    private MutableNode(HashGraph<E> graph, E element) {
      this.graph = graph;
      this.element = element;
    }

    @Override
    public void replaceValue(E element) {
      graph.replace(this.element, element);
    }

    @Override
    public void remove() {
      graph.remove(element);
    }

    @Override
    public E element() {
      return element;
    }

    @Override
    public MutableSet<MutableEdge<E>> edges() {
      return new MutableSet<>() {

        @Override
        public void add(MutableEdge<E> edge) {
          requireNonNull(edge);
          requireSameGraph(edge);
          {
            if (!edge.pair.contains(node())) {
              throw new IllegalArgumentException(
                  "attempted to add an edge to a node whose endpoints do not contain the node. "
                      + "node="
                      + node()
                      + " edge="
                      + edge);
            }
          }
          graph.addEdge(edge.pair.first(), edge.pair.second());
        }

        @Override
        public boolean remove(MutableEdge<E> edge) {
          requireNonNull(edge);
          requireSameGraph(edge);
          // remove the edge from the graph IFF the edge is related to the associated node
          return edge.pair.contains(element) && graph.removeEdge(edge.pair.first(), edge.pair.second());
        }

        private MutableNode node() {
          return MutableNode.this;
        }

        private void requireSameGraph(MutableEdge<E> other) {
          HashGraph.requireSameGraph(graph, other.graph);
        }

        @Override
        public void clear() {
          graph.removeEdgesForNode(element);
        }

        @Override
        public Iterator<MutableEdge<E>> iterator() {
          return new MappingIterator<>(
              new FilterIterator<>(graph.edges.iterator(), pair -> pair.contains(element)),
              graph::getOrCreateEdge);
        }

        @Override
        public boolean contains(Object otherObject) {
          return otherObject instanceof MutableEdge<?>
              && ((MutableEdge<?>) otherObject).graph == graph
              && ((MutableEdge<?>) otherObject).pair.contains(element)
              && graph.edges.contains(((MutableEdge<?>) otherObject).pair);
        }

        @Override
        public int count() {
          return (int) stream().count();
        }

        @Override
        public boolean isPopulated() {
          return stream().anyMatch(any -> true);
        }

        @Override
        public Stream<MutableEdge<E>> stream() {
          return graph.edges.stream()
              .filter(pair -> pair.contains(element))
              .map(graph::getOrCreateEdge);
        }
      };
    }

    @Override
    public MutableSet<MutableNode<E>> neighbors() {
      return new MutableSet<>() {
        @Override
        public void add(MutableNode<E> node) {
          requireSameGraph(node);
          graph.add(node.element);
        }

        private void requireSameGraph(MutableNode<E> other) {
          HashGraph.requireSameGraph(graph, other.graph);
        }

        @Override
        public boolean remove(MutableNode<E> node) {
          requireSameGraph(node);
          return graph.edges.remove(new ImmutableUnorderedPair<>(node.element, element));
        }

        @Override
        public void clear() {
          graph.removeEdgesForNode(element);
        }

        @Override
        public Iterator<MutableNode<E>> iterator() {
          return new MappingIterator<>(
              new MappingIterator<>(
                  new FilterIterator<>(graph.edges.iterator(), pair -> pair.contains(element)),
                  pair -> Objects.equals(pair.first(), element) ? pair.second() : pair.first()),
              graph::getOrCreateNode);
        }

        @Override
        public boolean contains(Object otherNode) {
          return otherNode instanceof MutableNode
              && ((MutableNode<?>) otherNode).graph == graph
              && graph.edges.contains(
                  new ImmutableUnorderedPair<>(element, ((MutableNode<?>) otherNode).element));
        }

        @Override
        public int count() {
          return (int) stream().count();
        }

        @Override
        public boolean isPopulated() {
          return stream().anyMatch(any -> true);
        }

        @Override
        public Stream<MutableNode<E>> stream() {
          return graph.edges.stream()
              .filter(pair -> pair.contains(element))
              .map(pair -> Objects.equals(pair.first(), element) ? pair.second() : pair.first())
              .map(graph::getOrCreateNode);
        }
      };
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof MutableNode
          && Objects.equals(((MutableNode<?>) other).graph, graph)
          && Objects.equals(((MutableNode<?>) other).element, element);
    }

    @Override
    public int hashCode() {
      return Objects.hash(graph, element);
    }
  }

  public static final class MutableEdge<E> implements MutableGraph.MutableEdge<E> {

    private final HashGraph<E> graph;
    private final ImmutableUnorderedPair<E> pair;

    private MutableEdge(HashGraph<E> graph, ImmutableUnorderedPair<E> pair) {
      this.graph = graph;
      this.pair = pair;
    }

    @Override
    public void remove() {
      graph.edges.remove(pair);
    }

    @Override
    public ImmutableUnorderedPair<MutableNode<E>> endpoints() {
      return ImmutableUnorderedPair.of(
              graph.getOrCreateNode(pair.first()), graph.getOrCreateNode(pair.second()));
    }
  }

  private static void requireSameGraph(HashGraph<?> expected, HashGraph<?> actual) {
    if (expected != actual) {
      throw new IllegalArgumentException(
          "item does not belong to this graph. this=" + expected + " other=" + actual);
    }
  }
}
