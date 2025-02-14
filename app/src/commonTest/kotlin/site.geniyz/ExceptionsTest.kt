package site.geniyz.ots

import site.geniyz.ots.commands.Executable
import site.geniyz.ots.commands.RepeatCommand
import site.geniyz.ots.logger.WriteToLog
import site.geniyz.ots.moving.*
import kotlin.test.Test
import kotlin.test.assertEquals

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

        // ПРОШУ ПОДСКАЗАТЬ, КАК ЭТО ДЕЛАТЬ ПРАВИЛЬНО
        // Я ДАЛЁК ОТ РЕФЛЕКСИИ
        // ПОТОМУ ТУТ ИЗВРАЩАЮСЬ КАК МОГУ СО СТРОКАМИ
        ExceptionHandler.register(Move::class.toString(), BadVelocity::class.toString()) { c, e -> // если при попытке движения падает на БэдВелосити,
            commands.addFirst( RepeatCommand(c) )                                                  // то попробовать ещё раз
            println("опп! → повтор")
        }

        while(true){
            val command = commands.removeFirstOrNull()
            if(command == null){
                return;
            }else{
                try {                          // Обернуть вызов Команды в блок try-catch.
                    command.execute()
                }catch (e: Throwable){         // Обработчик catch должен перехватывать только самое базовое исключение.
                    ExceptionHandler.handle( command, e ) { // дефолтный обработчик: (ставит в очередь команду, которая пишет в лог)
                        commands.addFirst(         // Реализовать обработчик исключения, который ставит Команду, пишущую в лог в очередь Команд.
                            WriteToLog(command,e)  // Реализовать Команду, которая записывает информацию о выброшенном исключении в лог.
                        )
                    }
                }
                println(ssOk)
            }
        }

    }

    @Test
    fun `Тестирование повторителя`() {
        val ssBroke = Spaceship(
            "position" to Vector(0, 0),
            "velocity" to Vector(Double.NaN, Double.NaN),
        )
        val commands = ArrayDeque<Executable>()
        commands.add(Move(MovableAdpater(ssBroke))) // в очередь команд добавляется «плохая»

        // регистрация повторителя:
        ExceptionHandler.register(Move::class.toString(), BadVelocity::class.toString()) { c, e -> commands.addFirst( RepeatCommand(c) ) }

        val command = commands.removeFirst()      // считывается первая (она же плохая, она же единственная) команда
        try {                                     // Обернуть вызов Команды в блок try-catch.
            command.execute()
        }catch (e: Throwable){                    // Обработчик catch должен перехватывать только самое базовое исключение.
            ExceptionHandler.handle( command, e ) // тут должен произойти вызов повторителя
        }

        assertEquals( "site.geniyz.ots.commands.RepeatCommand", commands.removeFirst().toString().substringBefore("@")) // убедиться, что в очереди команд появился повторитель

    }


    @Test
    fun `при первом выбросе исключения повторить команду, при повторном выбросе исключения записать информацию в лог`() {
        val ssBroke = Spaceship(
            "position" to Vector(0, 0),
            "velocity" to Vector(Double.NaN, Double.NaN),
        )
        val commands = ArrayDeque<Executable>()
        commands.add(Move(MovableAdpater(ssBroke))) // в очередь команд добавляется «плохая»

        // регистрация повторителя:
        ExceptionHandler.register(Move::class.toString(), BadVelocity::class.toString()) { c, e -> commands.addFirst( RepeatCommand(c) ) }

        // регистрация записи в лог, при ошибке в повторителе
        ExceptionHandler.register(RepeatCommand::class.toString(), BadVelocity::class.toString()) { c, e -> commands.addFirst( WriteToLog(c, e) ) }

        commands.doStep() // 1) — первый шаг. Должна произойти ошибка движения
        // должен стать в очередь повторитель:
        assertEquals("site.geniyz.ots.commands.RepeatCommand", commands.first().toString().substringBefore("@") ) // убедиться, что в очереди команд появился повторитель

        commands.doStep() // 2) — второй шаг. Должен вызваться повторитель, повторяющий вызов ошибочной команды
        // в очередь должен встать логгер
        assertEquals( "site.geniyz.ots.logger.WriteToLog", commands.first().toString().substringBefore("@")) // убедиться, что в очереди команд появился логгер

    }
}

fun ArrayDeque<Executable>.doStep(): Executable {
    val command = removeFirst()               // считывается первая (она же плохая, она же единственная) команда
    try {                                     // Обернуть вызов Команды в блок try-catch.
        command.execute()
    }catch (e: Throwable){                    // Обработчик catch должен перехватывать только самое базовое исключение.
        ExceptionHandler.handle( command, e ) // тут должен произойти вызов повторителя
    }
    return command
}
