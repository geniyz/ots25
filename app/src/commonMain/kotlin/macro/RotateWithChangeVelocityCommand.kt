package site.geniyz.ots.macro

import site.geniyz.ots.UObject
import site.geniyz.ots.Vector
import site.geniyz.ots.moving.ChangeVelocityCommand
import site.geniyz.ots.moving.MovableAdapter
import site.geniyz.ots.rotating.RotableAdapter
import site.geniyz.ots.rotating.Rotate
import kotlin.math.cos
import kotlin.math.sin

class RotateWithChangeVelocityCommand( // Реализована команда поворота, которая еще и меняет вектор мгновенной скорости
    o: UObject,
) : MacroCommand(
    ChangeVelocityCommand(o,
        ((/*RotableAdapter(o).direction + */RotableAdapter(o).angularVelocity)%360).toDouble().let { a ->

            val x = MovableAdapter(o).velocity.x
            val y = MovableAdapter(o).velocity.y

            val c = Math.round( 100000000000 * cos((a / 180 * Math.PI)) ) / 100000000000
            val s = Math.round( 100000000000 * sin((a / 180 * Math.PI)) ) / 100000000000

            Vector(
                x = ( x * c ) - ( y * s ),
                y = ( x * s ) + ( y * c ),
            )
        }
    ),
    Rotate(RotableAdapter(o)),
)
