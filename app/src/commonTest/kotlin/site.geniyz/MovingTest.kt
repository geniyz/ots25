package site.geniyz.ots

import site.geniyz.ots.moving.Move
import kotlin.test.Test
import kotlin.test.assertEquals

class `Прямолинейное равномерное движение без деформации` {
    @Test
    fun `Для объекта, находящегося в точке (12, 5) и движущегося со скоростью (-7, 3) движение меняет положение объекта на (5, 8)`() {
        val ss = Spaceship(
            position = Vector(12, 5),
            velocity = Vector(-7, 3),
        )
        Move(ss).execute()

        assertEquals(ss.position?.x, 5.toDouble())
        assertEquals(ss.position?.y, 8.toDouble())
    }

    @Test(expected = Throwable::class)
    fun `Попытка сдвинуть объект, у которого невозможно прочитать положение в пространстве, приводит к ошибке`(){
        Move(Spaceship()).execute()
    }
}
