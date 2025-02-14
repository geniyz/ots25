package site.geniyz.ots.commands

class RepeatCommand( // Реализовать Команду, которая повторяет Команду, выбросившую исключение.
    val command: Executable,
    val qty: Int = 1,
): Executable {
    override fun execute() {
        (qty..1).forEach {
            command.execute()
        }
    }
}
