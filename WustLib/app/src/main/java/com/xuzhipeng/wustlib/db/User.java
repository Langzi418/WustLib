package com.xuzhipeng.wustlib.db;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/14
 * Desc:
 */

@Entity
public class User {

    private static final String TAG = "User";

    @Id(autoincrement = true)
    private Long Id;

    @Unique
    private String stuId;
    private String name;


    
    @ToMany(referencedJoinProperty = "userId")
    @OrderBy("category ASC")
    private List<Book> books;

    @ToMany(referencedJoinProperty = "userId")
    @OrderBy("date DESC")
    private List<Comment> comments;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    @Generated(hash = 2085563445)
    public User(Long Id, String stuId, String name) {
        this.Id = Id;
        this.stuId = stuId;
        this.name = name;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getStuId() {
        return this.stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 172339346)
    public List<Book> getBooks() {
        if (books == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BookDao targetDao = daoSession.getBookDao();
            List<Book> booksNew = targetDao._queryUser_Books(Id);
            synchronized (this) {
                if (books == null) {
                    books = booksNew;
                }
            }
        }
        return books;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 353255226)
    public synchronized void resetBooks() {
        books = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1541110468)
    public List<Comment> getComments() {
        if (comments == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CommentDao targetDao = daoSession.getCommentDao();
            List<Comment> commentsNew = targetDao._queryUser_Comments(Id);
            synchronized (this) {
                if (comments == null) {
                    comments = commentsNew;
                }
            }
        }
        return comments;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 249603048)
    public synchronized void resetComments() {
        comments = null;
    }



}

