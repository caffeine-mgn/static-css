plugins {
    kotlin("jvm")
}
apply {
    plugin(pw.binom.plugins.BinomPublishPlugin::class.java)
}

apply<pw.binom.plugins.DocsPlugin>()
