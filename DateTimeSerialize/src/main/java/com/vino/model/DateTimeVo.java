package com.vino.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vino.CustomDateDeserialize;
import com.vino.CustomDateSerialize;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class DateTimeVo implements Serializable {

    private static final long serialVersionUID = 8607359499149763224L;

    //SpringMVC默认jackson类库来进行反序列化,并不触发@DateTimeFormat注解机制,所以需要使用jackson的格式化注解@JsonFormat
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    LocalDateTime localDateTime;


    //@JsonFormat 可以被@JsonDeserialize和@JsonSerialize 代替,但同时存在@JsonFormat覆盖其他注解
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = CustomDateSerialize.class) //序列化（后端往前端传值，CustomDateSerialize这里把日期转化为时间戳传回去）
    @JsonDeserialize(using = CustomDateDeserialize.class) //反序列化（前端往后端传值）
    Date date;

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DateTimeVo{" +
                "date=" + date +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
