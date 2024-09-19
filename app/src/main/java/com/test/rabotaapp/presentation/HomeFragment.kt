package com.test.rabotaapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.domain.api.ApiClient
import com.test.domain.api.ApiInterface
import com.test.domain.models.Offer
import com.test.domain.models.OffersResponse
import com.test.rabotaapp.R
import com.test.rabotaapp.presentation.adapter.OfferAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader

class HomeFragment : Fragment() {

    private lateinit var offerRV: RecyclerView
    private val offerAdapter by lazy(LazyThreadSafetyMode.NONE) {OfferAdapter(requireContext(), arrayListOf())}
    private lateinit var apiInterface: ApiInterface
    private lateinit var mockWebServer: MockWebServer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        offerRV = view.findViewById(R.id.horizontal_recycler_view)
        offerRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        offerRV.adapter = offerAdapter

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                startMockServer()
            }
            loadData()
        }
    }

    private fun startMockServer() {
        mockWebServer = MockWebServer()
        val json = loadJsonFromAssets("offers.json")
        mockWebServer.enqueue(MockResponse().setBody(json).setHeader("Content-Type", "application/json"))
        mockWebServer.start()

        val baseUrl = mockWebServer.url("/").toString()
        apiInterface = ApiClient.getClient(baseUrl).create(ApiInterface::class.java)
    }

    private fun loadJsonFromAssets(fileName: String): String {
        val inputStream = requireContext().assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }

    private fun loadData() {
        apiInterface.getOffers().enqueue(object : Callback<OffersResponse> {
            override fun onResponse(call: Call<OffersResponse>, response: Response<OffersResponse>) {
                Log.d("HomeFragment", "onResponse")
                if (response.isSuccessful) {
                    Log.d("HomeFragment", "Response Successful")
                    Log.d("HomeFragment", "Response.body: ${response.body().toString()}")
                    val offersResponse = response.body()
                    offersResponse?.offers?.let {
                        offerAdapter.updateData(it)
                    }
                }
            }

            override fun onFailure(call: Call<OffersResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("HomeFragment", "Failure: ${t.message}", t)
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::mockWebServer.isInitialized) {
            mockWebServer.shutdown()
        }
    }
}
