package site.geniyz.ots

import site.geniyz.ots.rotating.*
import kotlin.test.Test
import kotlin.test.assertEquals

class `Поворот` {
    @Test
    fun `Поворот с 10° на 3° = 13°`() {
        val ss = Spaceship(
            "position" to Vector(0, 0),
            "velocity" to Vector(0, 0),
            "angularVelocity" to 3,
            "direction" to 10,
        )
        Rotate(RotableAdpater(ss)).execute()

        assertEquals(ss["direction"], 13)
    }

    @Test
    fun `Поворот с 355° на 10° = 5°`() {
        val ss = Spaceship(
            "position" to Vector(0, 0),
            "velocity" to Vector(0, 0),
            "angularVelocity" to 10,
            "direction" to 355,
        )
        Rotate(RotableAdpater(ss)).execute()

        assertEquals(ss["direction"], 5)
    }

}
