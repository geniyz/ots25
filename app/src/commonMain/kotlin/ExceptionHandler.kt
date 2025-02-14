package site.geniyz.ots

import site.geniyz.ots.commands.Executable

object ExceptionHandler {

    // ПРОШУ ПОДСКАЗАТЬ, КАК ЭТО ДЕЛАТЬ ПРАВИЛЬНО
    // Я ДАЛЁК ОТ РЕФЛЕКСИИ
    // ПОТОМУ ТУТ ИЗВРАЩАЮСЬ КАК МОГУ СО СТРОКАМИ

    val store: MutableMap<String, MutableMap<String, (Executable, Throwable)->Unit>> = mutableMapOf()

    fun handle(
        command: Executable, // Команда
        exception: Throwable, // Ошибка
        defaultHandler: (()->Unit)? = null, // дефолтный обработчик
    ) {
        val cmdType = command.toString().substringBefore("@")
        val excType = exception.toString().substringBefore(":")

        val handler = store[cmdType]?.get(excType)
        if(handler != null){
            handler(command, exception)
        } else {
            defaultHandler?.invoke()
        }
    }

    fun register(cmdType: String, excType: String, handler: (Executable, Throwable)->Unit ){
        val ct = cmdType.substringAfter(" ").substringBefore(" ")
        val et = excType.substringAfter(" ").substringBefore(" ")
        if(store[ct] == null) store[ct] = mutableMapOf()
        store[ct]!![et] = handler
    }
}
