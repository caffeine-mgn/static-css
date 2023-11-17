package pw.binom.css

object CSS {
    operator fun invoke(function: RootCSSDef.() -> Unit): CSSBlock {
        val block = RootCSSDef()
        block.function()
        return block
    }

    fun style(name: String, vararg extends: CSSDef, function: CSSDef.() -> Unit) =
        style(name = name, extends = extends, then = false, f = function)
}

typealias CSSDefinition = CSSBlock.() -> Unit

fun CSSDefinition(func: CSSDefinition): CSSDefinition = func

open class CSSBlock {
    private val csses = LinkedHashSet<CSSDef>()
    private val keyframes = LinkedHashSet<AnimationBuilder>()

    operator fun String.invoke(vararg extends: CSSDef, function: CSSDef.() -> Unit): CSSDef {
        val style = style(name = this, extends = extends, then = false, f = function)
        csses += style
        return style
    }

    open fun buildRecursive(output: Appendable) {
        csses.forEach { it.buildRecursive(output, keyframes = false, media = false) }
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
