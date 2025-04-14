package site.geniyz.ots.el

import site.geniyz.ots.commands.Executable

class HardStopCommand(
    private val el: EventLoop
): Executable {
    override fun execute() {
        el.stop()
    }
}
