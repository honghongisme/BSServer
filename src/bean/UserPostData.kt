package bean

// 用户信息界面
data class UserPostData<T>(val endIndex: Int,
                           var postData: Array<T?>,
                           val countData : CountData
                           )