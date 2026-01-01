package com.embabel.example.investmentmanager

import com.embabel.agent.api.invocation.AgentInvocation
import com.embabel.agent.core.AgentPlatform
import com.embabel.example.investmentmanager.dto.InvestmentEnquiryRequest
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.embabel.example.investmentmanager.dto.InvestmentDirectResponse

@RestController
@RequestMapping("/api/investment")
@CrossOrigin(origins = ["*"])
class InvestmentController(private val agentPlatform: AgentPlatform) {

    @GetMapping
    fun handleEnquiry(@RequestParam question: String): String {
        val invocation = AgentInvocation.create(agentPlatform, String::class.java)
        return invocation.invoke(InvestmentEnquiryRequest(question))
    }
}
