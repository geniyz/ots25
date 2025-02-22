package site.geniyz.ots.macro

import site.geniyz.ots.UObject
import site.geniyz.ots.fuel.BurnFuelCommand
import site.geniyz.ots.fuel.CheckFuelCommand
import site.geniyz.ots.fuel.FuelableAdapter
import site.geniyz.ots.moving.MovableAdapter
import site.geniyz.ots.moving.Move

class MoveFuelableCommand( // Реализовать команду движения по прямой с расходом топлива, используя команды с предыдущих шагов
    o: UObject,
    q: Long = 1,
): MacroCommand(
    CheckFuelCommand(FuelableAdapter(o), q), // проверка достаточности топлива
    BurnFuelCommand(FuelableAdapter(o), q),  // сжигание топлива
    Move(MovableAdapter(o)),                 // движение
)
