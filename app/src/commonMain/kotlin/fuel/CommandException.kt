package site.geniyz.ots.fuel

// CheckFuelCommand проверяет, что топлива достаточно, если нет, то выбрасывает исключение CommandException
class CommandException(message : String?=null): Throwable(message ?: "Ошибка")
