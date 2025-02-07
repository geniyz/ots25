package site.geniyz.ots.rotating

import site.geniyz.ots.UObject

class RotableAdpater(val o: UObject): Rotable {

    override var direction: Int
        get()= o["direction"] as Int
        set(newDirection){
            o["direction"] = newDirection
        }

    override val angularVelocity: Int
        get()= o["angularVelocity"] as Int

}
