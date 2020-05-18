package dao

import BaseDao
import Constants.DATE_FORMAT_PATTERN
import Constants.FIELD_ACCOUNT
import Constants.FIELD_COMMENT_ID
import Constants.FIELD_DATE
import Constants.FIELD_POST_ID
import Constants.FIELD_TEXT
import Constants.FIELD_USER_ID
import Constants.FIRST_COMMENT_PAGE_SIZE
import Constants.POST_PAGE_SIZE
import Constants.TABLE_COLLECTION
import Constants.TABLE_FIRST_COMMENT
import Constants.TABLE_POST
import Constants.TABLE_USER
import Constants.TEMPORARY_TABLE_NAME_A
import bean.post.PageData
import bean.post.PostItem
import bean.post.FirstCommentItem
import java.sql.SQLException
import java.util.*

class PostDao: BaseDao() {


    public fun insertPost(userId : Int, text : String) : Int {
        val sql = "insert into $TABLE_POST($FIELD_USER_ID, $FIELD_TEXT, $FIELD_DATE) values(? ,?, ?)"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, userId)
            preparedStatement.setString(2, text)
            preparedStatement.setString(3, DateUtil.getDate(DATE_FORMAT_PATTERN, Date()))
            return preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }

    public fun getPostPage(index : Int) :PageData<PostItem> {
        var i = 0
        if (index == -1) { // -1表示第一次请求page
            i = getPostResultSetCount();
            if (i == 0) return PageData(-1, arrayOfNulls<PostItem>(0))
        } else {
            i = index
        }
        var pageSize = POST_PAGE_SIZE
        var startIndex = i - POST_PAGE_SIZE
        if (startIndex < 0) {
            startIndex = 0
            pageSize = i
        }
        val array = arrayOfNulls<PostItem>(pageSize)
        var j = pageSize - 1
        val sql = "select ${TABLE_USER}.${FIELD_USER_ID}" +
                ", ${TABLE_USER}.${FIELD_ACCOUNT}" +
                ", ${TEMPORARY_TABLE_NAME_A}.${FIELD_POST_ID}" +
                ", ${TEMPORARY_TABLE_NAME_A}.${FIELD_TEXT}" +
                ", ${TEMPORARY_TABLE_NAME_A}.${FIELD_DATE} " +
                "from (select ${TABLE_POST}.${FIELD_POST_ID}" +
                ", ${TABLE_POST}.${FIELD_USER_ID}" +
                ", ${TABLE_POST}.${FIELD_TEXT}" +
                ", ${TABLE_POST}.${FIELD_DATE} from $TABLE_POST limit $startIndex, $pageSize) as $TEMPORARY_TABLE_NAME_A " +
                "left join $TABLE_USER on ${TABLE_USER}.${FIELD_USER_ID} = ${TEMPORARY_TABLE_NAME_A}.${FIELD_USER_ID} "
        preparedStatement = connection.prepareStatement(sql)
        resultSet = preparedStatement.executeQuery()
        while (resultSet.next()) {
            val userId = resultSet.getInt(FIELD_USER_ID)
            val postId = resultSet.getInt(FIELD_POST_ID)
            val userAccount = resultSet.getString(FIELD_ACCOUNT)
            val text = resultSet.getString(FIELD_TEXT)
            val date = resultSet.getString(FIELD_DATE)
            val postItem = PostItem(userId, postId, userAccount, text, DateUtil.getDateDistance(date))
            array[j] = postItem
            j--
        }
        return PageData(startIndex, array)
    }

    public fun getUserPostPage(userId: Int, index : Int) :PageData<PostItem> {
        var i = 0
        if (index == -1) { // -1表示第一次请求page
            i = getUserPostResultSetCount(userId);
            if (i == 0) return PageData(-1, arrayOfNulls<PostItem>(0))
        } else {
            i = index
        }
        var pageSize = POST_PAGE_SIZE
        var startIndex = i - POST_PAGE_SIZE
        if (startIndex < 0) {
            startIndex = 0
            pageSize = i
        }
        val array = arrayOfNulls<PostItem>(pageSize)
        var j = pageSize - 1
        val sql = "select ${TABLE_USER}.${FIELD_USER_ID}, ${TABLE_USER}.${FIELD_ACCOUNT}, ${TEMPORARY_TABLE_NAME_A}.${FIELD_POST_ID}, ${TEMPORARY_TABLE_NAME_A}.${FIELD_TEXT}, ${TEMPORARY_TABLE_NAME_A}.${FIELD_DATE} " +
                "from (select ${TABLE_POST}.${FIELD_POST_ID}, ${TABLE_POST}.${FIELD_USER_ID}, ${TABLE_POST}.${FIELD_TEXT}, ${TABLE_POST}.${FIELD_DATE} from $TABLE_POST where $FIELD_USER_ID = ? limit $startIndex, $pageSize) as $TEMPORARY_TABLE_NAME_A " +
                "left join $TABLE_USER on ${TABLE_USER}.${FIELD_USER_ID} = ${TEMPORARY_TABLE_NAME_A}.${FIELD_USER_ID} "
        preparedStatement = connection.prepareStatement(sql)
        preparedStatement.setInt(1, userId)
        resultSet = preparedStatement.executeQuery()
        while (resultSet.next()) {
            val userId = resultSet.getInt(FIELD_USER_ID)
            val postId = resultSet.getInt(FIELD_POST_ID)
            val userAccount = resultSet.getString(FIELD_ACCOUNT)
            val text = resultSet.getString(FIELD_TEXT)
            val date = resultSet.getString(FIELD_DATE)
            val postItem = PostItem(userId, postId, userAccount, text, date)
            array[j] = postItem
            j--
        }
        return PageData(startIndex, array)
    }

    private fun getUserCollectedPostResultSetCount(userId : Int) : Int {
        return getResultSetCount(TABLE_COLLECTION, "where $FIELD_USER_ID = $userId")
    }

    private fun getUserPostResultSetCount(userId : Int) : Int {
        return getResultSetCount(TABLE_POST, "where $FIELD_USER_ID = $userId")
    }

    private fun getFirstCommentResultSetCount(postId : Int) : Int {
        return getResultSetCount(TABLE_FIRST_COMMENT, "where $FIELD_POST_ID = $postId")
    }

    public fun getFirstCommentPage(postId : Int, index : Int) :PageData<FirstCommentItem> {
        var i = 0
        if (index == -1) { // -1表示第一次请求page
            i = getFirstCommentResultSetCount(postId);
            if (i == 0) return PageData(-1, arrayOfNulls<FirstCommentItem>(0))
        } else {
            i = index
        }
        var pageSize = FIRST_COMMENT_PAGE_SIZE
        var startIndex = i - FIRST_COMMENT_PAGE_SIZE
        if (startIndex < 0) {
            startIndex = 0
            pageSize = i
        }
        val array = arrayOfNulls<FirstCommentItem>(pageSize)
        var j = pageSize - 1
        val sql = "select ${TABLE_USER}.${FIELD_USER_ID}, ${TABLE_USER}.${FIELD_ACCOUNT}, ${TEMPORARY_TABLE_NAME_A}.${FIELD_COMMENT_ID}, ${TEMPORARY_TABLE_NAME_A}.${FIELD_TEXT}, ${TEMPORARY_TABLE_NAME_A}.${FIELD_DATE} " +
                "from (select ${TABLE_FIRST_COMMENT}.${FIELD_COMMENT_ID}, ${TABLE_FIRST_COMMENT}.${FIELD_USER_ID}, ${TABLE_FIRST_COMMENT}.${FIELD_TEXT}, ${TABLE_FIRST_COMMENT}.${FIELD_DATE} from $TABLE_FIRST_COMMENT where ${TABLE_FIRST_COMMENT}.${FIELD_POST_ID} = ? limit $startIndex, $pageSize) as $TEMPORARY_TABLE_NAME_A " +
                "left join $TABLE_USER on ${TABLE_USER}.${FIELD_USER_ID} = ${TEMPORARY_TABLE_NAME_A}.${FIELD_USER_ID} "
        preparedStatement = connection.prepareStatement(sql)
        preparedStatement.setInt(1, postId)
        resultSet = preparedStatement.executeQuery()
        while (resultSet.next()) {
            val userId = resultSet.getInt(FIELD_USER_ID)
            val commentId = resultSet.getInt(FIELD_COMMENT_ID)
            val userAccount = resultSet.getString(FIELD_ACCOUNT)
            val text = resultSet.getString(FIELD_TEXT)
            val date = resultSet.getString(FIELD_DATE)
            val item = FirstCommentItem(userId, commentId, userAccount, text, date)
            array[j] = item
            j--
        }
        return PageData(startIndex, array)
    }

    private fun getPostResultSetCount() : Int {
        return getResultSetCount(TABLE_POST)
    }

    private fun getResultSetCount(tableName : String, whereCondition : String = "") : Int {
        val sql = "select count(1) from $tableName $whereCondition"
        var count = 0
        try {
            preparedStatement = connection.prepareStatement(sql)
            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) count = resultSet.getInt(1)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return count
    }

    public fun insertFirstComment(userId: Int, postId: Int, text: String) : Int {
        val sql = "insert into $TABLE_FIRST_COMMENT($FIELD_USER_ID, $FIELD_TEXT, $FIELD_DATE, $FIELD_POST_ID) values(? ,?, ?, ?)"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, userId)
            preparedStatement.setString(2, text)
            preparedStatement.setString(3, DateUtil.getDate(DATE_FORMAT_PATTERN, Date()))
            preparedStatement.setInt(4, postId)
            return preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }

    /*********************************************** 收藏帖子功能  ************************************************************/
    public fun insertCollection(userId: Int, postId: Int) : Int {
        val sql = "insert into $TABLE_COLLECTION values(? ,?)"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, userId)
            preparedStatement.setInt(2, postId)
            return preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }

    public fun deleteCollection(userId: Int, postId: Int) : Int {
        val sql = "delete from $TABLE_COLLECTION where $FIELD_USER_ID = ? and $FIELD_POST_ID = ? limit 1"
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, userId)
            preparedStatement.setInt(2, postId)
            return preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }

    public fun getPostCollectStatus(userId: Int, postId: Int) : Int {
        val sql = "select count(1) from $TABLE_COLLECTION where $FIELD_USER_ID = ? and $FIELD_POST_ID = ? limit 1"
        var count = 0
        try {
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, userId)
            preparedStatement.setInt(2, postId)
            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) count = resultSet.getInt(1)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return count
    }

    public fun getUserCollectPostPage(userId: Int, index: Int) : PageData<PostItem> {
        var i = 0
        if (index == -1) { // -1表示第一次请求page
            i = getUserCollectedPostResultSetCount(userId);
            if (i == 0) return PageData(-1, arrayOfNulls<PostItem>(0))
        } else {
            i = index
        }
        var pageSize = POST_PAGE_SIZE
        var startIndex = i - POST_PAGE_SIZE
        if (startIndex < 0) {
            startIndex = 0
            pageSize = i
        }
        val array = arrayOfNulls<PostItem>(pageSize)
        var j = pageSize - 1
        val sql = "select ${TABLE_USER}.${FIELD_USER_ID}, ${TABLE_USER}.${FIELD_ACCOUNT}, ${TEMPORARY_TABLE_NAME_A}.${FIELD_POST_ID}, ${FIELD_TEXT}, ${FIELD_DATE} " +
                "from ((select ${TABLE_COLLECTION}.${FIELD_POST_ID}, ${TABLE_COLLECTION}.${FIELD_USER_ID} from $TABLE_COLLECTION where $FIELD_USER_ID = ? limit $startIndex, $pageSize) as $TEMPORARY_TABLE_NAME_A " +
                "left join $TABLE_POST on ${TABLE_POST}.${FIELD_POST_ID} = ${TEMPORARY_TABLE_NAME_A}.${FIELD_POST_ID}) " +
                "left join $TABLE_USER on ${TABLE_USER}.${FIELD_USER_ID} = ${TABLE_POST}.${FIELD_USER_ID} "
        preparedStatement = connection.prepareStatement(sql)
        preparedStatement.setInt(1, userId)
        resultSet = preparedStatement.executeQuery()
        while (resultSet.next()) {
            val userId = resultSet.getInt(FIELD_USER_ID)
            val postId = resultSet.getInt(FIELD_POST_ID)
            val userAccount = resultSet.getString(FIELD_ACCOUNT)
            val text = resultSet.getString(FIELD_TEXT)
            val date = resultSet.getString(FIELD_DATE)
            val postItem = PostItem(userId, postId, userAccount, text, date)
            array[j] = postItem
            j--
        }
        return PageData(startIndex, array)
    }
}