package com.github.mperry.graph

case class Graph (edges: Map[(Node, Node), Edge]) {

  def shortestPath(from: Node, to: Node): Option[List[Edge]] = {
    None
  }

  def shortestPath(from: Node, to: Node, distances: Map[Node, List[Edge]]) = {
  }

  def distance(list: List[Edge]): Int = {
    list.foldLeft(0)((d, e) => d + e.distance)
  }

}
