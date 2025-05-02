package site.geniyz.ots.el

class HardStopCommand(
    private val el: EventLoop
): StopCommand(el)