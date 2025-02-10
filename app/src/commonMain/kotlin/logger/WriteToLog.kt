package site.geniyz.ots.logger

import site.geniyz.ots.commands.Executable

class WriteToLog( // Реализовать Команду, которая записывает информацию о выброшенном исключении в лог
    val command: Executable,
    val exception: Throwable,
): Executable {
    override fun execute()= println("${command}: ${exception.localizedMessage}")
}
