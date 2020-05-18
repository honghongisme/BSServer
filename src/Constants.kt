object Constants {


    // record table
    const val TABLE_RECORD = "record"
    const val FIELD_RECORD_ID = "recordId"
    const val FIELD_USER_ID = "userId"
    const val FIELD_TYPE = "type"
    const val FIELD_CLASSIFY = "classify"
    const val FIELD_AMOUNT = "amount"
    const val FIELD_DATE = "date"
    const val FIELD_NOTE = "note"

    // user table
    const val TABLE_USER = "custom_user"
    const val FIELD_PASSWORD = "password"
    const val FIELD_ACCOUNT = "account"

    // budget table
    const val TABLE_BUDGET = "budget"
    const val FIELD_BUDGET = "budget"
    const val FIELD_MONTH = "month"

    // post table
    const val TABLE_POST = "post"
    const val FIELD_POST_ID = "postId"
    const val FIELD_TEXT = "text"

    // one level comment table
    const val TABLE_FIRST_COMMENT = "first_comment"
    const val FIELD_COMMENT_ID = "commentId"

    // count table
    const val TABLE_COUNT = "custom_count"
    const val FIELD_LAST_CLOCK_DATE = "lastClockDate" // 最后打卡的日期(打卡和记账都算打卡)
    const val FIELD_CONTINUOUS_CLOCK_DAY_NUM = "continuousClockDayNum" // 已连续打卡多少天
    const val FIELD_TOTAL_CHARGE_DAY_NUM = "totalChargeDayNum" // 记账总天数
    const val FIELD_TOTAL_CHARGE_NUM = "totalChargeNum" // 记账总笔数

    // count_user trigger
    const val TRIGGER_COUNT_AND_USER = "count_user_trigger"

    // custom table
    const val TABLE_COLLECTION = "custom_collection"





    // login and register
    // request
    const val OPERATION = "operation"
    const val OPERATION_LOGIN = "login"
    const val OPERATION_REGISTER = "register"
    const val OPERATION_ADD_RECORD = "addRecord"
    const val OPERATION_DELETE_RECORD = "deleteRecord"
    const val OPERATION_UPDATE_RECORD = "updateRecord"
    const val OPERATION_ADD_BUDGET = "addBudget"
    const val OPERATION_DELETE_BUDGET = "deleteBudget"
    const val OPERATION_PUBLISH_POST = "publishPost"
    const val OPERATION_SELECT_POST = "selectPost"
    const val OPERATION_SELECT_COMMENT = "selectComment"
    const val OPERATION_SEND_COMMENT = "sendComment"
    const val OPERATION_SELECT_USER_POST = "selectUserPost"
    const val OPERATION_CLOCK = "clock" // 打卡
    const val OPERATION_SELECT_CLOCK = "selectClock" // 我的 查询打卡
    const val OPERATION_COLLECT_POST = "collectPost"
    const val OPERATION_SELECT_COLLECTION = "selectCollection"
    const val OPERATION_DELETE_COLLECTION = "deleteCollection"


    const val ACCOUNT = "account"
    const val PASSWORD = "password"
    const val RECORD = "record"
    const val BUDGET = "budget"
    const val USER_ID = "userId"
    const val TEXT = "text"
    const val INDEX = "index"
    const val POST_ID = "postId"

    const val RESULT = "result"
    const val RESULT_OK = "ok"
    const val RESULT_ERROR = "error"
    const val MESSAGE = "msg"
    const val DATA = "data"


    const val DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss"
    const val POST_PAGE_SIZE = 5
    const val FIRST_COMMENT_PAGE_SIZE = 3
    
    const val TEMPORARY_FIELD_NAME_NUM = "t_num"
    const val TEMPORARY_TABLE_NAME_A = "t_a"
    const val TEMPORARY_TABLE_NAME_B = "t_b"
}