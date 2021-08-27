package pw.binom.css

class AnimationBuilder(val name: String) {
    internal val animations = ArrayList<AnimCSSDef>()


    operator fun Int.invoke(function: CSSDef.() -> Unit) {
        on(persent = this, function)
    }

    fun on(persent: Int, func: AnimCSSDef.() -> Unit) {
        val d = AnimCSSDef(
            name = "$persent%",
            parent = null,
            then = false
        )
        func(d)
        animations += d
    }

    internal fun build(out: Appendable) {
        out.append("@keyframes ").append(name).append("{")
        animations.forEach {
            it.buildRecursive(out, false)
        }
        out.append("}")
    }
}