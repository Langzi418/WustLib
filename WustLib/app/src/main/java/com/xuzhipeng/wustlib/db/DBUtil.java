package com.xuzhipeng.wustlib.db;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/13
 * Desc: 数据库操作类
 */

public class DBUtil {
    private static BookDao bookDao;
    private static UserDao userDao;
    private static CommentDao commentDao;

    private static final String TAG = "DBUtil";

    private static BookDao getBookDao() {
        if (bookDao == null) {
            bookDao = DaoManager.getInstance().getBookDao();
        }

        return bookDao;
    }

    private static UserDao getUserDao() {
        if (userDao == null) {
            userDao = DaoManager.getInstance().getUserDao();
        }

        return userDao;
    }


    private static CommentDao getCommentDao() {
        if (commentDao == null) {
            commentDao = DaoManager.getInstance().getCommentDao();
        }

        return commentDao;
    }

    /**
     * 关闭数据库
     */
    public static void closeDB() {
        DaoManager.getInstance().closeConnection();
    }


    public static long insertUser(User user) {
        return getUserDao().insert(user);
    }


    public static User queryUserById(Long userId) {
        return getUserDao().loadByRowId(userId);
    }

    public static User queryUserByStuNo(String userNo) {
        QueryBuilder<User> builder = getUserDao().queryBuilder();
        return builder.where(UserDao.Properties.StuId.eq(userNo)).build().unique();
    }



    /**
     * 根据isbn userId 找到某本书
     */
    public static Book queryBookIfExist(String isbn, long userId) {
        QueryBuilder<Book> builder = getBookDao().queryBuilder();
        return builder
                .where(BookDao.Properties.Isbn.eq(isbn),
                        BookDao.Properties.UserId.eq(userId))
                .build().unique();
    }

    public static List<Book> queryBookByIsbn(String isbn) {
        QueryBuilder<Book> builder = getBookDao().queryBuilder();
        return builder.where(BookDao.Properties.Isbn.eq(isbn)).build().list();
    }

    public static Long insertBook(Book book) {
        return getBookDao().insert(book);
    }

    public static void updateBook(Book book){
        getBookDao().update(book);
    }

    public static  void unlikeBook(Book book){
        book.setLike(false);
        book.setUserId(-1L);
        updateBook(book);
    }

    // 评论相关
    public static Long insertComment(Comment comment) {
        return getCommentDao().insert(comment);
    }


}
