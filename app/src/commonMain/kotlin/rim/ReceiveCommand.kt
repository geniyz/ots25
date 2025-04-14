package site.geniyz.ots.rim

import kotlinx.serialization.json.Json
import site.geniyz.ots.UObject
import site.geniyz.ots.commands.Executable
import site.geniyz.ots.ioc.IoC

val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    isLenient = true
    allowTrailingComma = true
    allowComments = true
}

class ReceiveCommand(
    private val content: String,
): Executable {
    override fun execute() {
        val data: ReceiveData = json.decodeFromString(content)

        IoC.resolve<Executable>("Переключиться на игру", data.game ).execute() // переключиться на необходимую игру

        var obj = IoC.resolve<UObject>("Игровые объекты", data.obj) // получить необходимый игровой объедк

        data.actions.forEach { act -> // обход полученного перечня действий
            val actionCode = IoC.resolve<String>("Определение действия", data.game, obj, act) // получение зарегистрированного в IoC кода необходимого действия
            IoC.resolve("Очередь команд", // добавление в очередь команд очередного действия
                IoC.resolve<Executable>(actionCode, obj, act.args) // получение реализации необходимого действия
            )
        }
    }
}
