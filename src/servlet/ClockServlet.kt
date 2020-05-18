package servlet

import Constants.OPERATION
import Constants.OPERATION_CLOCK
import Constants.OPERATION_SELECT_CLOCK
import Constants.RESULT_ERROR
import Constants.RESULT_OK
import Constants.USER_ID
import bean.CountData
import bean.Response
import com.google.gson.Gson
import dao.CountDao
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import kotlin.jvm.Throws
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/ClockServlet")
class ClockServlet : HttpServlet() {
    @Throws(ServletException::class, IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html;charset=utf-8"
        request.characterEncoding = "utf-8"
        response.characterEncoding = "utf-8"

        val operation = request.getParameter(OPERATION)
        val userId = request.getParameter(USER_ID).toInt()

        val dao = CountDao()
        var result : String? = null
        when(operation) {
            OPERATION_CLOCK -> {
                val resp = Response<Int>()
                val row = dao.updateCountOnClocking(userId)
                if (row == 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "打卡失败"
                    resp.data = 0
                } else {
                    resp.result = RESULT_OK
                    resp.msg = "打卡成功"
                    resp.data = 0
                }
                result = Gson().toJson(resp)
            }
            OPERATION_SELECT_CLOCK -> {
                val resp = Response<CountData>()
                val data = dao.getCountData(userId)

                resp.result = RESULT_OK
                resp.msg = "查询打卡成功"
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