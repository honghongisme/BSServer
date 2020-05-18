package dao

import BaseDao
import Constants.FIELD_ACCOUNT
import Constants.FIELD_AMOUNT
import Constants.FIELD_BUDGET
import Constants.FIELD_CLASSIFY
import Constants.FIELD_DATE
import Constants.FIELD_MONTH
import Constants.FIELD_NOTE
import Constants.FIELD_PASSWORD
import Constants.FIELD_RECORD_ID
import Constants.FIELD_TYPE
import Constants.FIELD_USER_ID
import Constants.TABLE_BUDGET
import Constants.TABLE_RECORD
import Constants.TABLE_USER
import bean.Budget
import bean.Record
import java.sql.SQLException


class LoginDao : BaseDao() {


    /**
     * 插入user，返回自增id
     */
    fun insertUser(account : String, password : String) : Int {
        var sql = "insert into $TABLE_USER($FIELD_ACCOUNT, $FIELD_PASSWORD) values(? ,?)"
        var id = 0
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, account)
            preparedStatement.setString(2, password)
            preparedStatement.executeUpdate()
            sql = "select last_insert_id() from $TABLE_USER"
            preparedStatement = connection.prepareStatement(sql)
            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) id = resultSet.getInt(1)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return id
    }

    fun searchUser(account: String, password: String) : Int {
        val sql = "select $FIELD_USER_ID from $TABLE_USER where $FIELD_ACCOUNT = ? and $FIELD_PASSWORD = ? limit 1"
        var id = 0
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, account)
            preparedStatement.setString(2, password)
            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                id = resultSet.getInt(FIELD_USER_ID)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return id
    }

    fun searchRecord(userId : Int) : MutableList<Record> {
        val sql = "select * from $TABLE_RECORD where $FIELD_USER_ID = ?"
        val data = mutableListOf<Record>()
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, userId)
            resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                val id = resultSet.getInt(FIELD_RECORD_ID)
                val type = resultSet.getString(FIELD_TYPE)
                val classify = resultSet.getString(FIELD_CLASSIFY)
                val amount = resultSet.getFloat(FIELD_AMOUNT)
                val date = resultSet.getString(FIELD_DATE)
                val note = resultSet.getString(FIELD_NOTE)
                data.add(Record(id, userId, type, classify, amount, date, note))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return data
    }

    fun searchBudget(userId : Int) : MutableList<Budget> {
        val sql = "select * from $TABLE_BUDGET where $FIELD_USER_ID = ?"
        val data = mutableListOf<Budget>()
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, userId)
            resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                val classify = resultSet.getString(FIELD_CLASSIFY)
                val month = resultSet.getString(FIELD_MONTH)
                val budget = resultSet.getFloat(FIELD_BUDGET)
                data.add(Budget(userId, classify, month, budget))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return data
    }
}