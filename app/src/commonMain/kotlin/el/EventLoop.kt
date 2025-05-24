package site.geniyz.ots.el

import kotlinx.atomicfu.atomic
import site.geniyz.ots.commands.Executable
import site.geniyz.ots.ioc.IoC
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.collections.addAll
import kotlin.concurrent.thread

class EventLoop(
    private val queue: BlockingQueue<Executable>
) {
    constructor(l: Collection<Executable>): this( LinkedBlockingQueue(l) )
    constructor(): this( emptyList() )

    var handle: EventLooped = RunEventLoop(this)
    private val defaultTick: ()->Unit = {
        val command = queue.first()
        if(command is EventLoopSpecialCommand){ // это «маркер» особых команд, которые надо выполнять независимо от состояния очереди
            try {
                command.execute()
                take()
            } catch (e: Throwable) {
                println("error when command : $command : $e")
                IoC.resolve<Executable>("HandleException", command, e).execute()
            }
        }else{
            handle.defaultTick()
        }
    }

    private lateinit var thread: Thread
    private val running = atomic(true)

    var tick = atomic(defaultTick)
    var onEnd: ()->Unit = {}

    val isEmpty: Boolean
        get()= queue.isEmpty()

    fun start() {
        thread = thread(isDaemon = false) {
            while( running.value ) tick.value()
            onEnd()
        }
    }

    fun stop() {
        running.value = false
        add(object : Executable{ override fun execute(){} })
        // thread.interrupt()
    }

    fun join()= thread.join()

    fun add(c: List<Executable>)= queue.addAll(c)
    fun add(vararg c: Executable)= queue.addAll(c)

    fun take() = queue.take()


    interface EventLooped{
        val defaultTick: ()->Unit
    }

    class RunEventLoop(
        val el: EventLoop,
    ): EventLooped{
        override val defaultTick: ()->Unit = {
            val command = el.take()
            try {
                command.execute()
            } catch (e: Throwable) {
                println("error when command : $command : $e")
                IoC.resolve<Executable>("HandleException", command, e).execute()
            }
        }
    }

    class MoveToEventLoop(
        val el: EventLoop,
        val dst: BlockingQueue<Executable>,
    ): EventLooped{
        override val defaultTick: ()->Unit = {
            val command = el.take()
            dst.add(command)
        }
    }

}
