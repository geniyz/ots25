package site.geniyz.ots.ioc

import kotlinx.coroutines.runBlocking
import site.geniyz.ots.commands.Executable

class InitCommand: Executable {

    override fun execute() {
        if (alreadyExecutesSuccessfully) return

        runBlocking{
            rootScope.putIfAbsent("IoC.Scope.Current.Set") {
                return@putIfAbsent SetCurrentScopeCommand(it!!.first()!!)
            }

            rootScope.putIfAbsent("IoC.Scope.Current.Clear") {
                return@putIfAbsent ClearCurrentScopeCommand()
            }

            rootScope.putIfAbsent("IoC.Scope.Current") {
                return@putIfAbsent currentScopes.get() ?: rootScope
            }

            rootScope.putIfAbsent("IoC.Scope.Parent") {
                return@putIfAbsent error("The root scope has no a parent scope.")
            }

            rootScope.putIfAbsent("IoC.Scope.Create.Empty") {
                return@putIfAbsent mutableMapOf<String, (List<Any?>?) -> Any?>()
            }

            rootScope.putIfAbsent("IoC.Scope.Create") { args ->
                val prnt = if (args!!.isEmpty()) {
                    IoC.resolve("IoC.Scope.Current")
                } else {
                    args[0]
                }

                val creatingScope: MutableMap<String, (List<Any?>?)->Any?> = IoC.resolve("IoC.Scope.Create.Empty")
                creatingScope["IoC.Scope.Parent"] = { _ -> prnt }
                creatingScope
            }

            rootScope.putIfAbsent("IoC.Register") {
                RegisterDependencyCommand(
                    it!![0].toString(),
                    it!![1] as (List<Any?>?)->Any?,
                )
            }

            IoC.resolve<Executable>("Update Ioc Resolve Dependency Strategy",
                { oldStrategy: Any ->
                    { key: String, args: List<Any?> ->
                        DependencyResolver( currentScopes.get() ?: rootScope )
                            .resolve(key, args)
                    }
                }).execute()


            alreadyExecutesSuccessfully = true
        }
    }

    companion object{
        var currentScopes: ThreadLocal<Any?> = ThreadLocal<Any?>()
        private var rootScope: MutableMap<String, (List<Any?>?)->Any?> = mutableMapOf()
        private var alreadyExecutesSuccessfully = false

    }
}