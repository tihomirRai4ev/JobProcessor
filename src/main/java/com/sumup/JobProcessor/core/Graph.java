package com.sumup.JobProcessor.core;

/**
 * Class representing Graph with implementation of topological sort used to classify task by their
 * dependencies. The result might be exception in case of cyclic dependencies or misconfiguration.
 *
 * @author Tihomir Raychev
 */
public final class Graph {

  /**
   * Helper inner class to represent а Vertex in the Graph.
   */
  public static class Vertex {

    private String taskName;

    public Vertex(String taskName) {
      this.taskName = taskName;
    }
  }

  private Vertex[] vertexList;

  private int[][] adjacencyMatrix;

  private int numVerts; // current number of vertices

  private String[] sortedArray;

  public Graph(int size) {
    vertexList = new Vertex[size];
    adjacencyMatrix = new int[size][size];
    numVerts = 0;
    for (int i = 0; i < size; i++) {
      for (int k = 0; k < size; k++) {
        adjacencyMatrix[i][k] = 0;
      }
    }
    sortedArray = new String[size];
  }

  public void addVertex(String name) {
    vertexList[numVerts++] = new Vertex(name);
  }

  public void addEdge(int start, int end) {
    adjacencyMatrix[start][end] = 1;
  }

  public void performTopologicalSort() {
    while (numVerts > 0) {
      // get a vertex with no successors, or -1
      int currentVertex = noSuccessors();
      // must be a cycle
      if (currentVertex == -1) {
        System.out.println("ERROR: Graph has cycles");
        return;
      }
      // insert vertex label in sorted array (start at end)
      sortedArray[numVerts - 1] = vertexList[currentVertex].taskName;

      deleteVertex(currentVertex); // delete vertex
    }

    // TODO (traychev) add specific checked exception.
    for (String val : sortedArray) {
      if (val == null) {
        throw new RuntimeException("Cyclic dependency detected, aborting job");
      }
    }
  }

  public String[] getSortedArray() {
    return sortedArray;
  }

  // returns vertex with no successors (or -1 if no such verts)
  public int noSuccessors() {
    boolean isEdge;

    for (int row = 0; row < numVerts; row++) {
      isEdge = false; // check edges
      for (int col = 0; col < numVerts; col++) {
        // if edge to another
        if (adjacencyMatrix[row][col] > 0) {
          isEdge = true;
          break; // this vertex has a successor try another
        }
      }
      // if no edges, has no successors
      if (!isEdge) {
        return row;
      }
    }
    return -1; // no
  }

  public void deleteVertex(int delVert) {
    // if not last vertex, delete from vertexList
    if (delVert != numVerts - 1) {
      if (numVerts - 1 - delVert >= 0) {
        System.arraycopy(vertexList, delVert + 1, vertexList, delVert, numVerts - 1 - delVert);
      }
      for (int row = delVert; row < numVerts - 1; row++) {
        moveRowUp(row, numVerts);
      }
      for (int col = delVert; col < numVerts - 1; col++) {
        moveColLeft(col, numVerts - 1);
      }
    }
    numVerts--; // one less vertex
  }

  private void moveRowUp(int row, int length) {
    if (length >= 0) {
      System.arraycopy(adjacencyMatrix[row + 1], 0, adjacencyMatrix[row], 0, length);
    }
  }

  private void moveColLeft(int col, int length) {
    for (int row = 0; row < length; row++) {
      adjacencyMatrix[row][col] = adjacencyMatrix[row][col + 1];
    }
  }
}
