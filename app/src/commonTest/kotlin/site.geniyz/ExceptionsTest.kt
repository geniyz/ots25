package site.geniyz.ots

import site.geniyz.ots.commands.Executable
import site.geniyz.ots.logger.WriteToLog
import site.geniyz.ots.moving.*
import kotlin.test.Test

class `SOLID и исключения` {
    @Test
    fun `hw3`() {

        val ssOk = Spaceship(
            "position" to Vector(0, 0),
            "velocity" to Vector(1, 0),
        )

        val ssBroke = Spaceship(
            "position" to Vector(0, 0),
            "velocity" to Vector(Double.NaN, Double.NaN),
        )

        val commands = ArrayDeque<Executable>()

        commands.add(Move(MovableAdpater(ssOk)))
        commands.add(Move(MovableAdpater(ssOk)))
        commands.add(Move(MovableAdpater(ssBroke)))
        commands.add(Move(MovableAdpater(ssOk)))
        commands.add(Move(MovableAdpater(ssOk)))

        while(true){
            val command = commands.removeFirstOrNull()
            if(command == null){
                return;
            }else{
                try {                          // Обернуть вызов Команды в блок try-catch.
                    command.execute()
                }catch (e: Throwable){         // Обработчик catch должен перехватывать только самое базовое исключение.
                    commands.addFirst(         // Реализовать обработчик исключения, который ставит Команду, пишущую в лог в очередь Команд.
                        WriteToLog(command, e) // Реализовать Команду, которая записывает информацию о выброшенном исключении в лог.
                    )
                }
                println(ssOk)
            }
        }

    }
}
