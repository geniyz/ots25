package site.geniyz.ots.moving

import site.geniyz.ots.Vector

class Move(
    val m: Movable
) {
    fun execute(){
        if(m.position.isNaN()) error("Не корректны координаты, для совершения движения")
        if(m.velocity.isNaN()) error("Не корректна скорость, для совершения движения")

        m.position += m.velocity
    }
}