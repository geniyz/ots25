package site.geniyz.ots.el

import site.geniyz.ots.commands.Executable

class AddCommands(
    private val el: EventLoop,
    private val actions: List<Executable>,
): Executable {

    override fun execute() {
        el.add(actions)
    }
}
