package com.startup.moving.domain.services

import com.startup.moving.domain.TravelRequest
import com.startup.moving.domain.TravelRequestRepository
import com.startup.moving.domain.TravelRequestStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestParam

@Component
class TravelService(
    val travelRequestRepository: TravelRequestRepository,
    val gmapService: GmapService
) {
    val MAX_TRAVEL_TIME : Int = 600

    fun saveTravelRequest(travelRequest: TravelRequest) = travelRequestRepository.save(travelRequest)

    fun listNearByRequests(@RequestParam currentAddress: String): List<TravelRequest> {
        val requests = travelRequestRepository.findByStatus(TravelRequestStatus.CREATED)

        return requests.filter { tr -> gmapService.getDistanceBetweenAddresses(currentAddress, tr.origin) < MAX_TRAVEL_TIME }
    }
}



