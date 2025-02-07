package site.geniyz.ots

import site.geniyz.ots.moving.*
import kotlin.test.Test
import kotlin.test.assertEquals

class `Прямолинейное равномерное движение без деформации` {
    @Test
    fun `Для объекта, находящегося в точке (12, 5) и движущегося со скоростью (-7, 3) движение меняет положение объекта на (5, 8)`() {
        val ss = Spaceship(
            "position" to Vector(12, 5),
            "velocity" to Vector(-7, 3),
        )
        Move(MovableAdpater(ss)).execute()

        assertEquals((ss["position"] as Vector).x, 5.toDouble())
        assertEquals((ss["position"] as Vector).y, 8.toDouble())
    }

    @Test(expected = BadStartPosition::class)
    fun `Попытка сдвинуть объект, у которого невозможно прочитать положение в пространстве, приводит к ошибке — NaN`(){
        Move(MovableAdpater(Spaceship())).execute()
    }

    @Test(expected = BadStartPosition::class)
    fun `Попытка сдвинуть объект, у которого невозможно прочитать положение в пространстве, приводит к ошибке — ∞`(){
        Move(MovableAdpater(Spaceship("position" to Vector(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)))).execute()
    }

    @Test(expected = BadVelocity::class)
    fun `Попытка сдвинуть объект, у которого невозможно прочитать значение мгновенной скорости, приводит к ошибке — NaN`(){
        Move(MovableAdpater(Spaceship("position" to Vector(0,0)))).execute()
    }
    @Test(expected = BadVelocity::class)
    fun `Попытка сдвинуть объект, у которого невозможно прочитать значение мгновенной скорости, приводит к ошибке — ∞`(){
        Move(MovableAdpater(Spaceship("position" to Vector(0,0), "velocity" to Vector(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)))).execute()
    }

    @Test(expected = BadEndPosition::class)
    fun `Попытка сдвинуть объект, у которого невозможно изменить положение в пространстве, приводит к ошибке`(){
        Move(MovableAdpater(Spaceship("position" to Vector(Double.MAX_VALUE, Double.MIN_VALUE), "velocity" to Vector(Double.MAX_VALUE, Double.MAX_VALUE) ))).execute()
    }
}
