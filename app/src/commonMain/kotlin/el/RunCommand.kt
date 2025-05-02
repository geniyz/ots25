package site.geniyz.ots.el

class RunCommand(
    private val el: EventLoop,
): SwitchStateCommand(el) {

    override fun execute() {
        el.handle = EventLoop.RunEventLoop(el)
    }
}