package com.sportser.sportser_coach.service.registration

import com.sportser.sportser_coach.service.OperationCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationRemoteDataSource(apiRegistration: ApiRegistration): RegistrationDataSource {

    private val services = ApiRegistration.build()
    private var callRegistration: Call<String>? = null

    override fun sendRegistration(callback: OperationCallback<String>, email: String) {
        println("Send registration with " + email)
        val request = RegistrationRequest(email, true)
        callRegistration = services.getRegistration(request)

        callRegistration?.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.body() != null){
                   val responseData: String = response.body()!!
                   print(response.body())
                    callback.onSuccess(responseData)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                print(t)
            }
        })
    }

    override fun deleteRegistration(callback: OperationCallback<String>, email: String) {
        println("Delete registration with " + email)
        val request = RegistrationRequest(email, false)
        callRegistration = services.getRegistration(request)

        callRegistration?.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.body() != null){
                    val responseData: String = response.body()!!
                    print(response.body())
                    callback.onSuccess(responseData)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                print(t)
            }
        })
    }
}