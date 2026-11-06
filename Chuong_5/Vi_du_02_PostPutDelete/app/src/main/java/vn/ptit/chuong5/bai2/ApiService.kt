package vn.ptit.chuong5.bai2
 
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
 
interface ApiService {
	@POST("posts")
	suspend fun createPost(@Body post: PostRequest): Response<PostResponse>
 
	@PUT("posts/{id}")
	suspend fun updatePost(@Path("id") id: Int, @Body post: PostRequest): Response<PostResponse>
 
	@DELETE("posts/{id}")
	suspend fun deletePost(@Path("id") id: Int): Response<Void>
}
