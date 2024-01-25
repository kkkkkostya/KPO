package org.example.di

import domain.CinemaValidatorImp
import data.CinemaDao
import data.RuntimeCinemaDao
import domain.CinemaValidator
import domain.CinemaController
import domain.CinemaControllerImp

object DI {
    private val cinemaValidator: CinemaValidator
        get() = CinemaValidatorImp()

    private val cinemaDao: CinemaDao by lazy {
        RuntimeCinemaDao()
    }

    val cinemaController: CinemaController
        get() = CinemaControllerImp(
            cinemaValidator = cinemaValidator,
            cinemaDao = cinemaDao,
        )
}