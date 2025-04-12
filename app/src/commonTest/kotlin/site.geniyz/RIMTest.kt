package site.geniyz.ots

import site.geniyz.ots.commands.Executable
import site.geniyz.ots.el.EventLoop
import site.geniyz.ots.el.SoftStopCommand
import site.geniyz.ots.ioc.InitCommand
import site.geniyz.ots.ioc.IoC
import site.geniyz.ots.moving.ChangeVelocityCommand
import site.geniyz.ots.moving.MovableAdapter
import site.geniyz.ots.moving.Move
import site.geniyz.ots.rim.InterpretCommand
import site.geniyz.ots.rim.ReceiveAction
import site.geniyz.ots.rim.ReceiveCommand
import java.util.ArrayList
import kotlin.test.Test
import kotlin.test.assertEquals

class Game(val scope: Any, val el: EventLoop, var objs: MutableList<UObject>)

class `Тесты «работы с сообщениями»` {

    @Test
    fun `Тесты интерпретации комманд`() {

        InitCommand().execute()

        var games = mutableListOf<Game>() // перечень игр
        // var objs = mutableListOf<UObject>() // игровые объекты

        IoC.resolve<Executable>("IoC.Register",
            "Начать новую игру",
            { args: List<Any?>? ->
                val g = Game(
                    IoC.resolve<Any>("IoC.Scope.Create"),
                    EventLoop().also{it.start()},
                    mutableListOf(),
                )
                games.add( g )
                g.el.start()
                g
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "Добавить объекты",
            { args: List<UObject> ->
                println("Добавить объекты: $args")
                games.first { it.scope == IoC.resolve("IoC.Scope.Current") }.objs.addAll(args)
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "Переключиться на игру",
            { args: List<Int> ->
                println("Переключиться на игру: $args")
                IoC.resolve<Executable>("IoC.Scope.Current.Set", games[args[0]].scope)
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "Игровые объекты",
            { args: List<String> ->
                games.first { it.scope == IoC.resolve("IoC.Scope.Current") }.objs.first { it["id"] == args.first() }
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "Определение действия",
            { args: List<Any?> ->
                println("Определение действия: $args")
                InterpretCommand(
                    gameCode = args[0].toString(),
                    gameObj  = args[1] as UObject,
                    actData  = args[2] as ReceiveAction,
                ).execute()
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "Очередь команд",
            { args: List<Executable> ->
                println("Очередь команд: $args")
                games.first { it.scope == IoC.resolve("IoC.Scope.Current") }.el.add(args)
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "Задать скорость",
            { args: List<Any> ->
                println("Задать скорость: $args")
                ChangeVelocityCommand(args[0] as UObject, Vector(args[1] as Double, args[2] as Double))
            })

        IoC.resolve<Executable>("IoC.Register",
            "Задать координаты",
            { args: List<Any> ->
                println("Задать координаты: $args")
                val newVelocity = args[1] as ArrayList<String>
                MovableAdapter(args[0] as UObject).position = Vector( newVelocity[0].toDouble(), newVelocity[1].toDouble())
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "Шагнуть",
            { args: List<Any> ->
                println("Шагнуть: $args")
                Move( MovableAdapter(args[0] as UObject) )
            }).execute()


        assertEquals(0, games.size)
        val g = IoC.resolve<Game>("Начать новую игру")
        assertEquals(1, games.size)

        IoC.resolve<Executable>("Переключиться на игру", 0).execute()

        IoC.resolve<UObject>("Добавить объекты", Spaceship(
            "id" to "Идентификатор объекты, например UUID какой-нибудь",
            "position" to Vector(0, 0),
            "velocity" to Vector(0, 0),
        ))

        assertEquals(1, g.objs.size)

        ReceiveCommand("""
            {
                 "game": "0"
                ,"obj": "${g.objs.first()["id"]}"
                ,"actions": [
                    {
                          "id": "Задать координаты"
                         ,"args": ["1", "5"]
                    }
                 ]
            }
        """.trimIndent()).execute()

        g.el.add(SoftStopCommand(g.el))
        g.el.join()

        assertEquals(MovableAdapter(g.objs.first()).position.toString(), Vector(1,5).toString())

    }

}
