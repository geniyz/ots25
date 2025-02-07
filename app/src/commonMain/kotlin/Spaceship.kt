package site.geniyz.ots

import site.geniyz.ots.moving.Movable

class Spaceship(
    override val velocity: Vector = Vector.NONE,
    override var position: Vector = Vector.NONE,
) : Movable
