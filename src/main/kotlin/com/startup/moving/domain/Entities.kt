package com.startup.moving.domain

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class Driver (
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        val name: String,
        val birthDate: LocalDate
)

@Entity
data class Passenger (
        @Id
        @GeneratedValue
        var id: Long? = null,
        val name: String
        )

@Entity
data class TravelRequest(
        @Id
        @GeneratedValue
        var id: Long? = null,

        @ManyToOne
        val passenger: Passenger,
        val origin: String,
        val destination: String,
        val status: TravelRequestStatus = TravelRequestStatus.CREATED,
        val creationDate: LocalDateTime = LocalDateTime.now()
)

enum class TravelRequestStatus {
    CREATED, ACCEPTED, REFUSED
}

data class PatchDriver(
        val name: String?,
        val birthDate: LocalDate?
)

data class Film(
        val id: Long, val name: String, val provider: String
) {
        companion object {
                const val NOT_AVAILABLE = 0
                const val NOT_A_SERIE = "N/A"
        }

        @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy")
        var watchdate:LocalDate?=null

        var seriesNo: Int = NOT_AVAILABLE
        var episodeNo: Int = NOT_AVAILABLE
        var episodeName: String = NOT_A_SERIE
}