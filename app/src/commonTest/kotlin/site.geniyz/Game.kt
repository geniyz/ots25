package site.geniyz

import site.geniyz.ots.UObject
import site.geniyz.ots.el.EventLoop

class Game(
    val scope: Any,
    val el: EventLoop,
    var objs: MutableList<UObject>,
)