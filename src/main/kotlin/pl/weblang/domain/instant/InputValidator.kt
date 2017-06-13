package pl.weblang.domain.instant

class InputValidator {
    fun validate(input: String): Unit {
        require(input.isNotBlank())
    }
}
