package site.geniyz.ots.el

open class StopCommand(
    private val el: EventLoop
): EventLoopSpecialCommand() {
    override fun execute() {
        el.stop()
    }
}
