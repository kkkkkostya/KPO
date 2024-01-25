package domain.entity

import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class SessionEntity{
    var dates: ArrayList<LocalDate>?  = null
    var name: String? = null

    constructor(name: String) {
        this.name=name
        dates = ArrayList<LocalDate>()
    }

    private constructor(){}

    fun add(date: LocalDate){
        dates?.add(date)
    }
}