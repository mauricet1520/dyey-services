package com.reeceservices.dyeyservices.manager

import com.reeceservices.dyeyservices.data.CustomerInfo
import com.stripe.model.Customer
import com.stripe.model.PaymentMethod
import com.stripe.model.Plan
import com.stripe.model.Subscription
import org.springframework.stereotype.Component
import java.util.*

@Component
class StripeManager {

     fun createSubscriptionResponse(plan: Plan, customerInfo: CustomerInfo): Subscription {
        val item = HashMap<String, Any>()
        item["plan"] = plan.id
        val items = HashMap<String, Any>()
        items["0"] = item
        val expandList = LinkedList<String>()
        expandList.add("latest_invoice.payment_intent")
        val subscriptionParams = HashMap<String, Any>()

        subscriptionParams["customer"] = customerInfo.customerId ?: ""
        subscriptionParams["items"] = items

        subscriptionParams["expand"] = expandList

        return Subscription.create(subscriptionParams)
    }

     fun createProductPlan(): Plan {
        val product = HashMap<String, Any>()
        product["name"] = "Premium DYEY Subscription"
        val params = HashMap<String, Any>()
        params["amount"] = 999
        params["currency"] = "usd"
        params["interval"] = "month"
        params["product"] = product

        return Plan.create(params)
    }

     fun createCustomer(paymentMethod: PaymentMethod, customerInfo: CustomerInfo): Customer {
        val invoiceSettingsParams = HashMap<String, Any>()
        invoiceSettingsParams["default_payment_method"] = paymentMethod.id
        val customerParams = HashMap<String, Any>()
        customerParams["email"] = customerInfo.email ?: ""
        customerParams["payment_method"] = paymentMethod.id
        customerParams["invoice_settings"] = invoiceSettingsParams
        customerParams["description"] = "Customer: ${customerInfo.email}"
        return Customer.create(customerParams)
    }

     fun createPaymentMethod(customerInfo: CustomerInfo) {
        val paymentmethodParams = HashMap<String, Any>()
        paymentmethodParams["type"] = "card"
        val cardParams = HashMap<String, Any>()

        cardParams["number"] = customerInfo.number ?: 0
        cardParams["exp_month"] = customerInfo.exp_month ?: 0
        cardParams["exp_year"] = customerInfo.exp_year ?: 0
        cardParams["cvc"] = customerInfo.cvc ?: ""
        paymentmethodParams["card"] = cardParams
        val paymentMethod = PaymentMethod.create(paymentmethodParams)
        val customer = createCustomer(paymentMethod, customerInfo)

        customerInfo.customerId = customer.id
    }

}