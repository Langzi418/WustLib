package com.xuzhipeng.wustlib.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/12
 * Desc:
 */

@Entity
public class Comment {

    @Id(autoincrement = true)
    private Long id;
    private String content;
    private Date date;
    private Long bookId;

    private String username;

    @Generated(hash = 477047191)
    public Comment(Long id, String content, Date date, Long bookId,
            String username) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.bookId = bookId;
        this.username = username;
    }

    @Generated(hash = 1669165771)
    public Comment() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date  getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getBookId() {
        return this.bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
