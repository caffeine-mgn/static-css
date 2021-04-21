package pw.binom.css

class AnimCSSDef(name: String, parent: CSSDef?, then: Boolean) : CSSDef(name = name, parent = parent, then = then) {
    override fun buildSelf(sb: Appendable, keyframes: Boolean) {
        super.buildSelf(sb, keyframes)
    }
}