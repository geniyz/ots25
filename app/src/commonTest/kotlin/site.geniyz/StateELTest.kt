package site.geniyz.ots

import org.junit.Test
import site.geniyz.ots.commands.Executable
import site.geniyz.ots.el.EventLoop
import site.geniyz.ots.el.HardStopCommand
import site.geniyz.ots.el.MoveToCommand
import site.geniyz.ots.el.RunCommand
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingQueue
import kotlin.test.assertEquals

class StateELTest {

    private class AccCommand(
        val ss: UObject,
        val i2: Int,
    ): Executable {
        override fun execute() {
            // println(" ${ss["acc"] as Int} += $i2 ")
            ss["acc"] = (ss["acc"] as Int) + i2
        }
    }

    @Test
    fun `тест, который проверяет, что после команды hard stop, поток завершается в режиме MoveTo`() {
        var ss = Spaceship("acc" to 0)
        val dst = LinkedBlockingQueue<Executable>()

        val el = EventLoop()
        el.add(MoveToCommand(el, dst))
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

        assertEquals(0, ss["acc"] as Int) // ни одна команда не выполнилась
        assertEquals(3, dst.size) // в эту очередь перемещено три команды
    }

    @Test
    fun `тест, который проверяет, что после команды MoveToCommand, поток переходит на обработку Команд с помощью состояния MoveTo`(){
        var ss = Spaceship("acc" to 0)
        val dst = LinkedBlockingQueue<Executable>()

        val el = EventLoop()
        el.add(AccCommand(ss, 1), AccCommand(ss, 2), AccCommand(ss, 3))
        el.add(MoveToCommand(el, dst))
        el.add(AccCommand(ss, 4), AccCommand(ss, 5), AccCommand(ss, 6))
        el.add(HardStopCommand(el))

        val f = CountDownLatch(1)
        el.onEnd = {
            f.countDown()
            // println(" acc = ${ss["acc"]} ")
        }
        el.start()
        el.join()
        f.await()
        // println(" ВСЁ ")

        assertEquals(6, ss["acc"] as Int) // должны выполниться только первые три команды
        assertEquals(3, dst.size) // остальные три должны переместиться в другую очередь
    }

    @Test
    fun ` тест, который проверяет, что после команды RunCommand, поток переходит на обработку Команд с помощью состояния "Обычное"`(){
        var ss = Spaceship("acc" to 0)
        val dst = LinkedBlockingQueue<Executable>()

        val el = EventLoop()
        el.add(MoveToCommand(el, dst))
        el.add(AccCommand(ss, 1), AccCommand(ss, 2), AccCommand(ss, 3))
        el.add(RunCommand(el))
        el.add(AccCommand(ss, 4), AccCommand(ss, 5), AccCommand(ss, 6))
        el.add(HardStopCommand(el))

        val f = CountDownLatch(1)
        el.onEnd = {
            f.countDown()
            // println(" acc = ${ss["acc"]} ")
        }
        el.start()
        el.join()
        f.await()
        // println(" ВСЁ ")

        assertEquals(15, ss["acc"] as Int) // должны выполниться только последние три команды
        assertEquals(3, dst.size) // а первые три должны переместиться в другую очередь
    }
}
