package servlet

import Constants.OPERATION
import Constants.OPERATION_ADD_RECORD
import Constants.OPERATION_DELETE_RECORD
import Constants.OPERATION_UPDATE_RECORD
import Constants.RECORD
import Constants.RESULT_ERROR
import Constants.RESULT_OK
import bean.Record
import bean.Response
import bean.login.LoginData
import com.google.gson.Gson
import dao.CountDao
import dao.RecordDao
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import kotlin.jvm.Throws
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/RecordServlet")
class RecordServlet : HttpServlet() {
    @Throws(ServletException::class, IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html;charset=utf-8"
        request.characterEncoding = "utf-8"
        response.characterEncoding = "utf-8"

        val operation = request.getParameter(OPERATION)
        val record = request.getParameter(RECORD)
        val dao = RecordDao()
        val resp = Response<Int>()
        val data = Gson().fromJson(record, Record::class.java)
        when(operation) {
            OPERATION_ADD_RECORD -> {
                val id = dao.insertRecord(data)
                if (id == 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "插入失败！"
                    resp.data = 0
                } else {
                    CountDao().updateCountOnCharging(data.userId)
                    resp.result = RESULT_OK
                    resp.msg = "插入成功！"
                    resp.data = id
                }
            }
            OPERATION_UPDATE_RECORD -> {
                val row = dao.updateRecord(data)
                if (row == 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "更新失败！"
                    resp.data = 0
                } else {
                    resp.result = RESULT_OK
                    resp.msg = "更新成功！"
                    resp.data = 0
                }
            }
            OPERATION_DELETE_RECORD -> {
                val row = dao.deleteRecord(data)
                if (row == 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "删除失败！"
                    resp.data = 0
                } else {
                    resp.result = RESULT_OK
                    resp.msg = "删除成功！"
                    resp.data = 0
                }
            }
        }
        val result = Gson().toJson(resp)
        val out = response.writer
        out.write(result);
        out.close();
    }

    @Throws(ServletException::class, IOException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        doPost(request, response)
    }
}