import Constants.FIELD_ACCOUNT
import Constants.FIELD_AMOUNT
import Constants.FIELD_BUDGET
import Constants.FIELD_CLASSIFY
import Constants.FIELD_COMMENT_ID
import Constants.FIELD_CONTINUOUS_CLOCK_DAY_NUM
import Constants.FIELD_DATE
import Constants.FIELD_LAST_CLOCK_DATE
import Constants.FIELD_MONTH
import Constants.FIELD_NOTE
import Constants.FIELD_PASSWORD
import Constants.FIELD_POST_ID
import Constants.FIELD_RECORD_ID
import Constants.FIELD_TEXT
import Constants.FIELD_TOTAL_CHARGE_NUM
import Constants.FIELD_TYPE
import Constants.FIELD_USER_ID
import Constants.FIELD_TOTAL_CHARGE_DAY_NUM
import Constants.TABLE_BUDGET
import Constants.TABLE_COLLECTION
import Constants.TABLE_COUNT
import Constants.TABLE_FIRST_COMMENT
import Constants.TABLE_POST
import Constants.TABLE_RECORD
import Constants.TABLE_USER
import Constants.TRIGGER_COUNT_AND_USER
import java.sql.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.servlet.ServletConfig
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet

class JDBCManager : HttpServlet() {

    private var config //定义一个ServletConfig对象
            : ServletConfig? = null

    @Throws(ServletException::class)
    override fun init(config: ServletConfig) {
        super.init(config) //继承父类的init()方法
        this.config = config //获取配置信息
        username = config.getInitParameter("DBUsername") //获取数据库用户名
        password = config.getInitParameter("DBPassword") //获取数据库连接密码
        url = config.getInitParameter("ConnectionURL") //获取数据库连接URL

        createUserTable()
        createRecordTable()
        createBudgetTable()
        createPostTable()
        createFirstCommentTable()
        createCount()
        dropCountTableTrigger()
        createCountTableTrigger()
        createCollection()
    }

    private fun createUserTable() {
        val s = "$TABLE_USER($FIELD_USER_ID int auto_increment primary key, $FIELD_ACCOUNT varchar(20) not null, $FIELD_PASSWORD varchar(20) not null)"
        createTable(s)
    }

    private fun createRecordTable() {
        val s = "$TABLE_RECORD($FIELD_RECORD_ID int auto_increment primary key, $FIELD_USER_ID int, $FIELD_TYPE varchar(2), $FIELD_CLASSIFY text, $FIELD_AMOUNT float, $FIELD_DATE date, $FIELD_NOTE text)"
        createTable(s)
    }

    private fun createBudgetTable() {
        val s = "$TABLE_BUDGET($FIELD_USER_ID int, $FIELD_MONTH varchar(7), $FIELD_CLASSIFY varchar(2), $FIELD_BUDGET float)"
        createTable(s)
    }

    private fun createPostTable() {
        val s = "$TABLE_POST($FIELD_POST_ID int auto_increment primary key, $FIELD_USER_ID int, $FIELD_TEXT text, $FIELD_DATE varchar(19))"
        createTable(s)
    }

    private fun createFirstCommentTable() {
        val s = "$TABLE_FIRST_COMMENT($FIELD_COMMENT_ID int auto_increment primary key, $FIELD_USER_ID int, $FIELD_POST_ID int,  $FIELD_TEXT text, $FIELD_DATE varchar(19))"
        createTable(s)
    }

    private fun createCount() {
        val s = "$TABLE_COUNT($FIELD_USER_ID int primary key, $FIELD_LAST_CLOCK_DATE date, $FIELD_CONTINUOUS_CLOCK_DAY_NUM int, $FIELD_TOTAL_CHARGE_DAY_NUM int, $FIELD_TOTAL_CHARGE_NUM int)"
        createTable(s)
    }

    private fun createCollection() {
        val s = "$TABLE_COLLECTION($FIELD_USER_ID int, $FIELD_POST_ID int)"
        createTable(s)
    }

    private fun createTable(table : String) {
        val sql = "create table if not exists $table charset utf8mb4 collate utf8mb4_bin"
        try {
            val preparedStatement = connection?.prepareStatement(sql)
            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun dropCountTableTrigger() {
        val sql = "drop trigger if exists $TRIGGER_COUNT_AND_USER"
        try {
            val preparedStatement = connection?.prepareStatement(sql)
            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun createCountTableTrigger() {
        val sql = "create trigger $TRIGGER_COUNT_AND_USER after insert on $TABLE_USER for each row insert into $TABLE_COUNT($FIELD_USER_ID) values (new.$FIELD_USER_ID)"
        try {
            val preparedStatement = connection?.prepareStatement(sql)
            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }


    companion object {
        private var username //定义的数据库用户名
                : String? = null
        private var password //定义的数据库连接密码
                : String? = null
        private var url //定义数据库连接URL
                : String? = null

        /**
         * 获得数据库连接对象
         *
         * @return 数据库连接对象
         */
        var connection //定义连接
                : Connection? = null
            get() {
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance()
                    field = DriverManager.getConnection(url, username, password)
                } catch (ex: ClassNotFoundException) {
                    Logger.getLogger(JDBCManager::class.java.name).log(Level.SEVERE, null, ex)
                } catch (ex: InstantiationException) {
                    Logger.getLogger(JDBCManager::class.java.name).log(Level.SEVERE, null, ex)
                } catch (ex: IllegalAccessException) {
                    Logger.getLogger(JDBCManager::class.java.name).log(Level.SEVERE, null, ex)
                } catch (ex: SQLException) {
                    Logger.getLogger(JDBCManager::class.java.name).log(Level.SEVERE, null, ex)
                }
                return field
            }
            private set

        /**
         * 关闭所有的数据库连接资源
         *
         * @param connection Connection 链接
         * @param statement Statement 资源
         * @param resultSet ResultSet 结果集合
         */
        fun closeAll(connection: Connection?, statement: Statement?,
                     resultSet: ResultSet?) {
            try {
                resultSet?.close()
                statement?.close()
                connection?.close()
            } catch (ex: SQLException) {
                Logger.getLogger(JDBCManager::class.java.name).log(Level.SEVERE, null, ex)
            }
        }
    }
}