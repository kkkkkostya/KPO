package domain.entity

import OutputModel
import domain.Error
import domain.Success
import domain.Result
import java.time.LocalDate

enum class FreeSeat {
    Free, Occupied, Marked
}


class TimetableEntity(name: String) {
    val table = mutableMapOf<LocalDate, Array<Array<FreeSeat>>?>()
    var name: String? = name

    fun add(date: LocalDate) {
        if (!table.containsKey(date))
            table[date] = Array(15) { Array(15) { FreeSeat.Free } }
    }

    fun get(date: LocalDate): Array<Array<FreeSeat>>? {
        return table[date]
    }

    fun markPlace(date: LocalDate, place: Pair<Int?, Int?>): Result {
        if (place.first?.let { place.second?.let { it1 -> table[date]?.get(it)?.get(it1) } } == FreeSeat.Free)
            return Error(OutputModel("Place is free"))
        table[date]?.let { row -> row[place.first!!].let { col -> col[place.second!!] = FreeSeat.Marked } }
        return Success
    }

    fun update(date: LocalDate, place: Pair<Int?, Int?>) {
        table[date]?.let { row -> row[place.first!!].let { col -> col[place.second!!] = FreeSeat.Occupied } }
    }

    fun returnPlace(date: LocalDate, place: Pair<Int?, Int?>) {
        table[date]?.let { row -> row[place.first!!].let { col -> col[place.second!!] = FreeSeat.Free } }
    }

    fun replaceDate(date: LocalDate, newDate: LocalDate) {
        table.set(newDate, table.get(date))
        table.remove(date)
    }
}