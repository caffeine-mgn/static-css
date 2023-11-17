package pw.binom.css

class RootCSSDef : CSSBlock() {
    private val medias = LinkedHashSet<MediaCSSBlock>()

    fun media(title: String, func: CSSBlock.() -> Unit): CSSBlock {
        val block = MediaCSSBlock(title)
        func(block)
        medias += block
        return block
    }

    override fun buildRecursive(output: Appendable) {
        super.buildRecursive(output)
        medias.forEach { it.buildRecursive(output) }
    }
}
