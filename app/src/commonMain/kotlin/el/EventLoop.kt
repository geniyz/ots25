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

    private lateinit var thread: Thread
    private val running = atomic(true)
    private val defaultTick: ()->Unit = {
        // println(" in defaultTick $queue ")
        val command = queue.take()
        try {
            command.execute()
        } catch (e: Throwable) {
            println("error when command : $command : $e")
            IoC.resolve<Executable>("HandleException", command, e).execute()
        }
    }
    var tick = atomic(defaultTick)
    var onEnd: ()->Unit = {}

    val size: Int
        get()= queue.size

    val isEmpty: Boolean
        get()= queue.isEmpty()
    val isNotEmpty: Boolean = !isEmpty

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

}
