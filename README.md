## Simple Plugin for generate css from code

### Example

#### File build.gradle file

```groovy

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url "https://repo.binom.pw/releases"
        }
    }
    dependencies {
        classpath("pw.binom.static-css:plugin:0.1.28")
    }
}

apply plugin: "static-css"

buildCss {
    outputCss = file("${project.buildDir}/my-css-file.css")//path for output file
}
```

#### File src/main/kotlin/pw/binom/css/Main.kt

```kotlin
package pw.binom.css

val style = CSS {
    "bigPlayButton" {
        position = "absolute"
        left = "50%"
        top = "50%"

        ":active" > {
            top = "50%"
        }

        ":hover" > {
            top = "11"
        }
    }
}
```

Plugin will use variable `pw.binom.css.style` as root for your styles

### Custom packages of your styles
Also, you can create your styles in other packages. You can use function `apply`.<br>
Example:

#### File src/main/kotlin/pw/binom/css/Main.kt

```kotlin
package pw.binom.css

import my.packages.makeAnimation

val style = CSS {
    apply(makeAnimation)
}
```

#### File src/main/kotlin/my/packages/SomeFile.kt

```kotlin
package my.packages

private const val FIRST_DOT = "loading_first_dot"
private const val BIG_SCALE = 2.0

fun getPointPos(index: Int) = TODO("Some implement")

val makeAnimation = CSSDefinition {
    keyframes(FIRST_DOT) {
        0 {
            transform = "translateX(${getPointPos(0)}px) scale(1)"
        }
        25 {
            transform = "translateX(${getPointPos(0)}px) scale($BIG_SCALE)"
        }
        50 {
            transform = "translateX(${getPointPos(0)}px) scale(1)"
        }
        83 {
            transform = "translateX(${getPointPos(4)}px) scale(1)"
        }
        100 {
            transform = "translateX(${getPointPos(0)}px) scale(1)"
        }
    }
}

```