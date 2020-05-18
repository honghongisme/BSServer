package bean.post

data class PostItem (var userId : Int,
                     var postId : Int,
                     var userAccount : String, // 用户名
                     var text : String,
                     var date : String
)