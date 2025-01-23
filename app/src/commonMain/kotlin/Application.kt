package site.geniyz.ots

import kotlin.math.abs
import kotlin.math.sqrt

class AIsZeroError : Throwable()

class Application(
    val epsilon: Double = Double.MIN_VALUE,
){
    init {
        println("epsilon = $epsilon")
    }
    fun solve(a: Double = 0.0, b: Double = 0.0, c: Double = 0.0): List<Double>{
        return buildList {
            if(abs(a) < epsilon) throw AIsZeroError()

            val discriminant = (b*b)-(4*a*c) // D=b²−4ac

            println("D = $discriminant")

            if( discriminant == abs(discriminant) /* >= 0 */ ){
                if( discriminant > epsilon ){
                    sqrt(discriminant).let{
                        add( (-b+it)/2*a )
                        add( (-b-it)/2*a )
                    }
                }else{
                    add( -(b/2*a) )
                }
            }
        }

    }
}
