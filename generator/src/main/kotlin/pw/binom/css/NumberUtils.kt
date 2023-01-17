package pw.binom.css

val Int.px: String
    get() = "${this}px"

val Float.px: String
    get() = "${this}px"

val Double.px: String
    get() = "${this}px"

val Int.percent: String
    get() = "$this%"

val Float.percent: String
    get() = "$this%"

val Double.percent: String
    get() = "$this%"

fun rgb(r: Double, g: Double, b: Double): String = "rgb($r,$g,$b)"
fun rgba(r: Double, g: Double, b: Double, a: Double) = "rgba($r,$g,$b,$a)"

fun rgb(r: Float, g: Float, b: Float): String = "rgb($r,$g,$b)"
fun rgba(r: Float, g: Float, b: Float, a: Float) = "rgba($r,$g,$b,$a)"

val Int.color
    get() = "#${toString(16)}"

val transparent
    get() = "transparent"

val none
    get() = "none"

val absolute
    get() = "absolute"
val relative
    get() = "relative"

fun CSSDef.paddingPx(top: Float, right: Float, bottom: Float, left: Float) {
    padding = "${top.px} ${right.px} ${bottom.px} ${left.px}"
}

object Display {
    val flex = "flex"
    val none = "none"
    val block = "block"
    val empty = ""
}

object Flex {
    object JustifyContent {
        val flexStart = "flex-start"
        val flexEnd = "flex-end"
        val center = "center"
        val spaceBetween = "space-between"
        val spaceAround = "space-around"
    }

    object Align {
        val flexStart = "flex-start"
        val flexEnd = "flex-end"
        val center = "center"
        val stretch = "stretch"
        val baseline = "baseline"
    }
}
