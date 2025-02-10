package site.geniyz.ots

class Spaceship(
    val params: MutableMap<String, Any?> = mutableMapOf(),
) : UObject {

    constructor(vararg params: Pair<String, Any?>) : this(params.toMap().toMutableMap())

    override fun get(k: String): Any? = params[k]
    override fun set(k: String, value: Any?){
        params[k] = value
    }

    override fun toString()= params.map{ "${it.key} → ${it.value}" }.joinToString("; ")
}
