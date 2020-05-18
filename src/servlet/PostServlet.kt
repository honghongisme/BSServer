package servlet

import Constants.INDEX
import Constants.OPERATION
import Constants.OPERATION_COLLECT_POST
import Constants.OPERATION_DELETE_COLLECTION
import Constants.OPERATION_PUBLISH_POST
import Constants.OPERATION_SELECT_COLLECTION
import Constants.OPERATION_SELECT_COMMENT
import Constants.OPERATION_SELECT_POST
import Constants.OPERATION_SELECT_USER_POST
import Constants.OPERATION_SEND_COMMENT
import Constants.POST_ID
import Constants.RESULT_ERROR
import Constants.RESULT_OK
import Constants.TEXT
import Constants.USER_ID
import bean.PostDetailData
import bean.Response
import bean.UserPostData
import bean.post.PageData
import bean.post.PostItem
import bean.post.FirstCommentItem
import com.google.gson.Gson
import dao.CountDao
import dao.PostDao
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/PostServlet")
class PostServlet : HttpServlet() {
    @Throws(ServletException::class, IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html;charset=utf-8"
        request.characterEncoding = "utf-8"
        response.characterEncoding = "utf-8"

        val operation = request.getParameter(OPERATION)
        val dao = PostDao()
        var result : String? = null

        when(operation) {
            OPERATION_PUBLISH_POST -> { // 发表帖子
                val userId = request.getParameter(USER_ID).toInt()
                val text = request.getParameter(TEXT)
                val resp = Response<Int>()
                if (dao.insertPost(userId, text) == 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "发表失败"
                    resp.data = 0
                } else {
                    resp.result = RESULT_OK
                    resp.msg = "发表成功"
                    resp.data = 0
                }
                result = Gson().toJson(resp)
            }
            OPERATION_SELECT_POST -> { // 查询分页帖子
                val index = request.getParameter(INDEX).toInt()
                val resp = Response<PageData<PostItem>>()
                val data = dao.getPostPage(index)

                resp.result = RESULT_OK
                resp.msg = "查询帖子成功！"
                resp.data = data

                result = Gson().toJson(resp)
            }
            OPERATION_SELECT_COMMENT -> { // 查询评论  同时返回该贴是否收藏过  查询下一页数据的时候收藏数据就冗余了。。
                val userId = request.getParameter(USER_ID).toInt()
                val index = request.getParameter(INDEX).toInt()
                val postId = request.getParameter(POST_ID).toInt()
                val resp = Response<PostDetailData<FirstCommentItem>>()
                val data = dao.getFirstCommentPage(postId, index)
                var isCollected = false
                if(userId != -1) isCollected = (dao.getPostCollectStatus(userId, postId) == 1)

                resp.result = RESULT_OK
                resp.msg = "查询评论成功！"
                resp.data = PostDetailData(data.endIndex, data.data, isCollected)

                result = Gson().toJson(resp)
            }
            OPERATION_SEND_COMMENT -> {
                val userId = request.getParameter(USER_ID).toInt()
                val postId = request.getParameter(POST_ID).toInt()
                val text = request.getParameter(TEXT)
                val resp = Response<Int>()
                if (dao.insertFirstComment(userId,postId, text) == 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "评论失败"
                    resp.data = 0
                } else {
                    resp.result = RESULT_OK
                    resp.msg = "评论成功"
                    resp.data = 0
                }
                result = Gson().toJson(resp)
            }
            OPERATION_SELECT_USER_POST -> {
                val userId = request.getParameter(USER_ID).toInt()
                val index = request.getParameter(INDEX).toInt()
                val resp = Response<PageData<PostItem>>()
                val pageData = dao.getUserPostPage(userId, index)
       //         val countData = CountDao().getCountData(userId)

                resp.result = RESULT_OK
                resp.msg = "查询帖子成功！"
                resp.data = pageData

                result = Gson().toJson(resp)
            }
            OPERATION_COLLECT_POST -> {
                val userId = request.getParameter(USER_ID).toInt()
                val postId = request.getParameter(POST_ID).toInt()
                val row = dao.insertCollection(userId, postId)
                val resp = Response<Int>()
                if (row == 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "收藏失败"
                    resp.data = 0
                } else {
                    resp.result = RESULT_OK
                    resp.msg = "收藏成功"
                    resp.data = 0
                }
                result = Gson().toJson(resp)
            }
            OPERATION_DELETE_COLLECTION -> {
                val userId = request.getParameter(USER_ID).toInt()
                val postId = request.getParameter(POST_ID).toInt()
                val row = dao.deleteCollection(userId, postId)
                val resp = Response<Int>()
                if (row == 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "删除收藏失败"
                    resp.data = 0
                } else {
                    resp.result = RESULT_OK
                    resp.msg = "删除收藏成功"
                    resp.data = 0
                }
                result = Gson().toJson(resp)
            }
            OPERATION_SELECT_COLLECTION -> {
                val userId = request.getParameter(USER_ID).toInt()
                val index = request.getParameter(INDEX).toInt()
                val resp = Response<PageData<PostItem>>()
                val data = dao.getUserCollectPostPage(userId, index)

                resp.result = RESULT_OK
                resp.msg = "查询帖子成功！"
                resp.data = data

                result = Gson().toJson(resp)
            }
        }


        val out = response.writer
        out.write(result);
        out.close();
    }

    @Throws(ServletException::class, IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        doPost(request, response)
    }
}