package dao

import BaseDao
import Constants.FIELD_CONTINUOUS_CLOCK_DAY_NUM
import Constants.FIELD_LAST_CLOCK_DATE
import Constants.FIELD_TOTAL_CHARGE_NUM
import Constants.FIELD_USER_ID
import Constants.FIELD_TOTAL_CHARGE_DAY_NUM
import Constants.TABLE_COUNT
import bean.CountData
import java.sql.SQLException

class CountDao : BaseDao() {

    // 记账时更新
    public fun updateCountOnCharging(userId : Int) : Int {
        val today = DateUtil.getCurrentDate("yyyy-MM-dd")
        val sql = "update $TABLE_COUNT set $FIELD_TOTAL_CHARGE_NUM = $FIELD_TOTAL_CHARGE_NUM + 1" +
                ", $FIELD_CONTINUOUS_CLOCK_DAY_NUM = case when $FIELD_LAST_CLOCK_DATE = '$today' then $FIELD_CONTINUOUS_CLOCK_DAY_NUM when datediff('$today', $FIELD_LAST_CLOCK_DATE) = 1 then $FIELD_CONTINUOUS_CLOCK_DAY_NUM + 1 else 1 end " +
                ", $FIELD_TOTAL_CHARGE_DAY_NUM = (IF($FIELD_LAST_CLOCK_DATE = '$today', $FIELD_TOTAL_CHARGE_DAY_NUM, $FIELD_TOTAL_CHARGE_DAY_NUM + 1))" +
                ", $FIELD_LAST_CLOCK_DATE = '$today' where $FIELD_USER_ID = ?"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, userId)
            return preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }

    // 打卡时更新
    public fun  updateCountOnClocking(userId : Int) : Int {
        val today = DateUtil.getCurrentDate("yyyy-MM-dd")
        val sql = "update $TABLE_COUNT set $FIELD_CONTINUOUS_CLOCK_DAY_NUM = case when $FIELD_LAST_CLOCK_DATE = '$today' then $FIELD_CONTINUOUS_CLOCK_DAY_NUM when datediff('$today', $FIELD_LAST_CLOCK_DATE) = 1 then ($FIELD_CONTINUOUS_CLOCK_DAY_NUM + 1) else 1 end " +
                ", $FIELD_LAST_CLOCK_DATE = '$today'" +
                " where $FIELD_USER_ID = ?"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, userId)
            return preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }


    public fun getCountData(userId: Int) : CountData {
        val sql = "select $FIELD_LAST_CLOCK_DATE, $FIELD_CONTINUOUS_CLOCK_DAY_NUM, $FIELD_TOTAL_CHARGE_DAY_NUM, $FIELD_TOTAL_CHARGE_NUM from $TABLE_COUNT where $FIELD_USER_ID = ? limit 1"
        preparedStatement = connection.prepareStatement(sql)
        preparedStatement.setInt(1, userId)
        resultSet = preparedStatement.executeQuery()
        var countData : CountData? = null
        if (resultSet.next()) {
            val lastClockDate = resultSet.getString(FIELD_LAST_CLOCK_DATE)
            var isTodayClock = (lastClockDate == DateUtil.getCurrentDate("yyyy-MM-dd"))
            countData = CountData(resultSet.getInt(FIELD_CONTINUOUS_CLOCK_DAY_NUM), resultSet.getInt(FIELD_TOTAL_CHARGE_DAY_NUM), resultSet.getInt(FIELD_TOTAL_CHARGE_NUM), isTodayClock)
        }
        return countData!!
    }

}