package com.vino.mapper.master;

import com.vino.enums.UserSexEnum;
import com.vino.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MasterMapper {
    @Select("SELECT * FROM user")
    @Results({
            @Result(property = "userSex", column = "userSex", javaType = UserSexEnum.class),
            @Result(property = "nickName", column = "nickName")
    })
    public List<User> getAll();

    @Select("SELECT * FROM user WHERE id = #{id}")
    @Results({
            @Result(property = "userSex", column = "userSex", javaType = UserSexEnum.class),
            @Result(property = "nickName", column = "nickName")
    })
    public User getOne(Long id);

    @Insert("INSERT INTO user(userName,passWord,userSex) VALUES(#{userName}, #{passWord}, #{userSex})")
    public void insert(User user);

    @Update("UPDATE user SET userName=#{userName},nick_name=#{nickName} WHERE id =#{id}")
    public void update(User user);

    @Delete("DELETE FROM user WHERE id =#{id}")
    public void delete(Long id);
}