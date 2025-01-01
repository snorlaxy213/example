/**
 *
 */
package milliondata.model;

import com.alibaba.excel.annotation.ExcelProperty;
import milliondata.easyexcel.converter.LocalDateConverter;
import milliondata.easyexcel.converter.LocalDateTimeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    
    @ExcelProperty(value = "用户ID", index = 0)
    private String userId;
    @ExcelProperty(value = "用户编码", index = 1)
    private String userCode;
    @ExcelProperty(value = "用户名", index = 2)
    private String username;
    @ExcelProperty(value = "性别", index = 3)
    private String gender;
    @ExcelProperty(value = "生日", index = 4, converter = LocalDateConverter.class)
    private LocalDate birthday;
    @ExcelProperty(value = "创建时间", index = 5, converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserCode() {
        return userCode;
    }
    
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public LocalDate getBirthday() {
        return birthday;
    }
    
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
}
