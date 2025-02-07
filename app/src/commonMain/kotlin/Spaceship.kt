package site.geniyz.ots

import site.geniyz.ots.moving.Movable
import site.geniyz.ots.rotating.Rotable

class Spaceship(
    override val velocity: Vector = Vector.NONE,
    override var position: Vector = Vector.NONE,

    override val angularVelocity: Int = 0,
    override var direction: Int = 0,

) : Movable, Rotable
