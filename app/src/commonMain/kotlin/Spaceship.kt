package site.geniyz.ots

import site.geniyz.ots.moving.Movable

class Spaceship(
    override val velocity: Vector,
    override var position: Vector?,
) : Movable
