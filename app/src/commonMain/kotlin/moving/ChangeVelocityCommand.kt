package site.geniyz.ots.moving

import site.geniyz.ots.UObject
import site.geniyz.ots.Vector
import site.geniyz.ots.commands.Executable
import site.geniyz.ots.fuel.CommandException

class ChangeVelocityCommand( // Реализовать команду для модификации вектора мгновенной скорости при повороте. Необходимо учесть, что не каждый разворачивающийся объект движется
    val o: UObject,
    val v: Vector,
): Executable {
    override fun execute(){
        try {
            o["velocity"] as Vector // просто, чтоб упасть, если у объекта нет `velocity`. Нет velocity — значит объект не движется (не умеет двигаться)
        }catch (t: Throwable){
            throw CommandException("Не удалось считать вектор перемещения")
        }
        o["velocity"] = v
    }
}
