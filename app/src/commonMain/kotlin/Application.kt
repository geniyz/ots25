package site.geniyz.ots

import kotlin.math.abs
import kotlin.math.sqrt

class AIsZeroError : Throwable()
class NumberIsNotANumber: Throwable("Число не является числом")

class Application(
    val epsilon: Double = Double.MIN_VALUE,
){
    // init {
    //     println("epsilon = $epsilon")
    // }
    fun solve(a: Double = 0.0, b: Double = 0.0, c: Double = 0.0): List<Double>{
        return buildList {

            listOf(a,b,c).intersect(NotANumbers).isNotEmpty() && throw NumberIsNotANumber()

            (abs(a) < epsilon) && throw AIsZeroError()

            val discriminant = (b*b)-(4*a*c) // D=b²−4ac

            // println("D = $discriminant")

            if( discriminant == abs(discriminant) /* >= 0 */ ){
                if( discriminant > epsilon ){
                    sqrt(discriminant).let{ // x=⁻ᵇ±ᴰ⁄₂a
                        add( (-b+it)/2*a ) // x=⁻ᵇ⁺ᴰ⁄₂a
                        add( (-b-it)/2*a ) // x=⁻ᵇ⁻ᴰ⁄₂a
                    }
                }else{
                    add( -(b/2*a) ) // x=-ᵇ⁄₂a
                }
            }
        }

    }

    companion object {
        val NotANumbers = listOf(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
    }
}
