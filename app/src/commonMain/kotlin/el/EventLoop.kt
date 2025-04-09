package site.geniyz.ots.el

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
    private var running = true
    private val defaultTick = {
        val command = queue.take()
        try {
            command.execute()
        } catch (e: Throwable) {
            IoC.resolve<Executable>("HandleException", command, e).execute();
        }
    }
    var tick: ()->Unit = defaultTick
    var onEnd: ()->Unit = {}

    // val queueCount: Int
    //     get()= queue.size
    val isEmpty: Boolean
        get()= queue.isEmpty()

    fun start() {
        thread = thread(isDaemon = false) {
            while (running) {
                tick()
            }
            onEnd()
        }
    }

    fun stop() {
        running = false
    }

    fun join()= thread.join()

    fun add(c: List<Executable>)= queue.addAll(c)
    fun add(vararg c: Executable)= queue.addAll(c)

    // fun setTick(action: ()->Unit){ tick = action }

}
