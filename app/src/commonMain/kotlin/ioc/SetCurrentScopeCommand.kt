package site.geniyz.ots.ioc

import site.geniyz.ots.commands.Executable

class SetCurrentScopeCommand(
    private val scope: Any
): Executable {
    override fun execute() {
        InitCommand.currentScopes = scope //.set(scope)
    }
}
