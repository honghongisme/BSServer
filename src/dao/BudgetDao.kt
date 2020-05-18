package dao

import BaseDao
import Constants.FIELD_CLASSIFY
import Constants.FIELD_MONTH
import Constants.FIELD_USER_ID
import Constants.TABLE_BUDGET
import bean.Budget
import java.sql.SQLException

class BudgetDao : BaseDao() {

    fun insertBudget(budget: Budget) : Int {
        val sql = "insert into $TABLE_BUDGET values(? ,?, ?, ?)"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, budget.userId)
            preparedStatement.setString(2, budget.month)
            preparedStatement.setString(3, budget.classify)
            preparedStatement.setFloat(4, budget.budget)
            return preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }

    fun deleteBudget(budget: Budget) : Int {
        val sql = "delete from $TABLE_BUDGET where $FIELD_USER_ID = ? and $FIELD_MONTH = ? and $FIELD_CLASSIFY = ?"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, budget.userId)
            preparedStatement.setString(2, budget.month)
            preparedStatement.setString(3, budget.classify)
            return preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }
}