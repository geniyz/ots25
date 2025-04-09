package site.geniyz.ots.el

import site.geniyz.ots.commands.Executable

class SoftStopCommand(
    private val el: EventLoop
): Executable {
    override fun execute() {
        val currentTick = el.tick
        el.tick = {
            if (el.isEmpty) {
                el.stop()
            } else {
                currentTick()
            }
        }
    }
}
