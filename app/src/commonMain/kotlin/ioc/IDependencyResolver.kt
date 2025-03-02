package site.geniyz.ots.ioc

interface IDependencyResolver {
    fun resolve(dependency: String, args: List<Any?>): Any?
}
