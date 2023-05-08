package com.startup.moving.interfaces

import com.startup.moving.domain.TravelRequestStatus
import com.startup.moving.domain.services.TravelService
import com.startup.moving.interfaces.incoming.TravelRequestMapper
import org.springframework.hateoas.EntityModel
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Service
@RestController
@RequestMapping(path = ["/travelRequests"], produces = [MediaType.APPLICATION_JSON_VALUE])
class TravelRequestAPI (
    val travelService: TravelService,
    val mapper: TravelRequestMapper
        ) {
    @PostMapping
    fun makeTravelRequest(@RequestBody travelRequestInput: TravelRequestInput) : EntityModel<TravelRequestOutput> {
        val travelRequest = travelService.saveTravelRequest(mapper.map(travelRequestInput))
        val output = mapper.map(travelRequest)
        return mapper.buildOutputModel(travelRequest, output)
    }

    @GetMapping
    fun listNearByRequests(@RequestParam currentAddress: String) : List<EntityModel<TravelRequestOutput>> {
        val requests = travelService.listNearByRequests(currentAddress)
        return mapper.buildOutputModel(requests)
    }
}

data class TravelRequestInput(
    val passengerId: Long,
    val origin: String,
    val destination: String
)

data class TravelRequestOutput(
    val id: Long,
    val origin: String,
    val destination: String,
    val status: TravelRequestStatus,
    val creationDate: LocalDateTime
)
