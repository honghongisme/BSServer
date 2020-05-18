package bean.login

import bean.Budget
import bean.Record

data class LoginData(var id : Int,
                     var budgets: MutableList<Budget>,
                     var records: MutableList<Record>)