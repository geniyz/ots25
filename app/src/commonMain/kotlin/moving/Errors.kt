package site.geniyz.ots.moving

class BadStartPosition : Throwable("Не корректны координаты, для совершения движения")
class BadVelocity: Throwable("Не корректна скорость движения")
class BadEndPosition : Throwable("Не корректно значение полученных координат перемещения")
