package site.geniyz.ots.fuel

import site.geniyz.ots.commands.Executable

class CheckFuelCommand( // Реализована команда CheckFuelCommand - 1балл
    val o: Fuelable
): Executable {
    override fun execute() {
        if(o.fuelLevel <= 0) throw CommandException("Нет топлива") // CheckFuelCommand проверяет, что топлива достаточно, если нет, то выбрасывает исключение CommandException
    }
}
