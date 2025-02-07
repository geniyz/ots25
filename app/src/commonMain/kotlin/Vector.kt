package site.geniyz.ots

class Vector(
    val x: Double,
    val y: Double,
){
    constructor(x:Int, y:Int): this(x.toDouble(), y.toDouble())

    operator fun plus(v: Vector) = Vector(
        x = x+v.x,
        y = y+v.y,
    )

    operator fun times(n: Double) = Vector(
        x = x * n,
        y = y * n,
    )

    fun isNaN() = (x.isNaN() || y.isNaN()) ||
            (x.isInfinite() || y.isInfinite())

            companion object{
        val NONE = Vector(Double.NaN, Double.NaN)
    }
}
