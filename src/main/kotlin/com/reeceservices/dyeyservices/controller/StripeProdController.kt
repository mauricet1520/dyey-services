package com.reeceservices.dyeyservices.controller

import com.reeceservices.dyeyservices.data.CustomerInfo
import com.reeceservices.dyeyservices.manager.StripeManager
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.HashMap
import com.stripe.Stripe
import com.stripe.model.Plan
import com.stripe.model.Customer
import com.stripe.model.Subscription
import org.springframework.web.bind.annotation.RequestBody
import java.util.LinkedList
import com.stripe.model.PaymentMethod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource


@RestController
@PropertySource("classpath:/application.properties")
class StripeProdController(
        @Value("\${api-key}") val apiKey: String,
        @Autowired val stripeManager: StripeManager) {

    @PostMapping("/createSubscriptionWithCard")
    fun createSubscriptionWithCard(@RequestBody customerInfo: CustomerInfo): String {
        Stripe.apiKey = apiKey
        stripeManager.createPaymentMethod(customerInfo)
        return stripeManager.createSubscriptionResponse(stripeManager.createProductPlan(), customerInfo).id
    }

    @PostMapping("/createSubscriptionWithCustomerId")
    fun createSubscriptionWithCustomerId(@RequestBody customerInfo: CustomerInfo): String {
        Stripe.apiKey = apiKey
        return stripeManager.createSubscriptionResponse(stripeManager.createProductPlan(), customerInfo).id
    }
}