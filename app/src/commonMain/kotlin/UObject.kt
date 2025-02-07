package site.geniyz.ots

interface UObject{
    operator fun get(k: String): Any?
    operator fun set(k: String, value: Any?)
}
