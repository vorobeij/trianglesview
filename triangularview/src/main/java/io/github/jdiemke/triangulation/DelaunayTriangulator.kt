package io.github.jdiemke.triangulation

import java.util.ArrayList
import java.util.Collections

/**
 * A Java implementation of an incremental 2D Delaunay triangulation algorithm.
 *
 * @author Johannes Diemke
 */
class DelaunayTriangulator
/**
 * Constructor of the SimpleDelaunayTriangulator class used to create a new
 * triangulator instance.
 *
 * @param pointSet The point set to be triangulated
 */
(pointSet: List<Vector2D>) {

    /**
     * Returns the point set in form of a vector of 2D vectors.
     *
     * @return Returns the points set.
     */
    var pointSet: List<Vector2D>? = null
        private set
    private var triangleSoup: TriangleSoup? = null

    /**
     * Returns the trianges of the triangulation in form of a vector of 2D
     * triangles.
     *
     * @return Returns the triangles of the triangulation.
     */
    val triangles: List<Triangle2D>
        get() = triangleSoup!!.triangles

    init {
        this.pointSet = pointSet
        this.triangleSoup = TriangleSoup()
    }

    /**
     * This method generates a Delaunay triangulation from the specified point
     * set.
     *
     * @throws NotEnoughPointsException Thrown when the point set contains less than three points
     */
    @Throws(NotEnoughPointsException::class)
    fun triangulate() {
        triangleSoup = TriangleSoup()

        if (pointSet == null || pointSet!!.size < 3) {
            throw NotEnoughPointsException("Less than three points in point set.")
        }

        /**
         * In order for the in circumcircle test to not consider the vertices of
         * the super triangle we have to start out with a big triangle
         * containing the whole point set. We have to scale the super triangle
         * to be very large. Otherwise the triangulation is not convex.
         */
        var maxOfAnyCoordinate = 0.0

        for (vector in pointSet!!) {
            maxOfAnyCoordinate = Math.max(Math.max(vector.x, vector.y), maxOfAnyCoordinate)
        }

        maxOfAnyCoordinate *= 16.0

        val p1 = Vector2D(0.0, 3.0 * maxOfAnyCoordinate)
        val p2 = Vector2D(3.0 * maxOfAnyCoordinate, 0.0)
        val p3 = Vector2D(-3.0 * maxOfAnyCoordinate, -3.0 * maxOfAnyCoordinate)

        val superTriangle = Triangle2D(p1, p2, p3)

        triangleSoup!!.add(superTriangle)

        for (i in pointSet!!.indices) {
            val triangle = triangleSoup!!.findContainingTriangle(pointSet!![i])

            if (triangle == null) {
                /**
                 * If no containing triangle exists, then the vertex is not
                 * inside a triangle (this can also happen due to numerical
                 * errors) and lies on an edge. In order to find this edge we
                 * search all edges of the triangle soup and select the one
                 * which is nearest to the point we try to add. This edge is
                 * removed and four new edges are added.
                 */
                val edge = triangleSoup!!.findNearestEdge(pointSet!![i])

                val first = triangleSoup!!.findOneTriangleSharing(edge)
                    ?: throw IllegalStateException("")
                val second = triangleSoup!!.findNeighbour(first, edge)
                    ?: throw IllegalStateException("")

                val firstNoneEdgeVertex = first.getNoneEdgeVertex(edge)
                    ?: throw IllegalStateException("")
                val secondNoneEdgeVertex = second.getNoneEdgeVertex(edge)
                    ?: throw IllegalStateException("")

                triangleSoup!!.remove(first)
                triangleSoup!!.remove(second)

                val triangle1 = Triangle2D(edge.a, firstNoneEdgeVertex, pointSet!![i])
                val triangle2 = Triangle2D(edge.b, firstNoneEdgeVertex, pointSet!![i])
                val triangle3 = Triangle2D(edge.a, secondNoneEdgeVertex, pointSet!![i])
                val triangle4 = Triangle2D(edge.b, secondNoneEdgeVertex, pointSet!![i])

                triangleSoup!!.add(triangle1)
                triangleSoup!!.add(triangle2)
                triangleSoup!!.add(triangle3)
                triangleSoup!!.add(triangle4)

                legalizeEdge(triangle1, Edge2D(edge.a, firstNoneEdgeVertex), pointSet!![i])
                legalizeEdge(triangle2, Edge2D(edge.b, firstNoneEdgeVertex), pointSet!![i])
                legalizeEdge(triangle3, Edge2D(edge.a, secondNoneEdgeVertex), pointSet!![i])
                legalizeEdge(triangle4, Edge2D(edge.b, secondNoneEdgeVertex), pointSet!![i])
            } else {
                /**
                 * The vertex is inside a triangle.
                 */
                val a = triangle.a
                val b = triangle.b
                val c = triangle.c

                triangleSoup!!.remove(triangle)

                val first = Triangle2D(a, b, pointSet!![i])
                val second = Triangle2D(b, c, pointSet!![i])
                val third = Triangle2D(c, a, pointSet!![i])

                triangleSoup!!.add(first)
                triangleSoup!!.add(second)
                triangleSoup!!.add(third)

                legalizeEdge(first, Edge2D(a, b), pointSet!![i])
                legalizeEdge(second, Edge2D(b, c), pointSet!![i])
                legalizeEdge(third, Edge2D(c, a), pointSet!![i])
            }
        }

        /**
         * Remove all triangles that contain vertices of the super triangle.
         */
        triangleSoup!!.removeTrianglesUsing(superTriangle.a)
        triangleSoup!!.removeTrianglesUsing(superTriangle.b)
        triangleSoup!!.removeTrianglesUsing(superTriangle.c)
    }

    /**
     * This method legalizes edges by recursively flipping all illegal edges.
     *
     * @param triangle The triangle
     * @param edge The edge to be legalized
     * @param newVertex The new vertex
     */
    private fun legalizeEdge(triangle: Triangle2D, edge: Edge2D, newVertex: Vector2D) {
        val neighbourTriangle = triangleSoup!!.findNeighbour(triangle, edge)

        /**
         * If the triangle has a neighbor, then legalize the edge
         */
        if (neighbourTriangle != null) {
            if (neighbourTriangle.isPointInCircumcircle(newVertex)) {
                triangleSoup!!.remove(triangle)
                triangleSoup!!.remove(neighbourTriangle)

                val noneEdgeVertex = neighbourTriangle.getNoneEdgeVertex(edge)
                    ?: throw IllegalStateException("")

                val firstTriangle = Triangle2D(noneEdgeVertex, edge.a, newVertex)
                val secondTriangle = Triangle2D(noneEdgeVertex, edge.b, newVertex)

                triangleSoup!!.add(firstTriangle)
                triangleSoup!!.add(secondTriangle)

                legalizeEdge(firstTriangle, Edge2D(noneEdgeVertex, edge.a), newVertex)
                legalizeEdge(secondTriangle, Edge2D(noneEdgeVertex, edge.b), newVertex)
            }
        }
    }

    /**
     * Creates a random permutation of the specified point set. Based on the
     * implementation of the Delaunay algorithm this can speed up the
     * computation.
     */
    fun shuffle() {
        Collections.shuffle(pointSet!!)
    }

    /**
     * Shuffles the point set using a custom permutation sequence.
     *
     * @param permutation The permutation used to shuffle the point set
     */
    fun shuffle(permutation: IntArray) {
        val temp = ArrayList<Vector2D>()
        for (i in permutation.indices) {
            temp.add(pointSet!![permutation[i]])
        }
        pointSet = temp
    }
}