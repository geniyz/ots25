package site.geniyz.ots.fuel

import site.geniyz.ots.UObject
import site.geniyz.ots.commands.MacroCommand
import site.geniyz.ots.moving.MovableAdpater
import site.geniyz.ots.moving.Move

class MoveFuelableCommand( // Реализовать команду движения по прямой с расходом топлива, используя команды с предыдущих шагов
    o: UObject,
    q: Long = 1,
): MacroCommand(
    CheckFuelCommand(FuelableAdapter(o), q), // проверка достаточности топлива
    BurnFuelCommand(FuelableAdapter(o), q),  // сжигание топлива
    Move(MovableAdpater(o)),                 // движение
)
