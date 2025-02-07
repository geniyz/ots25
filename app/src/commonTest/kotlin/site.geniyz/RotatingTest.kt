package site.geniyz.ots

import site.geniyz.ots.rotating.Rotate
import kotlin.test.Test
import kotlin.test.assertEquals

class `Поворот` {
    @Test
    fun `Поворот с 10° на 3° = 13°`() {
        val ss = Spaceship(
            position = Vector(0, 0),
            velocity = Vector(0, 0),
            angularVelocity = 3,
            direction = 10,
        )
        Rotate(ss).execute()

        assertEquals(ss.direction, 13)
    }

    @Test
    fun `Поворот с 355° на 10° = 5°`() {
        val ss = Spaceship(
            position = Vector(0, 0),
            velocity = Vector(0, 0),
            angularVelocity = 10,
            direction = 355,
        )
        Rotate(ss).execute()

        assertEquals(ss.direction, 5)
    }

}
