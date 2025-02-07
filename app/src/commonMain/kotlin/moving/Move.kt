package site.geniyz.ots.moving

class Move(
    val m: Movable
) {
    fun execute(){
        if(m.position != null) {
            m.position = m.position!! + m.velocity
        }else{
            error("Не известны координаты, для совершения движения")
        }
    }
}