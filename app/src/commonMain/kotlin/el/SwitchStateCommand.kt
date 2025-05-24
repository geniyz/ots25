package site.geniyz.ots.el

abstract class SwitchStateCommand(
    private val el: EventLoop,
): EventLoopSpecialCommand()