package site.geniyz.ots

import org.junit.Assert.assertThrows
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class `Тестирование корректности решения квадратного уровнения` {
    val epsilon: Double = 100000*Application.Epsilon

    val app = Application(epsilon)

    @Test
    fun `N3 для уравнения x^2+1 = 0 корней нет`() {
        assertEquals( 0, app.solve(1.0, 0.0, 1.0).size )
    }

    @Test
    fun `N5 для уравнения x^2-1 = 0 есть два корня кратности 1 (x1=1, x2=-1)`() {
        val xs = app.solve(1.0, 0.0, -1.0)
        assertEquals( 2, xs.size )
        setOf(1.0,-1.0).forEach { r ->
            // assertContains(xs, r)
            assertEquals(1, xs.filter { x -> abs(x-r) < epsilon }.size )
        }
    }

    @Test
    fun `N7 для уравнения x^2+2x+1 = 0 есть один корень кратности 2 (x1= x2 = -1)`() {
        val xs = app.solve(1.0, 2.0, 1.0)
        assertEquals( 1, xs.size)
        assert( abs(-1.0-xs.first()) < epsilon )
    }

    @Test(expected = AIsZeroError::class)
    fun `N9 коэффициент a не может быть равен 0`() {
        app.solve(0.0)
    }

    @Test
    fun `N11 дискриминант отличный от нуля, но меньше заданного эпсилон`() {
        val epsilon = .00001
        val app = Application(epsilon)

        val xs = app.solve(1.0, 1.0, 0.249999999)

        assertEquals( 1, xs.size)
        assert( abs(-.5-xs.first()) < epsilon )
    }

    @Test
    fun `N13 обработка не настоящих чисел`() {
        Application.NotANumbers.forEach {
            assertThrows(NumberIsNotANumber::class.java) { app.solve(a = it) }
            assertThrows(NumberIsNotANumber::class.java) { app.solve(b = it) }
            assertThrows(NumberIsNotANumber::class.java) { app.solve(c = it) }
        }
    }
}
