package felix.codeQualityInsight.common;

/**
 * 自定义错误码
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),

    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    LOCAL_REPO_OPEN_ERROR(50002,"无法打开本地仓库"),
    LOCAL_REPO_ERROR(50003,"本地仓库错误"),
    REPOSITORIES_CONNECT_ERROR(50004, "无法连接到远程仓库"),
    SCRIPT_ERROR(50005, "脚本执行错误"),
    REPOSITORIES_ERROR(50006,"无法更新本地仓库");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
