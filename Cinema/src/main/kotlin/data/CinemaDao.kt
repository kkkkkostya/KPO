package data

import JsonHandler
import OutputModel
import domain.Error
import domain.Result
import domain.Success
import domain.entity.FreeSeat
import domain.entity.MovieEntity
import domain.entity.SessionEntity
import domain.entity.TimetableEntity
import java.io.File
import java.time.LocalDate


interface CinemaDao {
    fun addMovie(name: String, description: String = "")
    fun getAll(): MutableSet<String>
    fun get(name: String): Result
    fun deleteMovie(name: String): Result
    fun addSession(name: String, date: LocalDate): String
    fun modifyMovie(name: String, description: String)
    fun modifyPlace(name: String?, date: LocalDate, a: Int?, b: Int?)
    fun returnPlace(name: String?, date: LocalDate, a: Int?, b: Int?)
    fun seeSession(name: String, date: LocalDate): Array<Array<FreeSeat>>?
    fun modifyTimetable(name: String, date: LocalDate, newDate: LocalDate)
    fun markPlace(name: String, date: LocalDate, a: Int?, b: Int?): String
}

class RuntimeCinemaDao : CinemaDao {
    init {
        var fileObject = File("movies.json")
        fileObject.createNewFile()
        fileObject = File("sessions.json")
        fileObject.createNewFile()
        fileObject = File("timetable.json")
        fileObject.createNewFile()
    }

    private val movieFile = JsonHandler("movies.json")
    private val sessionFile = JsonHandler("sessions.json")
    private val timeTableFile = JsonHandler("timetable.json")
    private val movies = mutableMapOf<String, MovieEntity>()
    private val sessions = mutableMapOf<String, SessionEntity>()
    private val timeTable = mutableMapOf<String, TimetableEntity>()

    override fun addMovie(name: String, description: String) {
        movies[name] = MovieEntity(name, description)
        movieFile.save(movies)
    }

    override fun modifyMovie(name: String, description: String) {
        movies[name]?.description = description
        movieFile.save(movies)
    }

    override fun getAll(): MutableSet<String> {
        return movies.keys
    }

    override fun get(name: String): Result {
        if (!movies.containsKey(name))
            return Error(OutputModel("\nNo such film\n"))
        return Success
    }

    override fun deleteMovie(name: String): Result {
        if (!movies.containsKey(name))
            return Error(OutputModel("\nNo such film\n"))
        movies.remove(name)
        sessions.remove(name)
        timeTable.remove(name)
        movieFile.save(movies)
        sessionFile.save(sessions)
        timeTableFile.save(timeTable)
        return Success
    }

    override fun addSession(name: String, date: LocalDate): String {
        if (!timeTable.containsKey(name)) {
            sessions[name] = SessionEntity(name)
            timeTable[name] = TimetableEntity(name)
        }
        if (!movies.containsKey(name))
            addMovie(name,"")
        sessions[name]?.add(date)
        timeTable[name]?.add(date)
        timeTableFile.save(timeTable)
        sessionFile.save(sessions)
        return "Session successfully added"
    }

    override fun modifyPlace(name: String?, date: LocalDate, a: Int?, b: Int?) {
        timeTable[name]?.update(date, Pair(a, b))
        timeTableFile.save(timeTable)
    }

    override fun returnPlace(name: String?, date: LocalDate, a: Int?, b: Int?) {
        timeTable[name]?.returnPlace(date, Pair(a, b))
        timeTableFile.save(timeTable)
    }

    override fun seeSession(name: String, date: LocalDate): Array<Array<FreeSeat>>? {
        return timeTable[name]?.get(date)
    }

    override fun modifyTimetable(name: String, date: LocalDate, newDate: LocalDate) {
        timeTable[name]?.replaceDate(date, newDate)
        timeTableFile.save(timeTable)
    }

    override fun markPlace(name: String, date: LocalDate, a: Int?, b: Int?): String {
        val res = timeTable[name]?.markPlace(date, Pair(a, b))
        if (res is Error)
            return res.outputModel.message
        timeTableFile.save(timeTable)
        return "Success"
    }
}