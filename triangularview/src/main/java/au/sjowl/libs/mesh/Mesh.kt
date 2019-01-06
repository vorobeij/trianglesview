package au.sjowl.libs.mesh

import android.graphics.Color
import io.github.jdiemke.triangulation.DelaunayTriangulator
import io.github.jdiemke.triangulation.NotEnoughPointsException
import io.github.jdiemke.triangulation.Triangle2D
import io.github.jdiemke.triangulation.Vector2D
import java.util.Random

class Mesh {
    var width: Int = 0
    var height: Int = 0
    var dots: Int = 20
    var baseColor: Int = 0
        set(value) {
            field = value
            Color.colorToHSV(field, hsv)
            Color.colorToHSV(field, hsvOrig)
        }

    lateinit var mesh: List<Triangle2D>
    val grid = ArrayList<Vector2D>()
    val sideX = dots / 4
    val sideY = dots / 4
    val r = Random()

    private val hRandom = 0.2f
    private val hsv = floatArrayOf(0f, 0f, 0f)
    private val hsvOrig = floatArrayOf(0f, 0f, 0f)

    fun createMesh() {
        grid.clear()

        (0..dots).forEach { grid.add(Vector2D(r.nextInt(width).toDouble(), r.nextInt(height).toDouble())) }
        (0..sideX).forEach {
            grid.add(Vector2D(0.0, r.nextInt(height).toDouble()))
            grid.add(Vector2D(1.0 * width, r.nextInt(height).toDouble()))
        }
        (0..sideY).forEach {
            grid.add(Vector2D(r.nextInt(width).toDouble(), 0.0))
            grid.add(Vector2D(r.nextInt(width).toDouble(), 1.0 * height))
        }

        grid.add(Vector2D(0.0, 0.0))
        grid.add(Vector2D(0.0, 1.0 * height))
        grid.add(Vector2D(1.0 * width, 1.0 * height))
        grid.add(Vector2D(1.0 * width, 0.0))

        try {
            val delaunayTriangulator = DelaunayTriangulator(grid)
            delaunayTriangulator.triangulate()
            mesh = delaunayTriangulator.triangles
        } catch (e: NotEnoughPointsException) {
        }
    }

    /**
     *  Hue [0..360]
     *  Saturation 0..1
     *  Value [0...1]
     */
    val color: Int
        get() {
            hsv[2] = hsvOrig[2] + r.nextFloat() * hRandom
            hsv[2] -= r.nextFloat() * hRandom
            return Color.HSVToColor(hsv)
        }

    override fun toString(): String = mesh.toString()
}