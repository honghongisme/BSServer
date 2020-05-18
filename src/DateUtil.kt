import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun getCurrentDate(pattern: String) : String {
        return getDate(pattern, Date())
    }

    fun getDate(pattern: String, date: Date) : String {
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

    fun getCurrentYear() : Int {
        val cal = Calendar.getInstance()
        cal.time = Date()
        return cal.get(Calendar.YEAR)
    }

    fun getCurrentMonth() : Int {
        val cal = Calendar.getInstance()
        cal.time = Date()
        return cal.get(Calendar.MONTH) + 1
    }

    /**
     * 把 yyyy年MM月dd日 格式的时间转换为 yyyy-MM-dd 格式
     */
    fun getStandardDateStr(date : String) : String? {
        var format = SimpleDateFormat("yyyy年MM月dd日")
        return try {
            val d = format.parse(date)
            format = SimpleDateFormat("yyyy-MM-dd")
               format.format(d)
        } catch (e : ParseException) {
            println("日期解析错误！返回当前日期")
            format.format(Date())
        }
    }

    /**
     * 获取指定时间和当前时间的时间差
     * @param date
     * @return 小于12小时返回相差时间，否则返回日期
     */
    fun getDateDistance(date: String): String {
        val distance: Long = Date().time - getMills(date)
        val minutes = distance / (1000 * 60)
        if (minutes <= 12 * 60) {
            val hours = minutes / 60
            if (hours < 1) {
                return "${minutes.toInt()}分钟前"
            } else {
                return "${hours.toInt()}小时前"
            }
        } else {
            return date
        }
    }

    private fun getMills(date: String): Long {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var d: Date? = null
        try {
            d = df.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return d!!.time
    }

}