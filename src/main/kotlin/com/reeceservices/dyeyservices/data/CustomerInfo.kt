package com.reeceservices.dyeyservices.data

data class CustomerInfo (
        val email: String?,
        val number: String?,
        val exp_month: Int?,
        val exp_year: Int?,
        val cvc: String?,
        var customerId: String?
)