package domain

import OutputModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class Result

object Success : Result()
class Error(val outputModel: OutputModel) : Result()


interface CinemaValidator {
    fun validateNumber(name: String, a: Int, b: Int): Result
    fun validateMenu(name: String, menu: String): Result
    fun validateDate(date: String): Result
}

class CinemaValidatorImp : CinemaValidator {
    override fun validateNumber(name: String, a: Int, b: Int): Result {
        if (name.toIntOrNull() == null || name.toInt() < a || name.toInt() > b)
            return Error(OutputModel("\nНеверно введено число\n"))
        return Success
    }

    override fun validateMenu(name: String, menu: String): Result {
        return when {
            (!menu.contains(name) || name.length > 1) -> Error(OutputModel("\nIncorrect number. Try to input again\n"))
            else -> Success
        }
    }

    override fun validateDate(date: String): Result {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return try {
            LocalDate.parse(date, formatter)
            Success
        } catch (e: Exception) {
            Error(OutputModel("\nIncorrect year. Try to input again\n"))
        }
    }
}
