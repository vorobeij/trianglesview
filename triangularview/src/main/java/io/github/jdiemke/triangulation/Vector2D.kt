package io.github.jdiemke.triangulation

class Vector2D(
    var x: Double,
    var y: Double
) {

    fun sub(vector: Vector2D): Vector2D {
        return Vector2D(this.x - vector.x, this.y - vector.y)
    }

    fun add(vector: Vector2D): Vector2D {
        return Vector2D(this.x + vector.x, this.y + vector.y)
    }

    fun mult(scalar: Double): Vector2D {
        return Vector2D(this.x * scalar, this.y * scalar)
    }

    fun mag(): Double {
        return Math.sqrt(this.x * this.x + this.y * this.y)
    }

    fun dot(vector: Vector2D): Double {
        return this.x * vector.x + this.y * vector.y
    }

    fun cross(vector: Vector2D): Double {
        return this.y * vector.x - this.x * vector.y
    }

    override fun toString(): String {
        return "Vector2D[$x, $y]"
    }
}