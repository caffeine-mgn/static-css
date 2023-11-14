package pw.binom.css

import kotlin.reflect.KProperty

open class CSSDef(val name: String, extends: Array<out CSSDef>, var parent: CSSDef?, val then: Boolean) {
    val fields = LinkedHashMap<String, List<String>>()
    val childs = ArrayList<CSSDef>()
    val extended = HashSet<CSSDef>()

    var position by CssProperty()
    var left by CssProperty()
    var right by CssProperty()
    var top by CssProperty()
    var bottom by CssProperty()
    var display by CssProperty()
    var boxShadow by CssProperty()
    var transition by CssProperty()
    var visibility by CssProperty()
    var animation by CssProperty()
    var margin by CssProperty()
    var marginRight by CssProperty()
    var marginLeft by CssProperty()
    var marginTop by CssProperty()
    var marginBottom by CssProperty()
    var width by CssProperty()
    var height by CssProperty()
    var textDecoration by CssProperty()
    var cursor by CssProperty()
    var transitionDuration by CssProperty()
    var transitionProperty by CssProperty()
    var transform by CssProperty()
    var filter by CssProperty()
    var userSelect by CssProperty("-moz-user-select", "-ms-user-select", "-webkit-user-select")
    var fontFamily by CssProperty()
    var color by CssProperty()
    var inset by CssProperty()
    var fontSize by CssProperty()
    var fontWeight by CssProperty()
    var opacity by CssProperty()
    var verticalAlign by CssProperty()
    var textAlign by CssProperty()
    var backgroundColor by CssProperty()
    var background by CssProperty()
    var backgroundRepeat by CssProperty()
    var backgroundPosition by CssProperty()
    var backgroundImage by CssProperty()
    var backgroundClip by CssProperty()
    var backgroundOrigin by CssProperty()
    var backgroundSize by CssProperty()
    var backgroundAttachment by CssProperty()
    var backgroundPositionX by CssProperty()
    var backgroundPositionY by CssProperty()
    var border by CssProperty()
    var borderTopColor by CssProperty()
    var borderRightColor by CssProperty()
    var borderLeftColor by CssProperty()
    var borderBottomColor by CssProperty()
    var borderTop by CssProperty()
    var borderRight by CssProperty()
    var borderLeft by CssProperty()
    var borderBottom by CssProperty()
    var borderRadius by CssProperty()
    var borderColor by CssProperty()
    var outline by CssProperty()
    var maxHeight by CssProperty()
    var padding by CssProperty()
    var paddingLeft by CssProperty()
    var minWidth by CssProperty()
    var content by CssProperty(wrap = '\'')
    var minHeight by CssProperty()
    var paddingRight by CssProperty()
    var paddingTop by CssProperty()
    var paddingBottom by CssProperty()
    var transitionTimingFunction by CssProperty()
    var overflow by CssProperty()
    var overflowX by CssProperty()
    var overflowY by CssProperty()
    var whiteSpace by CssProperty()
    var zIndex by CssProperty()
    var fill by CssProperty()
    var stroke by CssProperty()
    var strokeWidth by CssProperty()
    var flexDirection by CssProperty()
    var justifyContent by CssProperty()
    var alignSelf by CssProperty()
    var flexBasis by CssProperty()
    var alignItems by CssProperty()
    var flexGrow by CssProperty()
    var flexShrink by CssProperty()
    var flex by CssProperty()
    var boxSizing by CssProperty()
    var animationName by CssProperty("-moz-animation-name", "-webkit-animation-name")
    var animationDuration by CssProperty("-moz-animation-duration", "-webkit-animation-duration")
    var animationIterationCount by CssProperty("-moz-animation-iteration-count", "-webkit-animation-iteration-count")
    var animationDirection by CssProperty("-moz-animation-direction", "-webkit-animation-direction")

    fun field(key: String, value: String) {
        fields[key] = listOf(value)
    }

    fun field(key: String, vararg values: String) {
        fields[key] = values.toList()
    }

    init {
        extends.forEach {
            it.extended += this
        }
    }

    operator fun String.compareTo(f: CSSDef.() -> Unit): Int {
        style(this, extends = emptyArray(), true, f)
        return 0
    }

    operator fun String.invoke(vararg extends: CSSDef, f: CSSDef.() -> Unit): Int {
        style(this, extends = extends, then = false, f)
        return 0
    }

    fun extend(other: CSSDef) {
        other.extended += this
    }

    internal open fun buildSelfPath(): String {
        if (parent == null) {
            return (listOf(name) + extended.map { it.name }).joinToString(", ")
        }

        val sb = StringBuilder(parent!!.buildSelfPath())
        if (!then) {
            sb.append(" ")
        }
        sb.append(name)
        extended.forEach {
            sb.append(" ").append(it.name)
        }
        return sb.toString()
    }

    internal open fun buildSelf(sb: Appendable, keyframes: Boolean) {
        if (fields.isEmpty()) {
            return
        }
        if (keyframes) {
            sb.append("@keyframes ")
        }
        sb.append(buildSelfPath()).append("{")
        var first = true
        fields.forEach { (key, values) ->
            values.forEach { value ->
                if (!first) {
                    sb.append(";")
                }
                first = false
                sb.append("${convertKey(key)}:$value")
            }
        }
        sb.append("}")
    }

    internal fun convertKey(key: String): String {
        val out = StringBuilder()
        for (i in 0..key.length - 1) {
            val c = key[i]
            val l = c.lowercaseChar()
            if (c == l) {
                out.append(c)
            } else {
                out.append("-").append(l)
            }
        }
        return out.toString()
    }

    internal open fun buildRecursive(sb: Appendable, keyframes: Boolean) {
        buildSelf(sb, keyframes)
        if (childs.isNotEmpty()) {
            childs.forEach {
                it.buildRecursive(sb, keyframes)
            }
        }
    }
}

private val current = ThreadLocal<CSSDef>()

inline fun <T, R> ThreadLocal<T>.swap(value: T, f: () -> R): R {
    val oldValue = get()
    set(value)
    try {
        return f()
    } finally {
        set(oldValue)
    }
}

fun style(name: String, extends: Array<out CSSDef>, then: Boolean = false, f: CSSDef.() -> Unit): CSSDef {
    val def = CSSDef(name = name, extends = extends, parent = current.get(), then = then)
    current.get()?.childs?.let { it.add(def) }
    current.swap(def) {
        def.f()
    }

    return def
}

class CssProperty(vararg val also: String, val wrap: Char? = null) {
    operator fun getValue(thisRef: CSSDef, property: KProperty<*>): String? {
        val value = thisRef.fields[property.name]?.firstOrNull() ?: return null
        return if (wrap != null) {
            val sb = StringBuilder()
            var i = 1
            while (i < value.length - 2) {
                val c = value[i]
                if (c == '\\') {
                    i++
                    continue
                }
                sb.append(c)
            }
            sb.toString()
        } else {
            value
        }
    }

    operator fun setValue(thisRef: CSSDef, property: KProperty<*>, value: String?) {
        if (value == null) {
            thisRef.fields.remove(property.name)
            also.forEach {
                thisRef.fields.remove(it)
            }
        } else {
            val resultValue = if (wrap != null) {
                val sb = StringBuffer()
                sb.append(wrap)
                value.forEach {
                    if (it == wrap) {
                        sb.append('\\')
                    }
                    sb.append(it)
                }
                sb.append(wrap)
                sb.toString()
            } else {
                value
            }
            thisRef.fields[property.name] = listOf(resultValue)
            also.forEach {
                thisRef.fields[it] = listOf(resultValue)
            }
        }
    }
}
