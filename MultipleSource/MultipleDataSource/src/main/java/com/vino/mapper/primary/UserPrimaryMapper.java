package com.vino.mapper.primary;

import com.vino.enums.UserSexEnum;
import com.vino.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserPrimaryMapper {

    @Select("SELECT * FROM user")
    @Results({
            @Result(property = "userSex", column = "userSex", javaType = UserSexEnum.class),
            @Result(property = "nickName", column = "nickName")
    })
    List<User> getAll();

    @Select("SELECT * FROM user WHERE userId = #{id}")
    @Results({
            @Result(property = "userSex", column = "userSex", javaType = UserSexEnum.class),
            @Result(property = "nickName", column = "nickName")
    })
    User getOne(Long id);

    @Insert("INSERT INTO user(userName,passWord,userSex) VALUES(#{userName}, #{passWord}, #{userSex})")
    void insert(User user);

    @Update("UPDATE user SET userName=#{userName},nickName=#{nickName} WHERE id =#{id}")
    void update(User user);

    @Delete("DELETE FROM user WHERE userId =#{id}")
    void delete(Long id);

}