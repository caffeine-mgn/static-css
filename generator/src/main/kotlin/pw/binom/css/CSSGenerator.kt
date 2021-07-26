package pw.binom.css

//fun CSS(function: CSSDefinition): CSSBlock {
//    val block = CSSBlock()
//    block.function()
//    return block
//}

object CSS {
    operator fun invoke(function: CSSDefinition): CSSBlock {
        val block = CSSBlock()
        block.function()
        return block
    }

    fun style(name: String, vararg extends: CSSDef, function: CSSDef.() -> Unit) =
        style(name = name, extends = extends, then = false, f = function)
}

typealias CSSDefinition = CSSBlock.() -> Unit

fun CSSDefinition(func: CSSDefinition): CSSDefinition = func

class CSSBlock {
    private val csses = LinkedHashSet<CSSDef>()
    private val keyframes = LinkedHashSet<AnimationBuilder>()
    operator fun String.invoke(vararg extends: CSSDef, function: CSSDef.() -> Unit): CSSDef {
        val style = style(name = this, extends = extends, then = false, f = function)
        csses += style
        return style
    }

    fun buildRecursive(output: Appendable) {
        csses.forEach { it.buildRecursive(output, false) }
        keyframes.forEach { it.build(output) }
    }

    fun keyframes(name: String, function: AnimationBuilder.() -> Unit) {
        val builder = AnimationBuilder(name)
        function(builder)
        keyframes += builder
    }

    fun add(definition: CSSDefinition): CSSBlock {
        definition.invoke(this)
        return this
    }

    fun add(block: CSSBlock): CSSBlock {
        csses += block.csses
        keyframes += block.keyframes
        return this
    }

    fun add(def: Collection<CSSDef>): CSSBlock {
        def.forEach {
            if (csses.add(it)) {
                add(it.childs)
            }
        }
        return this
    }

    fun add(vararg def: CSSDef): CSSBlock {
        def.forEach {
            if (csses.add(it)) {
                add(it.childs)
            }
        }
        return this
    }

}
//val example = CSS {
//    keyframes("OLOLO") {
//        this.on(100) {
//            display = "none"
//        }
//    }
//}
//
//fun main() {
//    val sb = StringBuilder()
//    example.buildRecursive(sb)
//    println(sb)
//}