package top.soyask.calendarii.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import top.soyask.calendarii.database.DBUtils;
import top.soyask.calendarii.domain.Event;

/**
 * Created by mxf on 2017/8/16.
 */
public class EventDao {
    public static final String EVENT = "EVENT";

    private DBUtils mDBUtils;
    private static EventDao mEventDao;

    public EventDao(DBUtils dBUtils) {
        this.mDBUtils = dBUtils;
    }

    public static EventDao getInstance(Context context) {
        if (mEventDao == null) {
            DBUtils dbUtils = DBUtils.getInstance(context);
            mEventDao = new EventDao(dbUtils);
        }
        return mEventDao;
    }

    public void add(Event event) {
        SQLiteDatabase database = mDBUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", event.getTitle());
        values.put("detail", event.getDetail());
        values.put("isDelete", event.isDelete());
        values.put("isComplete", event.isComplete());
        database.insert(EVENT, null, values);
    }

    public void update(Event event) {
        SQLiteDatabase database = mDBUtils.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", event.getTitle());
        values.put("detail", event.getDetail());
        values.put("isDelete", event.isDelete());
        values.put("isComplete", event.isComplete());
        database.update(EVENT, values, "id = ?", new String[]{String.valueOf(event.getId())});
    }

    public void delete(Event event) {
        SQLiteDatabase database = mDBUtils.getWritableDatabase();
        database.delete(EVENT, "id = ?", new String[]{String.valueOf(event.getId())});
    }

    public void deleteAll() {
        SQLiteDatabase database = mDBUtils.getWritableDatabase();
        database.delete(EVENT, null, null);
    }

    public void deleteComplete() {
        SQLiteDatabase database = mDBUtils.getWritableDatabase();
        database.delete(EVENT, "isComplete = ?", new String[]{String.valueOf(1)});
    }


    public List<Event> queryAll() {
        SQLiteDatabase database = mDBUtils.getReadableDatabase();
        Cursor cursor = database.query(EVENT, null, null, null, null, null, "title");
        List<Event> events = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String detail = cursor.getString(2);
            Event event = new Event(title, detail);
            event.setId(id);
            event.setDelete(cursor.getInt(3) == 1);
            event.setComplete(cursor.getInt(4) == 1);
            events.add(event);
        }

        return events;
    }


    public List<Event> query(String title) {
        SQLiteDatabase database = mDBUtils.getReadableDatabase();
        Cursor cursor = database.query(EVENT, null, "title = ?", new String[]{title}, null, null, "title");
        List<Event> events = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String detail = cursor.getString(2);
            Event event = new Event(title, detail);
            event.setId(id);
            event.setDelete(cursor.getInt(3) == 1);
            event.setComplete(cursor.getInt(4) == 1);
            events.add(event);
        }

        return events;
    }

    /**
     * 取消了回收站这个东西
     */
    @Deprecated
    public List<Event> query(boolean delete) {
        SQLiteDatabase database = mDBUtils.getReadableDatabase();
        Cursor cursor = database.query(EVENT, null, "isDelete = ?", new String[]{String.valueOf(delete)}, null, null, "title");
        List<Event> events = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String detail = cursor.getString(2);
            Event event = new Event(title, detail);
            event.setId(id);
            event.setDelete(cursor.getInt(3) == 1);
            event.setComplete(cursor.getInt(4) == 1);
            events.add(event);
        }

        return events;
    }
}