package site.geniyz.ots.rotating

import site.geniyz.ots.commands.Executable

class Rotate(
    val r: Rotable
): Executable {
    override fun execute(){
        val newDirection = (r.direction + r.angularVelocity)%360
        r.direction = newDirection
    }
}
