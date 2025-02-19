package site.geniyz.ots

import site.geniyz.ots.fuel.*
import site.geniyz.ots.moving.ChangeVelocityCommand
import kotlin.test.Test
import kotlin.test.assertEquals

class `Макрокоманды` {

    @Test
    fun `CheckFuelCommand проверяет, что топлива достаточно, если нет, то выбрасывает исключение CommandException → достаточно`() {
        val ss = Spaceship(
            "fuelLevel" to 10L,
        )
        CheckFuelCommand(FuelableAdapter(ss), 1L).execute()
    }

    @Test(expected = CommandException::class)
    fun `CheckFuelCommand проверяет, что топлива достаточно, если нет, то выбрасывает исключение CommandException → пусто`() {
        val ss = Spaceship(
            "fuelLevel" to 0L,
        )
        CheckFuelCommand(FuelableAdapter(ss), 1L).execute()
    }

    @Test(expected = CommandException::class)
    fun `CheckFuelCommand проверяет, что топлива достаточно, если нет, то выбрасывает исключение CommandException → мало`() {
        val ss = Spaceship(
            "fuelLevel" to 23L,
        )
        CheckFuelCommand(FuelableAdapter(ss), 27L).execute()
    }


    @Test
    fun `Топливо сгорает с 10 до 9 за один шаг`(){
        val ss = Spaceship(
            "fuelLevel" to 10L,
        )
        BurnFuelCommand(FuelableAdapter(ss), 1L).execute()
        assertEquals(ss.params["fuelLevel"], 9L)
    }

    @Test
    fun `Топливо сгорает с 7 до 4 за один шаг`(){
        val ss = Spaceship(
            "fuelLevel" to 7L,
        )
        BurnFuelCommand(FuelableAdapter(ss), 3L).execute()
        assertEquals(ss.params["fuelLevel"], 4L)
    }

    @Test(expected = CommandException::class)
    fun `Отрицательный расход топлива`(){
        val ss = Spaceship(
            "fuelLevel" to 7L,
        )
        BurnFuelCommand(FuelableAdapter(ss), -3L).execute()
    }

    @Test
    fun `Движение по прямой с расходом топлива → два шага → ок`(){
        val ss = Spaceship(
            "position"  to Vector(12, 5),
            "velocity"  to Vector(-7, 3),
            "fuelLevel" to 7L,
        )

        val move = MoveFuelableCommand(ss, 3)

        move.execute()
        assertEquals((ss["position"] as Vector).x, 5.toDouble())
        assertEquals((ss["position"] as Vector).y, 8.toDouble())
        assertEquals((ss["fuelLevel"] as Long), 4L)

        move.execute()
        assertEquals((ss["position"] as Vector).x, (-2).toDouble())
        assertEquals((ss["position"] as Vector).y, 11.toDouble())
        assertEquals((ss["fuelLevel"] as Long), 1L)

    }

    @Test
    fun `Движение по прямой с расходом топлива → два шага без расхода → ок`(){
        val ss = Spaceship(
            "position"  to Vector(12, 5),
            "velocity"  to Vector(-7, 3),
            "fuelLevel" to 7L,
        )

        val move = MoveFuelableCommand(ss, 0)

        move.execute()
        assertEquals((ss["position"] as Vector).x, 5.toDouble())
        assertEquals((ss["position"] as Vector).y, 8.toDouble())
        assertEquals((ss["fuelLevel"] as Long), 7L)

        move.execute()
        assertEquals((ss["position"] as Vector).x, (-2).toDouble())
        assertEquals((ss["position"] as Vector).y, 11.toDouble())
        assertEquals((ss["fuelLevel"] as Long), 7L)

    }


    @Test(expected = CommandException::class)
    fun `Движение по прямой с расходом топлива → два шага → ошибка`(){
        val ss = Spaceship(
            "position"  to Vector(12, 5),
            "velocity"  to Vector(-7, 3),
            "fuelLevel" to 7L,
        )
        val move = MoveFuelableCommand(ss, 4L)

        move.execute()
        assertEquals((ss["position"] as Vector).x, 5.toDouble())
        assertEquals((ss["position"] as Vector).y, 8.toDouble())
        assertEquals((ss["fuelLevel"] as Long), 3L)

        move.execute() // тут должно упасть, поскольку топлива на очередной шаг не достаточно
    }

    @Test
    fun `ChangeVelocityCommand меняет скорость`(){
        val ss = Spaceship(
            "position"  to Vector(12, 5),
            "velocity"  to Vector(-7, 3),
            "fuelLevel" to 7L,
        )
        ChangeVelocityCommand(ss, Vector(5,2)).execute()
        assertEquals((ss["velocity"] as Vector).x, 5.0)
        assertEquals((ss["velocity"] as Vector).y, 2.0)
    }

    @Test(expected = CommandException::class)
    fun `ChangeVelocityCommand падает, если не умеет двигаться`(){
        val ss = Spaceship()
        ChangeVelocityCommand(ss, Vector(5,2)).execute()
    }
}
