package pw.binom.css

fun CSS(function: CSSDefinition): CSSBlock {
    val block = CSSBlock()
    block.function()
    return block
}

typealias CSSDefinition = CSSBlock.() -> Unit

fun CSSDefinition(func: CSSDefinition): CSSDefinition = func

class CSSBlock {
    private val csses = ArrayList<CSSDef>()
    private val keyframes = ArrayList<AnimationBuilder>()
    operator fun String.invoke(function: CSSDef.() -> Unit) {
        csses += style(this, false, function)
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

    fun apply(definition: CSSDefinition) {
        definition.invoke(this)
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