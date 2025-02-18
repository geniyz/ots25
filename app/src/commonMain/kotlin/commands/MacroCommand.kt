package site.geniyz.ots.commands

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
