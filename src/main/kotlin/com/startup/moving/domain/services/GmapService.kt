package com.startup.moving.domain.services

import com.jayway.jsonpath.JsonPath
import com.startup.moving.interfaces.TravelRequestOutput
import net.minidev.json.JSONArray
import org.springframework.beans.factory.annotation.Value
import org.springframework.hateoas.EntityModel
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestTemplate

@Service
class GmapService(
    @Value("\${app.moving.domain.googlemaps.apikey}")
    val appKey: String
) {

    val GMAPS_TEMPLATE : String = "https://maps.googleapis.com/maps/api/directions/json?origin={origin}&destination={destination}&key={key}"

    fun getDistanceBetweenAddresses(addressOne: String, addressTwo: String) : Int {
        val template = RestTemplate()
        val jsonResult = template.getForObject(GMAPS_TEMPLATE, String::class.java, addressOne, addressTwo)
        val rawResults : JSONArray = JsonPath.parse(jsonResult).read("\$..legs[*].duration.value")

        return rawResults.map { it as Int }.minOrNull() ?: Int.MAX_VALUE
    }
}