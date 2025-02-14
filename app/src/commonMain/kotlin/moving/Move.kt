package site.geniyz.ots.moving

import site.geniyz.ots.commands.Executable

class Move(
    val m: Movable
): Executable {
    override fun execute(){
        if(m.position.isNaN()) throw BadStartPosition()
        if(m.velocity.isNaN()) throw BadVelocity()

        val newPos = m.position + m.velocity
        if(newPos.isNaN()) throw BadEndPosition()
        m.position = newPos
    }
}