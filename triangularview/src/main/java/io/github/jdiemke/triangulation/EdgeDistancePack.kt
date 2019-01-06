package io.github.jdiemke.triangulation

class EdgeDistancePack(
    var edge: Edge2D,
    var distance: Double
) : Comparable<EdgeDistancePack> {

    override fun compareTo(o: EdgeDistancePack): Int {
        return java.lang.Double.compare(this.distance, o.distance)
    }
}