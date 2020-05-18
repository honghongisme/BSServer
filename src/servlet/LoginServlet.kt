package servlet

import Constants.ACCOUNT
import Constants.OPERATION_LOGIN
import Constants.OPERATION
import Constants.PASSWORD
import Constants.OPERATION_REGISTER
import Constants.OPERATION_SELECT_USER_POST
import Constants.RESULT_ERROR
import Constants.RESULT_OK
import bean.Response
import bean.login.LoginData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dao.LoginDao
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet("/LoginServlet")
class LoginServlet : HttpServlet() {
    @Throws(ServletException::class, IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html;charset=utf-8"
        request.characterEncoding = "utf-8"
        response.characterEncoding = "utf-8"

        val operation = request.getParameter(OPERATION)
        val account = request.getParameter(ACCOUNT)
        val password = request.getParameter(PASSWORD)

        val dao = LoginDao()
        var result : String? = null
        when(operation) {
            OPERATION_LOGIN -> {
                val id = dao.searchUser(account, password)
                val resp = Response<LoginData>()
                if (id == 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "用户不存在！"
                    resp.data = null
                } else {
                    resp.result = RESULT_OK
                    resp.msg = "登录成功！"
                    resp.data = LoginData(id, dao.searchBudget(id), dao.searchRecord(id))
                }
                result = Gson().toJson(resp)
            }
            OPERATION_REGISTER -> {
                var id = dao.searchUser(account, password)
                val resp = Response<Int>()
                if (id != 0) {
                    resp.result = RESULT_ERROR
                    resp.msg = "用户已存在！"
                    resp.data = null
                } else {
                    id = dao.insertUser(account, password)
                    if (id == 0) {
                        resp.result = RESULT_ERROR
                        resp.msg = "未知错误"
                        resp.data = null
                    } else {
                        resp.result = RESULT_OK
                        resp.msg = "注册成功！"
                        resp.data = id
                    }
                }
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