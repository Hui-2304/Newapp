package com.group7.tonghop.demo7
import com.group7.tonghop.R
import com.group7.tonghop.DemoActivity
 
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
 
object RetrofitClient {
	private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
 
	val api: ApiService by lazy {
    	Retrofit.Builder()
        	.baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
        	.build()
        	.create(ApiService::class.java)
	}
}
