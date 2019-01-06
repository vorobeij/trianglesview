package io.github.jdiemke.triangulation

import java.util.ArrayList

/**
 * Triangle soup class implementation.
 *
 * @author Johannes Diemke
 */
internal class TriangleSoup {

    private val triangleSoup: MutableList<Triangle2D>

    /**
     * Returns the triangles from this triangle soup.
     *
     * @return The triangles from this triangle soup
     */
    val triangles: List<Triangle2D>
        get() = this.triangleSoup

    /**
     * Constructor of the triangle soup class used to create a new triangle soup
     * instance.
     */
    init {
        this.triangleSoup = ArrayList()
    }

    /**
     * Adds a triangle to this triangle soup.
     *
     * @param triangle The triangle to be added to this triangle soup
     */
    fun add(triangle: Triangle2D) {
        this.triangleSoup.add(triangle)
    }

    /**
     * Removes a triangle from this triangle soup.
     *
     * @param triangle The triangle to be removed from this triangle soup
     */
    fun remove(triangle: Triangle2D) {
        this.triangleSoup.remove(triangle)
    }

    /**
     * Returns the triangle from this triangle soup that contains the specified
     * point or null if no triangle from the triangle soup contains the point.
     *
     * @param point The point
     * @return Returns the triangle from this triangle soup that contains the
     * specified point or null
     */
    fun findContainingTriangle(point: Vector2D): Triangle2D? {
        for (triangle in triangleSoup) {
            if (triangle.contains(point)) {
                return triangle
            }
        }
        return null
    }

    /**
     * Returns the neighbor triangle of the specified triangle sharing the same
     * edge as specified. If no neighbor sharing the same edge exists null is
     * returned.
     *
     * @param triangle The triangle
     * @param edge The edge
     * @return The triangles neighbor triangle sharing the same edge or null if
     * no triangle exists
     */
    fun findNeighbour(triangle: Triangle2D, edge: Edge2D): Triangle2D? {
        for (triangleFromSoup in triangleSoup) {
            if (triangleFromSoup.isNeighbour(edge) && triangleFromSoup !== triangle) {
                return triangleFromSoup
            }
        }
        return null
    }

    /**
     * Returns one of the possible triangles sharing the specified edge. Based
     * on the ordering of the triangles in this triangle soup the returned
     * triangle may differ. To find the other triangle that shares this edge use
     * the [] method.
     *
     * @param edge The edge
     * @return Returns one triangle that shares the specified edge
     */
    fun findOneTriangleSharing(edge: Edge2D): Triangle2D? {
        for (triangle in triangleSoup) {
            if (triangle.isNeighbour(edge)) {
                return triangle
            }
        }
        return null
    }

    /**
     * Returns the edge from the triangle soup nearest to the specified point.
     *
     * @param point The point
     * @return The edge from the triangle soup nearest to the specified point
     */
    fun findNearestEdge(point: Vector2D): Edge2D {
        val edgeList = ArrayList<EdgeDistancePack>()

        for (triangle in triangleSoup) {
            edgeList.add(triangle.findNearestEdge(point))
        }

        return edgeList.sorted()[0].edge
    }

    /**
     * Removes all triangles from this triangle soup that contain the specified
     * vertex.
     *
     * @param vertex The vertex
     */
    fun removeTrianglesUsing(vertex: Vector2D) {
        val trianglesToBeRemoved = ArrayList<Triangle2D>()

        for (triangle in triangleSoup) {
            if (triangle.hasVertex(vertex)) {
                trianglesToBeRemoved.add(triangle)
            }
        }

        triangleSoup.removeAll(trianglesToBeRemoved)
    }
}