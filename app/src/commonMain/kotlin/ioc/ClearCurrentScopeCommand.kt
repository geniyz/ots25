package site.geniyz.ots.ioc

import site.geniyz.ots.commands.Executable

class ClearCurrentScopeCommand:Executable {
    override fun execute() {
        InitCommand.currentScopes = null // .set(null) // = ThreadLocal<Any?>()
    }
}