package site.geniyz.ots

import org.junit.Test
import site.geniyz.ots.commands.Executable
import site.geniyz.ots.el.AddCommands
import site.geniyz.ots.el.EventLoop
import site.geniyz.ots.el.HardStopCommand
import site.geniyz.ots.el.SoftStopCommand
import site.geniyz.ots.el.StartCommand
import site.geniyz.ots.ioc.InitCommand
import site.geniyz.ots.ioc.IoC
import java.util.concurrent.CountDownLatch
import kotlin.test.assertEquals

class AccCommand(
    val ss: UObject,
    val i2: Int,
): Executable {
    override fun execute() {
        // println(" ${ss["acc"] as Int} += $i2 ")
        ss["acc"] = (ss["acc"] as Int) + i2
    }
}

class EventLoopTest {

    @Test
    fun `В цикле получает из потокобезопасной очереди команду и запускает её`() {
        var ss = Spaceship("acc" to 0)

        val el = EventLoop( listOf(
            AccCommand(ss, 1),
            AccCommand(ss, 2),
            AccCommand(ss, 3),
        ) )

        el.add(SoftStopCommand(el))

        val f = CountDownLatch(1)
        el.onEnd = {
            f.countDown()
            // println(" acc = ${ss["acc"]} ")
        }
        el.start()
        el.join()
        f.await()
        // println(" ВСЁ ")

        assertEquals(6, ss["acc"] as Int)
    }



    @Test
    fun `Soft Stop добавляется в очередь комманд первой, но цикл выполнения команд завершает свою работу тока в конце`() {
        var ss = Spaceship("acc" to 0)

        val el = EventLoop()
        el.add(SoftStopCommand(el))
        el.add(AccCommand(ss, 1), AccCommand(ss, 2), AccCommand(ss, 3))

        val f = CountDownLatch(1)
        el.onEnd = {
            f.countDown()
            // println(" acc = ${ss["acc"]} ")
        }
        el.start()
        el.join()
        f.await()
        // println(" ВСЁ ")

        assertEquals(6, ss["acc"] as Int)
    }


    @Test
    fun `Hard Stop добавляется в очередь комманд в середину, и цикл выполнения команд завершает свою работу сразу после его вызова`() {
        var ss = Spaceship("acc" to 0)

        val el = EventLoop()
        el.add(AccCommand(ss, 1), AccCommand(ss, 2), AccCommand(ss, 3))
        el.add(HardStopCommand(el))
        el.add(AccCommand(ss, 4), AccCommand(ss, 5), AccCommand(ss, 6))

        val f = CountDownLatch(1)
        el.onEnd = {
            f.countDown()
            // println(" acc = ${ss["acc"]} ")
        }
        el.start()
        el.join()
        f.await()
        // println(" ВСЁ ")

        assertEquals(6, ss["acc"] as Int)
    }

    @Test
    fun `В IoC`() {
        var ss = Spaceship("acc" to 0)
        var el: EventLoop? = null

        InitCommand().execute()

        IoC.resolve<Executable>("IoC.Register", "el", { _: List<Any?>? ->
            if(el == null) el = EventLoop()
            el
        }).execute()
        IoC.resolve<Executable>("IoC.Register", "el.add", { args: List<Executable> ->
            AddCommands(IoC.resolve("el") as EventLoop, args)
        }).execute()
        IoC.resolve<Executable>("IoC.Register", "el.start", { _: List<Any?>? ->
            StartCommand(IoC.resolve("el") as EventLoop)
        }).execute()
        IoC.resolve<Executable>("IoC.Register", "el.stop", { _: List<Any?>? ->
            HardStopCommand(IoC.resolve("el") as EventLoop)
        }).execute()

        IoC.resolve<Executable>("el.add", AccCommand(ss, 1), AccCommand(ss, 2), AccCommand(ss, 3)).execute()
        IoC.resolve<Executable>("el.add", IoC.resolve("el.stop")).execute()
        IoC.resolve<Executable>("el.add", AccCommand(ss, 4), AccCommand(ss, 5), AccCommand(ss, 6)).execute()

        val f = CountDownLatch(1)
        (IoC.resolve("el") as EventLoop).onEnd = {
            f.countDown()
            // println(" acc = ${ss["acc"]} ")
        }

        IoC.resolve<Executable>("el.start").execute()

        f.await()
        // println(" ВСЁ ")

        assertEquals(6, ss["acc"] as Int)
    }

}
