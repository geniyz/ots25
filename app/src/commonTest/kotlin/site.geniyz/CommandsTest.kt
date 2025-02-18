package site.geniyz.ots

import site.geniyz.ots.fuel.CheckFuelCommand
import site.geniyz.ots.fuel.CommandException
import site.geniyz.ots.fuel.FuelableAdapter
import kotlin.test.Test

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

}
