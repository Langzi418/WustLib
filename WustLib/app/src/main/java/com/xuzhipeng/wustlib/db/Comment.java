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

    //表关联
    private Long bookId;
    private Long userId;
    @Generated(hash = 1165288817)
    public Comment(Long id, String content, Date date, Long bookId, Long userId) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.bookId = bookId;
        this.userId = userId;
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
    public Date getDate() {
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
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }


    //返回用户名
    public String getUsername(){
        User user =DBUtil.queryUserById(userId);
        if(user!=null){
            return user.getName();
        }

        return null;
    }
}
