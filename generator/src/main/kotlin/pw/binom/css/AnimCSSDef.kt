package pw.binom.css

class AnimCSSDef(name: String, parent: CSSDef?, then: Boolean) :
    CSSDef(name = name, parent = parent, then = then, extends = emptyArray()) {
    override fun buildSelf(sb: Appendable, keyframes: Boolean, media: Boolean) {
        super.buildSelf(sb = sb, keyframes = keyframes, media = media)
    }
}
