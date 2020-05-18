package dao

import BaseDao
import Constants.FIELD_AMOUNT
import Constants.FIELD_CLASSIFY
import Constants.FIELD_DATE
import Constants.FIELD_NOTE
import Constants.FIELD_RECORD_ID
import Constants.FIELD_TYPE
import Constants.FIELD_USER_ID
import Constants.TABLE_RECORD
import bean.Record
import java.sql.SQLException

class RecordDao : BaseDao() {

    fun insertRecord(record : Record) : Int {
        var sql = "insert into $TABLE_RECORD($FIELD_USER_ID, $FIELD_TYPE, $FIELD_CLASSIFY, $FIELD_AMOUNT, $FIELD_DATE, $FIELD_NOTE) values(? ,?, ?, ?, ?, ?)"
        var id = 0
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, record.userId)
            preparedStatement.setString(2, record.type)
            preparedStatement.setString(3, record.classify)
            preparedStatement.setFloat(4, record.amount)
            preparedStatement.setString(5, record.date)
            preparedStatement.setString(6, record.note)
            preparedStatement.executeUpdate()
            sql = "select last_insert_id() from $TABLE_RECORD"
            preparedStatement = connection.prepareStatement(sql)
            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) id = resultSet.getInt(1)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return id
    }

    fun updateRecord(record: Record) : Int {
        val sql = "update $TABLE_RECORD set $FIELD_TYPE = ?, $FIELD_CLASSIFY = ?, $FIELD_AMOUNT = ?, $FIELD_DATE = ?, $FIELD_NOTE = ? where $FIELD_RECORD_ID = ?"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, record.type)
            preparedStatement.setString(2, record.classify)
            preparedStatement.setFloat(3, record.amount)
            preparedStatement.setString(4, record.date)
            preparedStatement.setString(5, record.note)
            preparedStatement.setInt(6, record.id)
            return preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }

    fun deleteRecord(record: Record) : Int {
        val sql = "delete from $TABLE_RECORD where $FIELD_RECORD_ID = ?"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, record.id)
            return preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }
}