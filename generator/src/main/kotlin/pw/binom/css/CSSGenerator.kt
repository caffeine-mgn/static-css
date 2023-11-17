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
    private val blocks = LinkedHashSet<CSSBlock>()

    operator fun String.invoke(vararg extends: CSSDef, function: CSSDef.() -> Unit): CSSDef {
        val style = style(name = this, extends = extends, then = false, f = function)
        csses += style
        return style
    }

    open fun buildRecursive(output: Appendable) {
        csses.forEach { it.buildRecursive(output, keyframes = false, media = false) }
        keyframes.forEach { it.build(output) }
        blocks.forEach {
            it.buildRecursive(output)
        }
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

    fun add(vararg block: CSSBlock): CSSBlock {
        block.forEach {
            add(it)
        }
        return this
    }

    @JvmName("add2")
    fun add(blocks: Collection<CSSBlock>): CSSBlock {
        blocks.forEach {
            add(it)
        }
        return this
    }

    operator fun plusAssign(other: CSSBlock) {
        add(other)
    }

    @JvmName("plusAssign2")
    operator fun plusAssign(other: Collection<CSSBlock>) {
        add(other)
    }

    fun add(block: CSSBlock): CSSBlock {
        blocks += block
        return this
    }

    operator fun plusAssign(def: Collection<CSSDef>) {
        add(def)
    }

    operator fun plusAssign(def: CSSDef) {
        add(def)
    }

    fun add(def: Collection<CSSDef>): CSSBlock {
        def.forEach {
            add(it)
        }
        return this
    }

    fun add(def: CSSDef): CSSBlock {
        if (csses.add(def)) {
            add(def.childs)
        }
        return this
    }

    fun add(vararg def: CSSDef): CSSBlock {
        def.forEach {
            add(it)
        }
        return this
    }
}
