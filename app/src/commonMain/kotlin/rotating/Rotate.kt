package site.geniyz.ots.rotating

class Rotate(
    val r: Rotable
) {
    fun execute(){
        val newDirection = (r.direction + r.angularVelocity)%360
        r.direction = newDirection
    }
}