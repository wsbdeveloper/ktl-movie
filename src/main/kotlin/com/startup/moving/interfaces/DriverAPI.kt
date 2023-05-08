package com.startup.moving.interfaces

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.startup.moving.domain.Driver
import com.startup.moving.domain.DriverRepository
import com.startup.moving.domain.Film
import com.startup.moving.domain.PatchDriver
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.io.File
import java.nio.charset.StandardCharsets

@Service
@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class DriverAPI (
    val driverRepository: DriverRepository)
{
    @GetMapping("/drivers")
    fun listDrivers() : List<Driver> = driverRepository.findAll()

    @GetMapping("/drivers/{id}")
    fun findDriver(@PathVariable("id") id: Long) =
            driverRepository.findById(id).orElseThrow() {
                ResponseStatusException(HttpStatus.NOT_FOUND)
            }

    @GetMapping("/drivers/type")
    fun findDriverFilters(@RequestParam("relations") relations: List<String>) =
        driverRepository.findAllByNameIn(relations)


    @PostMapping("/drivers")
    fun createDriver(@RequestBody driver : Driver) : Driver =
        driverRepository.save(driver)

    @PutMapping("/drivers/{id}")
    fun fullUpdateDriver(@PathVariable("id") id: Long, @RequestBody driver
    : Driver) : Driver {
        val foundDriver = findDriver(id)
        val copyDriver = foundDriver.copy(
            birthDate = driver.birthDate,
            name = driver.name
        )
        return driverRepository.save(copyDriver)
    }

    @PatchMapping("/drivers/{id}")
    fun incrementalUpdateDriver(@PathVariable("id") id: Long, @RequestBody driver
    : PatchDriver) : Driver {
        val foundDriver = findDriver(id)
        val copyDriver = foundDriver.copy(
            birthDate = driver.birthDate ?: foundDriver.birthDate,
            name = driver.name ?: foundDriver.name
        )
        return driverRepository.save(copyDriver)
    }

    @DeleteMapping("/drivers/{id}")
    fun deleteDriver(@PathVariable("id") id: Long) =
        driverRepository.deleteById(id)

    // Example get file data
    @GetMapping("/mocked-api")
    fun getMockedData(): ResponseEntity<List<Film>> {
        val mapper = jacksonObjectMapper()
        mapper.registerKotlinModule()
        mapper.registerModule(JavaTimeModule())

        val jsonString: String = File("./src/main/resources/films.json").readText(Charsets.UTF_8)
        val jsonTextList: List<Film> = mapper.readValue<List<Film>>(jsonString)

        return ResponseEntity.ok().body(jsonTextList)
    }


    // Example for file json
    @PostMapping("/mocked-api")
    fun createMockedData(): ResponseEntity<List<Film>> {
        val mapper = jacksonObjectMapper()
        mapper.registerKotlinModule()
        mapper.registerModule(JavaTimeModule())

        val jsonString: String = File("./src/main/resources/films.json").readText(Charsets.UTF_8)

        val new = Film(
            name = "Qualquer",
            provider = "Qualquer",
            id = 1
        )

        val jsonTextList: List<Film> = mapper.readValue(jsonString)
        val updatedJson = jsonTextList.plus(new)


        val salvar = mapper.writeValueAsString(updatedJson)

        File("./src/main/resources/films.json").writeText(salvar)

        return ResponseEntity.ok().body(updatedJson)
    }
}