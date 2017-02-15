package pl.weblang.persistence

/**
 * Base repository interface
 */
interface Repository<T> {
    fun create(entity: T)
    fun retrieveAll(): Iterator<T>
    fun getFieldNames(): List<String>
}
