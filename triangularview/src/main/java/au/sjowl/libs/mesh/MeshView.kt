package au.sjowl.libs.mesh

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import au.sjowl.lib.triangulation.R
import io.github.jdiemke.triangulation.Triangle2D

class MeshView : View {

    private val paint = Paint().apply {
        style = Paint.Style.FILL
    }

    private val mesh = Mesh()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(attrs)
    }

    private fun initAttributes(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MeshView, 0, 0)
        mesh.dots = a.getInteger(R.styleable.MeshView_mv_dots, 20)
        mesh.baseColor = a.getColor(R.styleable.MeshView_mv_baseColor, Color.parseColor("#6ED37C"))
        a.recycle()
    }

    private fun drawTriangle(c: Canvas, d: Triangle2D) {
        val path = Path().apply {
            moveTo(d.a.x, d.a.y)
            lineTo(d.b.x, d.b.y)
            moveTo(d.b.x, d.b.y)
            lineTo(d.c.x, d.c.y)
            lineTo(d.a.x, d.a.y)
            close()
        }
        c.drawPath(path, paint.apply {
            color = mesh.color
        })
    }

    override fun onDraw(canvas: Canvas) {
        mesh.width = width
        mesh.height = height
        mesh.createMesh()
        mesh.mesh.forEach { drawTriangle(canvas, it) }
    }
}
