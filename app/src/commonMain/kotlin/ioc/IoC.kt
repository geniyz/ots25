package site.geniyz.ots.ioc

object IoC { // синглтон ради стабильной работы в многопоточной параллельной среде
// class IoC{
//    companion object {
        var strategy: (String, List<Any?>)->Any? = { key, args ->
            if ("Update Ioc Resolve Dependency Strategy" == key) {
                UpdateIocResolveDependencyStrategyCommand(args[0] as ((String, List<Any?>) -> Any?) -> (String, List<Any?>) -> Any?)
            } else {
                error("Dependency $key is not found")
            }
        }

        // fun <T> resolve(key: String): T                   = strategy(key, emptyList()  ) as T
        fun <T> resolve(key: String, vararg args: Any? = arrayOf()): T = strategy(key, args.asList()) as T

        // fun bind(key: String, obj: (List<Any>)->Unit){
        //
        // }
//    }
}
