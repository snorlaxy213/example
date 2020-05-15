package com.vino.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class DateTimeVo implements Serializable {

    private static final long serialVersionUID = 8607359499149763224L;

    Date date;

    LocalDateTime localDateTime;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return "DateTimeVo{" +
                "date=" + date +
                ", localDateTime=" + localDateTime +
                '}';
    }
}
