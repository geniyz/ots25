package site.geniyz.ots.rim

import kotlinx.serialization.json.Json
import site.geniyz.ots.UObject
import site.geniyz.ots.commands.Executable
import site.geniyz.ots.ioc.IoC

class Receiver(
    private val config: UObject
) {
    init {
        Kafka(config).consume{ topic, key, content ->
            val data: ReceiveData = Json.decodeFromString(content as String)

            IoC.resolve<Executable>("IoC.Scope.Current.Set", data.game ).execute() // переключиться на необходимую игру

            var obj = IoC.resolve<UObject>("Игровые объекты", data.obj) // получить необходимый игровой объедк

            data.actions.forEach { act -> // обход полученного перечня действий

                val actionCode = IoC.resolve<String>("Определение действия", data.game, obj, act) // получение зарегистрированного в IoC кода необходимого действия

                IoC.resolve<Executable>("Очередь команд", // добавление в очередь команд очередного действия
                    IoC.resolve(actionCode, obj, act.args) // получение реализации необходимого действия
                ).execute()
            }

        }
    }
}
