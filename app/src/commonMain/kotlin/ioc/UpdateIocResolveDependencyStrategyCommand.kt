package site.geniyz.ots.ioc

import site.geniyz.ots.commands.Executable

class UpdateIocResolveDependencyStrategyCommand(
    val updater: (((String, List<Any?>)->Any?)->(String, List<Any?>)->Any?)
):Executable {
    override fun execute() {
        IoC.strategy = updater( IoC.strategy )
    }
}
