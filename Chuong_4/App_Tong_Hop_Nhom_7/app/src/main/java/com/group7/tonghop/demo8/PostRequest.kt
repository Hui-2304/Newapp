package com.group7.tonghop.demo8
import com.group7.tonghop.R
import com.group7.tonghop.DemoActivity
 
data class PostRequest(val userId: Int, val title: String, val body: String)
 
data class PostResponse(val userId: Int?, val id: Int?, val title: String?, val body: String?)
