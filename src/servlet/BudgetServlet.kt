package servlet

import Constants.BUDGET
import Constants.OPERATION
import Constants.OPERATION_ADD_BUDGET
import Constants.OPERATION_DELETE_BUDGET
import Constants.RESULT_ERROR
import Constants.RESULT_OK
import bean.Budget
import bean.Record
import bean.Response
import com.google.gson.Gson
import dao.BudgetDao
import dao.RecordDao
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/BudgetServlet")
class BudgetServlet : HttpServlet() {

    @Throws(ServletException::class, IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html;charset=utf-8"
        request.characterEncoding = "utf-8"
        response.characterEncoding = "utf-8"

        val operation = request.getParameter(OPERATION)
        val record = request.getParameter(BUDGET)
        val dao = BudgetDao()
        val resp = Response<Int>()
        val data = Gson().fromJson(record, Budget::class.java)
        when(operation) {
            OPERATION_ADD_BUDGET -> {
                dao.deleteBudget(data)
                val row = dao.insertBudget(data)
                if (row == 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "添加预算失败！服务器异常"
                    resp.data = 0
                } else {
                    resp.result = RESULT_OK
                    resp.msg = "添加预算成功！"
                    resp.data = 0
                }
            }
            OPERATION_DELETE_BUDGET -> {
                val row = dao.deleteBudget(data)
                if (row == 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "删除预算失败！服务器异常"
                    resp.data = 0
                } else {
                    resp.result = RESULT_OK
                    resp.msg = "删除预算成功！"
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