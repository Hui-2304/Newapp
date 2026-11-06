package vn.ptit.chuong5.bai2
 
data class PostRequest(val userId: Int, val title: String, val body: String)
 
data class PostResponse(val userId: Int?, val id: Int?, val title: String?, val body: String?)
