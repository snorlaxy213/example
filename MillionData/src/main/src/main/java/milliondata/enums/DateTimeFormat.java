package milliondata.enums;

/**
 * @className DateFormatUtil
 * @description 时间格式ENUM
 * @author Chen Jiaying
 * @date 2021/10/29
 **/
public enum DateTimeFormat {

    /**
     * 时间格式
     */
    YYYY("yyyy"),
    YYYY_MM("yyyy-MM"),
    YYYY_MM_DD("yyyy-MM-dd"),
    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    MM_DD_HH_MM("MM-dd HH:mm");

    private final String dateTimeFormat;

    DateTimeFormat(String dateFormat) {
        this.dateTimeFormat = dateFormat;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }
}
