package domain.entity

data class MovieEntity(
    var name: String,
    var description: String
) {
    override fun toString(): String = "Name: ${name}\nDescription: ${description}"
}