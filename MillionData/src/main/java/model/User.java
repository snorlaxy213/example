/**
 *
 */
package model;

import com.alibaba.excel.annotation.ExcelProperty;
import easyexcel.converter.LocalDateConverter;
import easyexcel.converter.LocalDateTimeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {

    @ExcelProperty(value = "用户ID", index = 0)
    private Integer userId;
    @ExcelProperty(value = "用户名", index = 1)
    private String username;
    @ExcelProperty(value = "昵称", index = 2)
    private String name;
    @ExcelProperty(value = "密码", index = 3)
    private String password;
    @ExcelProperty(value = "性别", index = 4)
    private String gender;
    @ExcelProperty(value = "生日", index = 5, converter = LocalDateConverter.class)
    private LocalDate birthday;
    @ExcelProperty(value = "创建时间", index = 6, converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;
    @ExcelProperty(value = "创建用户", index = 7)
    private String createUser;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
