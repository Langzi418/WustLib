package com.xuzhipeng.wustlib.db;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMMENT".
*/
public class CommentDao extends AbstractDao<Comment, Long> {

    public static final String TABLENAME = "COMMENT";

    /**
     * Properties of entity Comment.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Content = new Property(1, String.class, "content", false, "CONTENT");
        public final static Property Date = new Property(2, java.util.Date.class, "date", false, "DATE");
        public final static Property BookId = new Property(3, Long.class, "bookId", false, "BOOK_ID");
        public final static Property Username = new Property(4, String.class, "username", false, "USERNAME");
        public final static Property UserId = new Property(5, Long.class, "userId", false, "USER_ID");
    }

    private Query<Comment> book_CommentsQuery;
    private Query<Comment> user_CommentsQuery;

    public CommentDao(DaoConfig config) {
        super(config);
    }
    
    public CommentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMMENT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CONTENT\" TEXT," + // 1: content
                "\"DATE\" INTEGER," + // 2: date
                "\"BOOK_ID\" INTEGER," + // 3: bookId
                "\"USERNAME\" TEXT," + // 4: username
                "\"USER_ID\" INTEGER);"); // 5: userId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMMENT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Comment entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(2, content);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(3, date.getTime());
        }
 
        Long bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindLong(4, bookId);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(5, username);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(6, userId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Comment entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(2, content);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(3, date.getTime());
        }
 
        Long bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindLong(4, bookId);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(5, username);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(6, userId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Comment readEntity(Cursor cursor, int offset) {
        Comment entity = new Comment( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // content
            cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)), // date
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // bookId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // username
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // userId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Comment entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setContent(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDate(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
        entity.setBookId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setUsername(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUserId(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Comment entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Comment entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Comment entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "comments" to-many relationship of Book. */
    public List<Comment> _queryBook_Comments(Long bookId) {
        synchronized (this) {
            if (book_CommentsQuery == null) {
                QueryBuilder<Comment> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.BookId.eq(null));
                queryBuilder.orderRaw("T.'DATE' DESC");
                book_CommentsQuery = queryBuilder.build();
            }
        }
        Query<Comment> query = book_CommentsQuery.forCurrentThread();
        query.setParameter(0, bookId);
        return query.list();
    }

    /** Internal query to resolve the "comments" to-many relationship of User. */
    public List<Comment> _queryUser_Comments(Long userId) {
        synchronized (this) {
            if (user_CommentsQuery == null) {
                QueryBuilder<Comment> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.UserId.eq(null));
                queryBuilder.orderRaw("T.'DATE' DESC");
                user_CommentsQuery = queryBuilder.build();
            }
        }
        Query<Comment> query = user_CommentsQuery.forCurrentThread();
        query.setParameter(0, userId);
        return query.list();
    }

}
