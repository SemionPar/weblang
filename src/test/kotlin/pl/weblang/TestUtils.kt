package pl.weblang

class TestUtils {

    companion object {
        fun getResourceAsString(resourcePath: String) = javaClass.classLoader.getResourceAsStream(resourcePath).use { it.reader().readText() }
    }
}
