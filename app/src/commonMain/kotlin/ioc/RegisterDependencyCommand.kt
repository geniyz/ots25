package site.geniyz.ots.ioc

import site.geniyz.ots.commands.Executable

class RegisterDependencyCommand(
    private val key: String,
    private val dependencyResolverStratgey: (List<Any?>?)->Any?,
):Executable {
    override fun execute() {
        val currentScope = IoC.resolve<MutableMap<String, (List<Any?>?)->Any?>>("IoC.Scope.Current")
        currentScope[key] = dependencyResolverStratgey
    }
}