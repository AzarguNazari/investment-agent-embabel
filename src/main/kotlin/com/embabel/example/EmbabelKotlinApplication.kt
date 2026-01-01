package com.embabel.example

import com.embabel.agent.config.annotation.EnableAgents
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableAgents
class EmbabelKotlinApplication

fun main(args: Array<String>) {
    runApplication<EmbabelKotlinApplication>(*args)
}
