package site.geniyz.ots.moving

import site.geniyz.ots.UObject
import site.geniyz.ots.Vector
import kotlin.math.cos
import kotlin.math.sin

class MovableAdpater(val o: UObject): Movable {

    override var position: Vector
        get()= o["position"] as Vector
        set(newPosition){
            o["Position"] = newPosition
        }

    override val velocity: Vector
        get(){
            val d = o["direction"] as Double
            val n = o["directionsNumber"] as Int
            val v = o["velocity"] as Int

            return Vector(
                v * cos(d/360*n),
                v * sin(d/360*n),
            )
        }

}
