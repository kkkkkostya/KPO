package domain

import data.CinemaDao
import domain.entity.FreeSeat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

interface CinemaController {
    fun modifyMovie()
    fun modifyTickets(method: CinemaDao.(String?, LocalDate, Int?, Int?) -> Unit)
    fun seePlaces()
    fun modifyTimetable()
    fun checkPlace()
    fun menu()
    fun exit()
}

class CinemaControllerImp(
    private val cinemaDao: CinemaDao,
    private val cinemaValidator: CinemaValidator,
) : CinemaController {


    override fun modifyMovie() {
        println("\nВведите 1 - чтобы добавить новый фильм\n2 - изменить существующий\n0 - Выход")
        var option = readln()
        var flag = cinemaValidator.validateMenu(option, "012")
        while (flag is domain.Error) {
            println("Некорректный ввод. Нужно ввести число 1, 2 или 0")
            option = readln()
            flag = cinemaValidator.validateMenu(option, "012")
        }
        when (option) {
            "0" -> return
            "1" -> getMovieName()
            "2" -> changeMovie()
        }
    }

    fun getMovieName() {
        println("Введите имя фильма")
        val option = readln()
        if (cinemaDao.get(option) !is domain.Error) {
            println("Такой фильм уже существует, изменить его? Чтобы изменить введите 1 иначе любой другой символ")
            val mod = readln()
            if (mod != "1")
                return
            else {
                println("Введите описание фильма")
                val des = readln()
                cinemaDao.modifyMovie(option, des)
            }
        }
        println("Введите описание фильма")
        val des = readln()
        cinemaDao.addMovie(option, des)
    }

    fun changeMovie() {
        println("Введите имя фильма")
        val option = readln()
        if (cinemaDao.get(option) is domain.Error) {
            println("Такой фильма не существует, создать его? Чтобы создать новый введите 1 иначе любой другой символ")
            val mod = readln()
            if (mod != "1")
                return
            else {
                println("Введите описание фильма")
                val des = readln()
                cinemaDao.addMovie(option, des)
                return
            }
        }
        println("Введите описание фильма")
        val des = readln()
        cinemaDao.modifyMovie(option, des)
    }

    fun getFilmName(): String? {
        println("\nВведите имя фильма\n")
        val name = readln()
        if (cinemaDao.get(name) is domain.Error) {
            println("\nТакого фильма нет в прокате. Сперва нужно создать его\n")
            return null
        }
        return name
    }

    fun getDate(output: String = "Введите дату показа фильма в формате: yyyy-MM-dd HH:mm"): String? {
        println(output)
        val input = readln()
        if (cinemaValidator.validateDate(input) is domain.Error) {
            println("Incorrect year. Try to input again")
            return null
        }
        return input
    }

    fun modPlace(name: String?, date: LocalDate, method: CinemaDao.(String?, LocalDate, Int?, Int?) -> Unit): String? {
        println("Введите число проданных билетов")
        val count = readln()
        if (cinemaValidator.validateNumber(count, 1, Int.MAX_VALUE) is domain.Error)
            return null
        println(
            "Введите номер продаваемых мест в виде: \n" +
                    "Место 1: ряд номер\n" +
                    "Место 2: ряд номер\n" +
                    "и тд\nВсего в зале 15 рядом по 15 мест"
        )
        for (i in 0..<count.toInt()) {
            var num = readLine()!!.split(" ").map { it.toIntOrNull() }
            if (num[0] == null || num.size != 2 || num[0]!! > 15 || num[0]!! < 1 || num[1]!! > 15 || num[1]!! < 1) {
                println("Неверно указано место")
                return null
            }
            cinemaDao.method(name, date, num[0]?.minus(1), num[1]?.minus(1))
        }
        return "Success"
    }

    override fun modifyTickets(method: CinemaDao.(String?, LocalDate, Int?, Int?) -> Unit) {
        val name = getFilmName() ?: return
        val dateStr = getDate() ?: return
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val date = LocalDate.parse(dateStr, formatter)
        if (cinemaDao.seeSession(name, date) == null) {
            println("\nТакого сеанса нет. Сперва нужно создать соответствующий сеанс\n")
            return
        }
        modPlace(name, date, method)
    }

    override fun seePlaces() {
        val name = getFilmName() ?: return
        val date = getDate() ?: return
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val places = cinemaDao.seeSession(name, LocalDate.parse(date, formatter))
        if (places != null) {
            println("\nF - place is free, N - is not, M - is marked\n")
            print("   ")
            for (i in places.indices)
                print((i + 1).toString()+" ")
            println()
            for (i in places.indices) {
                if (i+1<10)
                    print((i + 1).toString() + "  ")
                else
                    print((i + 1).toString() + " ")
                for (j in 0..<places[i].size) {
                    if (places[i][j] == FreeSeat.Free)
                        print("F ")
                    else if (places[i][j] == FreeSeat.Occupied)
                        print("N ")
                    else
                        println("M ")
                }
                println()
            }
        }
    }

    override fun modifyTimetable() {
        val name = getFilmName() ?: return
        val date = getDate() ?: return
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateChange = getDate("Введите новую дату фильма") ?: return
        cinemaDao.modifyTimetable(name, LocalDate.parse(date, formatter), LocalDate.parse(dateChange, formatter))
    }

    override fun checkPlace() {
        val name = getFilmName() ?: return
        val date = getDate() ?: return
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        println("Введите кол-во отметки занятых мест")
        val count = readln()
        if (cinemaValidator.validateNumber(count, 1, Int.MAX_VALUE) is domain.Error)
            return
        println(
            "Введите номер продаваемых мест в виде: \n" +
                    "x y - номер места 1, номер ряда\n" +
                    "x y - номер места 2, номер ряда\n" +
                    "и тд\nВсего в зале 15 рядов по 15 мест"
        )
        for (i in 0..<count.toInt()) {
            var num = readLine()!!.split(" ").map { it.toIntOrNull() }
            if (num[0] == null || num.size != 2 || num[0]!! > 15 || num[0]!! < 1 || num[1]!! > 15 || num[1]!! < 1) {
                println("Неверно указано место")
                return
            }
            val res = cinemaDao.markPlace(name, LocalDate.parse(date, formatter), num[0]?.minus(1), num[1]?.minus(1))
            if (res!="Success")
                println("\nМесто с номером ${num[0]} ряд ${num[1]} свободно. Его нельзя отметить как занятое!\n")
        }
    }

    fun addNewSession() {
        val name = getFilmName() ?: return
        val date = getDate() ?: return
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        cinemaDao.addSession(name, LocalDate.parse(date, formatter))
    }

    override fun menu() {
        val inputStr =
            "\nВыберить один из следующих пунктов: \n1) Продажа билетов\n2) Возврат билетов\n3) Просмотр свободных и проданных " +
                    "мест\n4) Редактировать данные о фильме или создать новый\n5) Добавить сеанс\n6) Редактировать расписание\n7) Отметка занятых мест\n8) Выход\n"
        println(inputStr)
        var option = readln()
        while (option != "8") {
            var flag = cinemaValidator.validateMenu(option, "12345678")
            while (flag is domain.Error) {
                println("\nНужно ввести число из диапазона 1-8\n")
                option = readln()
                flag = cinemaValidator.validateMenu(option, "12345678")
            }
            when (option) {
                "1" -> modifyTickets(CinemaDao::modifyPlace)
                "2" -> modifyTickets(CinemaDao::returnPlace)
                "3" -> seePlaces()
                "4" -> modifyMovie()
                "5" -> addNewSession()
                "6" -> modifyTimetable()
                "7" -> checkPlace()
                "8" -> break
            }
            println(inputStr)
            option = readln()
        }
        exit()
    }

    override fun exit() {
        println("\nGoodbye!\n")
    }
}