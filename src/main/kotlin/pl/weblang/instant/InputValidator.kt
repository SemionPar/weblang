package pl.weblang.instant

class InputValidator {
    fun validate(input: String): Unit {
        require(input.isNotBlank())
    }
}
