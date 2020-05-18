import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BaseDao {

    protected Connection connection = null;
    protected PreparedStatement preparedStatement = null;
    protected ResultSet resultSet = null;

    public BaseDao() {
        //获得数据库的连接对象
        connection = JDBCManager.Companion.getConnection();
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        JDBCManager.Companion.closeAll(connection, preparedStatement, resultSet);
    }

}
