package com.startup.moving.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DriverRepository : JpaRepository<Driver, Long> {
    fun findAllByNameIn(name: List<String>): List<Driver>
}

@Repository
interface PassengerRepository: JpaRepository<Passenger, Long>

@Repository
interface TravelRequestRepository: JpaRepository<TravelRequest, Long> {
    fun findByStatus(status: TravelRequestStatus) : List<TravelRequest>
}