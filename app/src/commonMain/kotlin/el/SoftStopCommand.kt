package site.geniyz.ots.el

class SoftStopCommand(
    private val el: EventLoop
): StopCommand(el) {
    override fun execute() {
        val currentTick = el.tick.value
        el.tick.value = {
            if (el.isEmpty) {
                el.stop()
            } else {
                currentTick()
            }
        }
    }
}
