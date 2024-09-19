package com.test.domain.api

import com.test.domain.models.Offer
import com.test.domain.models.OffersResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("offers.json")
    fun getOffers(): Call<OffersResponse>
}

