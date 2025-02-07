package site.geniyz.ots.moving

import site.geniyz.ots.Vector

interface Movable {
    val velocity: Vector
    var position: Vector?
}
