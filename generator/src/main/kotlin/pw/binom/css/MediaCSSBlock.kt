package pw.binom.css

class MediaCSSBlock(val title: String) : CSSBlock() {
    override fun buildRecursive(output: Appendable) {
        output.append("@media ").append(title).append("{")
        super.buildRecursive(output)
        output.append("}")
    }
}
