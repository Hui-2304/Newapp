package com.group7.tonghop.demo7
import com.group7.tonghop.R
import com.group7.tonghop.DemoActivity
 
import retrofit2.http.GET
import retrofit2.http.Path
 
interface ApiService {
	@GET("posts")
	suspend fun getPosts(): List<Post>
 
	@GET("posts/{id}")
	suspend fun getPost(@Path("id") id: Int): Post
}
