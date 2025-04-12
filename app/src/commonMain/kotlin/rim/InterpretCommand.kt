package site.geniyz.ots.rim

import site.geniyz.ots.UObject
import site.geniyz.ots.commands.Executable

class InterpretCommand(
    private val gameCode: String,
    private val gameObj:  UObject,
    private val actData:  ReceiveAction,
): Executable {

    override fun execute():String {
        // тут какая-то логика
        // проверка возможности выполнения действия
        // вычисление коллизий
        // прав
        // ивсётакое
        return actData.id
    }
}
