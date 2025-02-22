package site.geniyz.ots.fuel

import site.geniyz.ots.commands.Executable

class BurnFuelCommand( // Реализована команда BurnFuelCommand - 1балл
    val o: Fuelable, // заправляемый объект
    val q: Long = 1, // расход топлива за один шаг
): Executable {
    override fun execute() {
        if(q != 0L) {
            if (q < 0) throw CommandException("Расход топлива не может быть отрицательным")
            o.fuelLevel -= q
        }
    }
}
