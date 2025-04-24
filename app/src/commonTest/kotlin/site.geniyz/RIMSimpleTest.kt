package site.geniyz.ots

import site.geniyz.Game
import site.geniyz.ots.commands.Executable
import site.geniyz.ots.el.EventLoop
import site.geniyz.ots.el.SoftStopCommand
import site.geniyz.ots.ioc.InitCommand
import site.geniyz.ots.ioc.IoC
import site.geniyz.ots.moving.ChangePositionCommand
import site.geniyz.ots.moving.ChangeVelocityCommand
import site.geniyz.ots.moving.MovableAdapter
import site.geniyz.ots.moving.Move
import site.geniyz.ots.rim.InterpretCommand
import site.geniyz.ots.rim.ReceiveAction
import site.geniyz.ots.rim.ReceiveCommand
import java.util.ArrayList
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class `Тесты работы с сообщениями` {
    @Test
    fun `Тесты интерпретации команд`() {

        InitCommand().execute()

        val s = IoC.resolve<Any>("IoC.Scope.Create")
        IoC.resolve<Executable>("IoC.Scope.Current.Set", s).execute()

        var games = mutableListOf<Game>() // перечень игр
        // IoC.resolve<Map<*,*>>("IoC.Scope.Current.Clear")
        // println(" → 1")

        IoC.resolve<Executable>("IoC.Register",
            "Начать новую игру",
            { args: List<Any?>? ->
                val g = Game(
                    IoC.resolve<Any>("IoC.Scope.Create", s),
                    EventLoop().also{it.start()},
                    mutableListOf(),
                )
                games.add( g )
                g.el.start()
                g
            }).execute()
        // println(" → 2")

        IoC.resolve<Executable>("IoC.Register",
            "Добавить объекты",
            { args: List<UObject> ->
                games.first { it.scope == IoC.resolve("IoC.Scope.Current") }.objs.addAll(args)
            }).execute()
        // println(" → 3")

        IoC.resolve<Executable>("IoC.Register",
            "Переключиться на игру",
            { args: List<Int> ->
                IoC.resolve<Executable>("IoC.Scope.Current.Set", games[args[0]].scope)
            }).execute()
        // println(" → 4")

        IoC.resolve<Executable>("IoC.Register",
            "Игровые объекты",
            { args: List<String> ->
                games.first { it.scope == IoC.resolve("IoC.Scope.Current") }
                    .objs.first { it["id"] == args.first() }
            }).execute()
        // println(" → 5")

        IoC.resolve<Executable>("IoC.Register",
            "Определение действия",
            { args: List<Any?> ->
                InterpretCommand(
                    gameCode = args[0].toString(),
                    gameObj  = args[1] as UObject,
                    actData  = args[2] as ReceiveAction,
                ).execute()
            }).execute()
        //println(" → 6")

        IoC.resolve<Executable>("IoC.Register",
            "Очередь команд",
            { args: List<Executable> ->
                games.first { it.scope == IoC.resolve("IoC.Scope.Current") }.el.add(args)
            }).execute()
        // println(" → 7")

        IoC.resolve<Executable>("IoC.Register",
            "Задать скорость",
            { args: List<Any> ->
                val newVelocity = args[1] as ArrayList<String>
                ChangeVelocityCommand(args[0] as UObject, Vector( newVelocity[0].toDouble(), newVelocity[1].toDouble()))
            }).execute()
        // println(" → 8")

        IoC.resolve<Executable>("IoC.Register",
            "Задать координаты",
            { args: List<Any> ->
                val newPosition = args[1] as ArrayList<String>
                ChangePositionCommand(args[0] as UObject, Vector( newPosition[0].toDouble(), newPosition[1].toDouble()))
            }).execute()
        // println(" → 9")

        IoC.resolve<Executable>("IoC.Register",
            "Шагнуть",
            { args: List<Any> ->
                Move( MovableAdapter(args[0] as UObject) )
            }).execute()
        // println(" → 10")

        val g = IoC.resolve<Game>("Начать новую игру")
        assertContains(games, g)
        // println(" → 11")

        IoC.resolve<Executable>("Переключиться на игру", 0).execute()
        // println(" → 12")

        IoC.resolve<UObject>("Добавить объекты", Spaceship(
            "id" to "Идентификатор объекты, например UUID какой-нибудь",
            "position" to Vector(0, 0),
            "velocity" to Vector(0, 0),
        ))
        // println(" → 13")

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
        // println(" → 14")

        ReceiveCommand("""
            {
                 "game": "0"
                ,"obj": "${g.objs.first()["id"]}"
                ,"actions": [
                    {
                          "id": "Задать скорость"
                         ,"args": ["1", "0"]
                    }
                 ]
            }
        """.trimIndent()).execute()
        // println(" → 15")

        ReceiveCommand("""
            {
                 "game": "0"
                ,"obj": "${g.objs.first()["id"]}"
                ,"actions": [
                    {
                          "id": "Шагнуть"
                         ,"args": []
                    }
                 ]
            }
        """.trimIndent()).execute()
        // println(" → 16")

        g.el.add(SoftStopCommand(g.el))
        g.el.join()
        // println(" → 17")


        assertEquals(
            Vector(2,5).toString(),
            MovableAdapter(g.objs.first()).position.toString(),
        )

        assertEquals(
            Vector(1,0).toString(),
            MovableAdapter(g.objs.first()).velocity.toString(),
        )

    }

}
