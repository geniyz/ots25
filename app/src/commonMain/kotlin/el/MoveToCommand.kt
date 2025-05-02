package site.geniyz.ots.el

import site.geniyz.ots.commands.Executable
import java.util.concurrent.BlockingQueue

class MoveToCommand(
    private val el: EventLoop,
    private val dst: BlockingQueue<Executable>,
): SwitchStateCommand(el) {

    override fun execute() {
        el.handle = EventLoop.MoveToEventLoop(el, dst)
    }
}
