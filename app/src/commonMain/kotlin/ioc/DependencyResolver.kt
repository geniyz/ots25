package site.geniyz.ots.ioc

class DependencyResolver(
    val scope: Any
):IDependencyResolver {

    override fun resolve(dependency: String, args: List<Any?>): Any {
        var dependencies = scope as Map<String, (List<Any?>) -> Any?>

        while (true) {
            if (dependencies[dependency] is (List<Any?>) -> Any? ) {
                return dependencies[dependency]!!(args) as Any
            } else {
                dependencies = dependencies["IoC.Scope.Parent"]!!(args) as Map<String, (List<Any?>) -> Any?>
            }
        }
    }
}
