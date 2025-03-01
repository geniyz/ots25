package site.geniyz.ots

import kotlinx.coroutines.*
import site.geniyz.ots.commands.Executable
import site.geniyz.ots.ioc.InitCommand
import site.geniyz.ots.ioc.IoC
import kotlin.math.absoluteValue
import kotlin.test.Test
import kotlin.test.assertEquals

class `Тесты использования IoC` {

    @Test
    fun `База`() {
        var rez: Int = 0

        InitCommand().execute()

        IoC.resolve<Executable>("IoC.Register",
            "сложение",
            { args: List<Int> ->
                args.reduce(Int::plus)
            }).execute()

        IoC.resolve<Executable>("IoC.Register",
            "вычитание",
            { args: List<Int> ->
                args.reduce(Int::minus)
            }).execute()


        rez = IoC.resolve("сложение", 1,2,3,4)
        assertEquals(rez, 10)

        rez = IoC.resolve("вычитание", -1,-2,3,4)
        assertEquals(rez, -6)

    }


    @Test
    fun `Скоупы в одном — норм сложение, в другом сложение по модулю`() {
        var rez: Int = 0

        InitCommand().execute()

        val s1 = IoC.resolve<Any>("IoC.Scope.Create")
        val s2 = IoC.resolve<Any>("IoC.Scope.Create", s1)

        IoC.resolve<Executable>("IoC.Scope.Current.Set", s1).execute()
        IoC.resolve<Executable>("IoC.Register",
            "сложение",
            { args: List<Int> ->
                args.reduce(Int::plus)
            }).execute()
        IoC.resolve<Executable>("IoC.Register",
            "вычитание",
            { args: List<Int> ->
                args.reduce(Int::minus)
            }).execute()

        IoC.resolve<Executable>("IoC.Scope.Current.Set", s2).execute()
        IoC.resolve<Executable>("IoC.Register",
            "сложение",
            { args: List<Int> ->
                args.reduce{ a, b -> a.absoluteValue + b.absoluteValue }
            }).execute()


        rez = IoC.resolve("сложение", -1,-2,3,4)
        assertEquals(rez, 10)

        rez = IoC.resolve("вычитание", 7,2)
        assertEquals(rez, 5)

        IoC.resolve<Executable>("IoC.Scope.Current.Set", s1).execute()
        rez = IoC.resolve("сложение", -1,2,-3,4)
        assertEquals(rez, 2)

    }

    @Test
    fun `Многопоточность`(): Unit = runBlocking {
        var rez: Int = 0

        InitCommand().execute()

        val s1 = IoC.resolve<Any>("IoC.Scope.Create")     // один скоп
        val s2 = IoC.resolve<Any>("IoC.Scope.Create", s1) // ещё  скоп

        IoC.resolve<Executable>("IoC.Scope.Current.Set", s1).execute()
        IoC.resolve<Executable>("IoC.Register",
            "сложение",
            { args: List<Int> ->
                args.reduce(Int::plus)
            }).execute()
        IoC.resolve<Executable>("IoC.Register",
            "вычитание",
            { args: List<Int> ->
                args.reduce(Int::minus)
            }).execute()

        IoC.resolve<Executable>("IoC.Scope.Current.Set", s2).execute()
        IoC.resolve<Executable>("IoC.Register",
            "сложение",
            { args: List<Int> ->
                args.reduce{ a, b -> a.absoluteValue + b.absoluteValue }
            }).execute()


        GlobalScope.launch { // по потоку на вызов
            rez = IoC.resolve("сложение", -1, -2, 3, 4)
            assertEquals(rez, 10)
        }

            coroutineScope {
                launch { // по потоку на вызов
                    rez = IoC.resolve("вычитание", 7, 2)
                    assertEquals(rez, 5)
                }

                launch { // по потоку на вызов
                    IoC.resolve<Executable>("IoC.Scope.Current.Set", s1).execute()
                    rez = IoC.resolve("сложение", -1, 2, -3, 4)
                    assertEquals(rez, 2)
                }
            }

    }

}
