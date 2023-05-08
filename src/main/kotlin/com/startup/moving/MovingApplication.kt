package com.startup.moving

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MovingApplication

fun main(args: Array<String>) {
	runApplication<MovingApplication>(*args)
}
