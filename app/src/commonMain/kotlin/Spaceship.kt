package site.geniyz.ots

import site.geniyz.ots.moving.Movable

class Spaceship(
    override val velocity: Vector = Vector(0, 0),
    override var position: Vector? = null,
) : Movable
