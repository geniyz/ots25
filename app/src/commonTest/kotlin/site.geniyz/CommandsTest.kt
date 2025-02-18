package site.geniyz.ots

import site.geniyz.ots.fuel.BurnFuelCommand
import site.geniyz.ots.fuel.CheckFuelCommand
import site.geniyz.ots.fuel.CommandException
import site.geniyz.ots.fuel.FuelableAdapter
import kotlin.test.Test
import kotlin.test.assertEquals

class `Макрокоманды` {

    @Test
    fun `CheckFuelCommand проверяет, что топлива достаточно, если нет, то выбрасывает исключение CommandException → достаточно`() {
        val ss = Spaceship(
            "fuelLevel" to 10L,
        )
        CheckFuelCommand(FuelableAdapter(ss)).execute()
    }

    @Test(expected = CommandException::class)
    fun `CheckFuelCommand проверяет, что топлива достаточно, если нет, то выбрасывает исключение CommandException → пусто`() {
        val ss = Spaceship(
            "fuelLevel" to 0L,
        )
        CheckFuelCommand(FuelableAdapter(ss)).execute()
    }

    @Test
    fun `Топливо сгорает с 10 до 9 за один шаг`(){
        val ss = Spaceship(
            "fuelLevel" to 10L,
        )
        BurnFuelCommand(FuelableAdapter(ss)).execute()
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
}
