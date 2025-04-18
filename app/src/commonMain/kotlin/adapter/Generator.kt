package site.geniyz.ots.adapter

import site.geniyz.ots.UObject
import site.geniyz.ots.ioc.IoC
import javax.script.ScriptEngineManager
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.*
import kotlin.reflect.javaType

class Generator{
    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun generateClassSourceCode(iface: KClass<out Any>, className: String): String = """
                    package ${className.substringBeforeLast(".")}
                    class ${className.substringAfterLast(".")}(
                        val obj: ${UObject::class.qualifiedName}
                    ) : ${iface.qualifiedName} {
                        ${
                            iface.memberProperties.joinToString("\n") { m ->
                                val lr = if(m is KMutableProperty<*>){"var"}else{"val"}
                                """
                                    override $lr ${m.name}: ${m.returnType}
                                        get()= ${IoC::class.qualifiedName}.resolve("${iface.qualifiedName}:${m.name}.get", obj)
                                        ${ if(lr == "val"){ "" }else{"""
                                        set(newValue){ ${IoC::class.qualifiedName}.resolve<Unit>("${iface.qualifiedName}:${m.name}.set", obj, newValue)}
                                        """.trimEnd()
                                        }}
                                """.trimEnd()
                            }
                        }
                        ${
                            iface
                                .declaredFunctions
                                .joinToString("\n") { f -> 
                                """
                                    override fun ${f.name}(${
                                        f.parameters
                                            .filter { p -> p.name != null }
                                            .joinToString(", ") { p -> 
                                                """ ${ if(p.isVararg){" vararg "}else{""} } ${p.name}: ${p.type}"""
                                            }
                                        }): ${f.returnType} {
                                        return ${IoC::class.qualifiedName}.resolve("${iface.qualifiedName}:${f.name}", obj,
                                            ${
                                                f.parameters
                                                    .filter { p -> p.name != null }
                                                    .joinToString(", "){ p -> """${p.name}""" }
                                            }
                                            )
                                    }
                                """.trimEnd()
                            }
                        }
                    }
                """.trimEnd()

        fun <T : Any> generateClass(iface: KClass<out T>): KClass<T> {
            val className = "${iface.qualifiedName!!.substringBeforeLast(".")}.AutoGenerated${iface.simpleName}Adapter"
            val src = generateClassSourceCode(iface, className)
            // println(src)
            val scriptEngine = ScriptEngineManager().getEngineByExtension("kts")
            scriptEngine.eval(src)
            return scriptEngine.eval("${className.substringAfterLast(".")}::class") as KClass<T>
        }

        fun <T : Any> generateClassItem(iface: KClass<out T>, vararg args: Any = arrayOf() ): T {
            return generateClass(iface).primaryConstructor!!.call(*args)
        }
    }
}
