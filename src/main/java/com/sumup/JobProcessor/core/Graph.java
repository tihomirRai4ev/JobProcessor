package com.sumup.JobProcessor.core;

/**
 * Class representing Graph with implementation of topological sort used to classify task by their
 * dependencies. The result might be exception in case of cyclic dependencies or misconfiguration.
 *
 * @author Tihomir Raychev
 */
public class Graph {

  /**
   * Helper inner class to represent Vertex in the Graph.
   */
  public static class Vertex {

    public String taskName;

    public Vertex(String taskName) {
      this.taskName = taskName;
    }
  }

  private final int MAX_VERTS = 200;

  private Vertex[] vertexList; // list of vertices

  private int[][] matrix; // adjacency matrix

  private int numVerts; // current number of vertices

  private String[] sortedArray;

  public Graph() {
    vertexList = new Vertex[MAX_VERTS];
    matrix = new int[MAX_VERTS][MAX_VERTS];
    numVerts = 0;
    for (int i = 0; i < MAX_VERTS; i++) {
      for (int k = 0; k < MAX_VERTS; k++) {
        matrix[i][k] = 0;
      }
    }
    sortedArray = new String[MAX_VERTS]; // sorted vert labels
  }

  public void addVertex(String name) {
    vertexList[numVerts++] = new Vertex(name);
  }

  public void addEdge(int start, int end) {
    matrix[start][end] = 1;
  }

  public void displayVertex(int v) {
    System.out.print(vertexList[v].taskName);
  }

  public void performTopologicalSort() {
    int orig_nVerts = numVerts;

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

    // vertices all gone; display sortedArray
    System.out.print("Topologically sorted order: ");
    for (int j = 0; j < orig_nVerts; j++) {
      System.out.print(sortedArray[j] + " ");
    }
    System.out.println();
  }

  public String[] getSortedArray() {
    return sortedArray;
  }

  // returns vert with no successors (or -1 if no such verts)
  public int noSuccessors() {
    boolean isEdge;

    for (int row = 0; row < numVerts; row++) {
      isEdge = false; // check edges
      for (int col = 0; col < numVerts; col++) {
        // if edge to another
        if (matrix[row][col] > 0) {
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
      System.arraycopy(matrix[row + 1], 0, matrix[row], 0, length);
    }
  }

  private void moveColLeft(int col, int length) {
    for (int row = 0; row < length; row++) {
      matrix[row][col] = matrix[row][col + 1];
    }
  }
}
