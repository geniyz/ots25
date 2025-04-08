package site.geniyz.ots

import site.geniyz.ots.adapter.Generator
import site.geniyz.ots.commands.Executable
import site.geniyz.ots.ioc.InitCommand
import site.geniyz.ots.ioc.IoC
import site.geniyz.ots.moving.Movable
import site.geniyz.ots.moving.Move
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class `Тестирование генерации кода` {
    @Test
    fun `Корректность исходника`(){
        val src = Generator.generateClassSourceCode(Movable::class, "Klazz")

        assertContains(src, "override var position: ")  // наличие поля position
        assertContains(src, "override val velocity: ")  // наличие поля velocity

        // нет ни чего лишнего:
        assertEquals(2, src.qty("override va"))    // всего два свойства
        assertEquals(2, src.qty("get()="))         // два геттера
        assertEquals(1, src.qty("set(newValue)"))  // один сеттер
    }

    @Test
    fun `Движение по прямой`(){

        InitCommand().execute()

        IoC.resolve<Executable>("IoC.Register",
            "site.geniyz.ots.moving.Movable:position.get",
            { args: List<Any> ->
                return@resolve (args[0] as UObject)["position"] as Vector
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "site.geniyz.ots.moving.Movable:position.set",
            { args: List<Any> ->
                (args[0] as UObject)["position"] = (args[1] as Vector)
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "site.geniyz.ots.moving.Movable:velocity.get",
            { args: List<Any> ->
                return@resolve (args[0] as UObject)["velocity"] as Vector
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "Adapter",
            { args: List<Any> ->
                return@resolve Generator.generateClassItem(args[0] as KClass<*>, args[1])
            }).execute()

        val ss = Spaceship(
            "position" to Vector(12, 5),
            "velocity" to Vector(-7, 3),
        )


        Move(
            IoC.resolve("Adapter", Movable::class, ss),
        ).execute()


        assertEquals((ss["position"] as Vector).x, 5.toDouble())
        assertEquals((ss["position"] as Vector).y, 8.toDouble())

    }

    @Test
    fun `Методы`(){

        InitCommand().execute()

        IoC.resolve<Executable>("IoC.Register",
            "Adapter",
            { args: List<Any> ->
                return@resolve Generator.generateClassItem(args[0] as KClass<*>, args[1])
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "site.geniyz.ots.ITestInterface:calc",
            { args: List<Any> ->
                val o = (args[0] as UObject)
                return@resolve (o["a"] as Int) + (o["z"] as Int)
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "site.geniyz.ots.ITestInterface:sum",
            { args: List<Any> ->
                return@resolve (args[1] as Int)+(args[2] as Int)
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "site.geniyz.ots.ITestInterface:execute",
            { args: List<Any> ->
                /**/
            }).execute()

        val o = Spaceship("a" to 5, "z" to 2)
        val x = IoC.resolve<ITestInterface>("Adapter", ITestInterface::class, o)

        assertEquals(7, IoC.resolve("site.geniyz.ots.ITestInterface:calc", o))
        assertEquals(7, IoC.resolve("site.geniyz.ots.ITestInterface:sum", o, 2, 5))
        assert(IoC.resolve<Unit>("site.geniyz.ots.ITestInterface:execute", o) is Unit)
    }

}

fun String.qty(substring: String): Int { // кол-во вхождений подстроки в строку
    var cnt = 0
    var i = 0
    while (true) {
        i = indexOf(substring, i)
        if (i<0) break
        cnt++
        i += substring.length
    }
    return cnt
}

interface ITestInterface {
    var a: Int
    val z: Int
    fun calc(): Int
    fun sum(a: Int, b: Int): Int
    fun execute()
}
