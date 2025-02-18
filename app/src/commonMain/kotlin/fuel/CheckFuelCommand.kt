package site.geniyz.ots.fuel

import site.geniyz.ots.commands.Executable

class CheckFuelCommand( // Реализована команда CheckFuelCommand - 1балл
    val o: Fuelable,
    val q: Long = 0L,
): Executable {
    override fun execute() {
        if(o.fuelLevel <= q) throw CommandException("Не достаточно топлива") // CheckFuelCommand проверяет, что топлива достаточно, если нет, то выбрасывает исключение CommandException
    }
}
