package pl.weblang.persistence

/**
 * Base repository interface
 */
interface Repository<T> {
    fun create(exactHitVO: T)
    fun retrieveAll(): Iterator<T>
    fun getFieldNames(): List<String>
}
