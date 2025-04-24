package site.geniyz.ots.moving

import site.geniyz.ots.UObject
import site.geniyz.ots.Vector
import site.geniyz.ots.commands.Executable

class ChangePositionCommand(
    val o: UObject,
    val p: Vector,
): Executable {
    override fun execute(){
        o["position"] = p
    }
}
