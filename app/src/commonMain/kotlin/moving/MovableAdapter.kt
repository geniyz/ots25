package site.geniyz.ots.moving

import site.geniyz.ots.UObject
import site.geniyz.ots.Vector

class MovableAdapter(val o: UObject): Movable {

    override var position: Vector
        get()= (o["position"] ?: Vector.NONE) as Vector
        set(newPosition){
            o["position"] = newPosition
        }

    override val velocity: Vector
        get()= (o["velocity"] ?: Vector.NONE) as Vector

}
