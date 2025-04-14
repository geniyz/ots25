package site.geniyz.ots.el

import site.geniyz.ots.commands.Executable

class StartCommand(
    private val el: EventLoop
): Executable {
    override fun execute() {
        el.start()
    }
}
