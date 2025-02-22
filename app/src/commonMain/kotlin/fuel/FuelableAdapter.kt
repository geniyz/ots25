package site.geniyz.ots.fuel

import site.geniyz.ots.UObject

class FuelableAdapter(val o: UObject): Fuelable {

    override var fuelLevel: Long
        get()= (o["fuelLevel"] ?: 0) as Long
        set(newFuelLevel){
            o["fuelLevel"] = newFuelLevel
        }

}
