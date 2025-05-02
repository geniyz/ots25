package site.geniyz.ots.el

import site.geniyz.ots.commands.Executable

// это «маркер» особых команд, которые надо выполнять независимо от состояния очереди
abstract class EventLoopSpecialCommand: Executable
