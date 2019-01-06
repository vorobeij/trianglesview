package au.sjowl.libs.mesh

import android.graphics.Path

fun Path.moveTo(x: Double, y: Double) {
    moveTo(x.toFloat(), y.toFloat())
}

fun Path.lineTo(x: Double, y: Double) {
    lineTo(x.toFloat(), y.toFloat())
}