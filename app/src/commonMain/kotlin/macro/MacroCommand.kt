package site.geniyz.ots.macro

import site.geniyz.ots.commands.Executable

open class MacroCommand(
    val commands: List<Executable>
): Executable {
    constructor(vararg commands: Executable): this(commands.toList())

    override fun execute() {
        commands.forEach {
            it.execute()
        }
    }
}
