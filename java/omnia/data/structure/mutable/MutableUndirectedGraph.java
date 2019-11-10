package omnia.data.structure.mutable;

import java.util.Optional;
import omnia.data.structure.UndirectedGraph;
import omnia.data.structure.UnorderedPair;

public interface MutableUndirectedGraph<E> extends MutableGraph<E>, UndirectedGraph<E> {

  interface MutableUndirectedNode<E> extends MutableNode<E>, UndirectedNode<E> {

    @Override
    MutableSet<? extends MutableUndirectedEdge<E>> edges();

    @Override
    MutableSet<? extends MutableUndirectedNode<E>> neighbors();
  }

  interface MutableUndirectedEdge<E> extends MutableEdge<E>, UndirectedEdge<E> {

    @Override
    UnorderedPair<? extends MutableUndirectedNode<E>> endpoints();
  }

  @Override
  Optional<? extends MutableUndirectedNode<E>> nodeOf(E element);

  @Override
  MutableSet<? extends MutableUndirectedNode<E>> nodes();

  @Override
  MutableSet<? extends MutableUndirectedEdge<E>> edges();
}
