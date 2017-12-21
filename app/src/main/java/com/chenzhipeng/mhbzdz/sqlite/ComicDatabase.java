package com.chenzhipeng.mhbzdz.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.comic.ComicDownloadBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ComicDatabase extends SQLiteOpenHelper {
    private static final int version = 1;
    private static final String DATABASE_NAME = "comicDatabase";
    private static volatile ComicDatabase database;

    /**
     * 漫画阅读历史记录表
     */
    private static final String TABLE_HISTORY = "comicHistoryRecord";

    /**
     * 漫画收藏记录表
     */
    private static final String TABLE_COLLECTION = "comicCollection";

    /**
     * 漫画搜索记录表
     */
    private static final String TABLE_SEARCH = "comicSearchRecord";


    /**
     * 漫画阅读历史记录
     */
    private static final String SQL_HISTORY = "create table " + TABLE_HISTORY
            + "(_id integer primary key autoincrement,"
            + "comicId txt,"
            + "comicName txt,"
            + "chapterName txt,"
            + "pictureUrl txt)";

    /**
     * 漫画收藏记录
     */
    private static final String SQL_COLLECTION = "create table " + TABLE_COLLECTION
            + "(_id integer primary key autoincrement,"
            + "comicId txt,"
            + "comicName txt)";

    /**
     * 漫画搜索记录
     */
    private static final String SQL_SEARCH = "create table " + TABLE_SEARCH
            + "(_id integer primary key autoincrement,"
            + "searchKey txt)";


    private static final String URL_SPLIT = "@";
    private static final String TABLE_DOWNLOAD_DATA = "downloadData";
    private static final String TABLE_DOWNLOAD_BOOK = "downloadName";
    private static final String SQL_DOWNLOAD_DATA = "create table " + TABLE_DOWNLOAD_DATA + "(_id integer primary key autoincrement,comicId txt,comicName txt,chapterName txt,urls txt)";
    private static final String SQL_DOWNLOAD_BOOK = "create table " + TABLE_DOWNLOAD_BOOK + "(_id integer primary key autoincrement,comicId txt,comicName txt)";


    private ComicDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static ComicDatabase getInstance() {
        if (database == null) {
            synchronized (ComicDatabase.class) {
                if (database == null) {
                    database = new ComicDatabase(BaseApplication.getContext(), DATABASE_NAME, null, version);
                }
            }
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_SEARCH);
        sqLiteDatabase.execSQL(SQL_HISTORY);
        sqLiteDatabase.execSQL(SQL_COLLECTION);
        sqLiteDatabase.execSQL(SQL_DOWNLOAD_DATA);
        sqLiteDatabase.execSQL(SQL_DOWNLOAD_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * 保存搜索记录
     */
    public void insertSearch(String searchKey) {
        if (!TextUtils.isEmpty(searchKey)) {
            Cursor cursor = getWritableDatabase().query(TABLE_SEARCH, null, "searchKey=?", new String[]{searchKey}, null, null, null);
            if (cursor != null && cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put("searchKey", searchKey);
                getWritableDatabase().insert(ComicDatabase.TABLE_SEARCH, null, values);
            }
            closeCursor(cursor);
        }
    }

    /**
     * 获取搜索记录
     */
    public List<String> getSearch() {
        List<String> strings = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(ComicDatabase.TABLE_SEARCH, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String searchKey = cursor.getString(cursor.getColumnIndex("searchKey"));
                strings.add(searchKey);
            } while (cursor.moveToNext());

        }
        closeCursor(cursor);
        return strings;
    }

    /**
     * 删除搜索记录
     */
    public void deleteSearch(String searchKey) {
        if (!TextUtils.isEmpty(searchKey)) {
            getWritableDatabase().delete(ComicDatabase.TABLE_SEARCH, "searchKey=?", new String[]{searchKey});
        }
    }


    /**
     * 保存我的收藏
     */
    public boolean insertCollection(String comicId, String comicName) {
        if (!EmptyUtils.isStringsEmpty(comicId, comicName)) {
            Cursor cursor = getReadableDatabase().query(TABLE_COLLECTION, null, "comicId=?", new String[]{comicId}, null, null, null);
            if (cursor != null && cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put("comicId", comicId);
                values.put("comicName", comicName);
                long insert = getWritableDatabase().insert(TABLE_COLLECTION, null, values);
                return insert > 0;
            }
            closeCursor(cursor);
        }
        return false;
    }

    /**
     * 删除我的收藏
     */
    public boolean deleteCollection(String comicId) {
        if (!TextUtils.isEmpty(comicId)) {
            int delete = getWritableDatabase().delete(ComicDatabase.TABLE_COLLECTION, "comicId=?", new String[]{comicId});
            return delete > 0;
        }
        return false;
    }

    public boolean isCollection(String comicId) {
        if (!TextUtils.isEmpty(comicId)) {
            Cursor cursor = getReadableDatabase().query(TABLE_COLLECTION, null, "comicId=?", new String[]{comicId}, null, null, null);
            if (cursor != null) {
                int count = cursor.getCount();
                closeCursor(cursor);
                return count > 0;
            }
        }
        return false;
    }

    /**
     * 获取我的收藏
     */
    public List<ComicItemBean> getCollection() {
        List<ComicItemBean> beanList = new ArrayList<>();
        Cursor cursor = getWritableDatabase().query(ComicDatabase.TABLE_COLLECTION, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String comicId = cursor.getString(cursor.getColumnIndex("comicId"));
                String comicName = cursor.getString(cursor.getColumnIndex("comicName"));
                ComicItemBean bean = new ComicItemBean(comicId, comicName, false, false);
                beanList.add(bean);
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return beanList;
    }

    /**
     * 保存 更改阅读记录
     */
    public void changeHistory(String comicId, String comicName, String chapterName, String pictureUrl) {
        if (!EmptyUtils.isStringsEmpty(comicId, comicName, chapterName, pictureUrl)) {
            Cursor cursor = getReadableDatabase().query(TABLE_HISTORY, null, "comicId=?", new String[]{comicId}, null, null, null);
            if (cursor != null && cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put("comicId", comicId);
                values.put("comicName", comicName);
                values.put("chapterName", chapterName);
                values.put("pictureUrl", pictureUrl);
                getWritableDatabase().insert(TABLE_HISTORY, null, values);
            } else {
                ContentValues values = new ContentValues();
                values.put("chapterName", chapterName);
                values.put("pictureUrl", pictureUrl);
                getWritableDatabase().update(TABLE_HISTORY, values, "comicId=?", new String[]{comicId});
            }
            closeCursor(cursor);
        }
    }

    /**
     * 获取历史记录
     */
    public String getHistoryPictureUrl(String comicId, String chapterName) {
        String pictureUrl = null;
        if (!EmptyUtils.isStringsEmpty(comicId, chapterName)) {
            Cursor cursor = getReadableDatabase().query(ComicDatabase.TABLE_HISTORY, null, "comicId=? and chapterName=?", new String[]{comicId, chapterName}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                pictureUrl = cursor.getString(cursor.getColumnIndex("pictureUrl"));
            }
            closeCursor(cursor);
        }
        return pictureUrl;
    }

    public String getHistoryChapterName(String comicId) {
        String chapterName = null;
        if (!TextUtils.isEmpty(comicId)) {
            Cursor cursor = getReadableDatabase().query(ComicDatabase.TABLE_HISTORY, null, "comicId=?", new String[]{comicId}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                chapterName = cursor.getString(cursor.getColumnIndex("chapterName"));
            }
            closeCursor(cursor);
        }
        return chapterName;
    }


    public List<ComicItemBean> getHistory() {
        List<ComicItemBean> comicItemBeanList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(ComicDatabase.TABLE_HISTORY, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String comicId = cursor.getString(cursor.getColumnIndex("comicId"));
                String comicName = cursor.getString(cursor.getColumnIndex("comicName"));
                ComicItemBean comicItemBean = new ComicItemBean(comicId, comicName, false, false);
                comicItemBeanList.add(comicItemBean);
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return comicItemBeanList;
    }

    public boolean deleteHistory(String comicId) {
        if (!TextUtils.isEmpty(comicId)) {
            int delete = getWritableDatabase().delete(TABLE_HISTORY, "comicId=?", new String[]{comicId});
            return delete > 0;
        }
        return false;
    }


    public boolean insertDownloadData(ComicDownloadBean comicDownloadBean) {
        if (comicDownloadBean != null) {
            Cursor cursor = getReadableDatabase().query(TABLE_DOWNLOAD_DATA, null, "comicId=? and chapterName=?", new String[]{comicDownloadBean.getComicId(), comicDownloadBean.getChapterName()}, null, null, null);
            if (cursor != null && cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put("comicId", comicDownloadBean.getComicId());
                values.put("comicName", comicDownloadBean.getComicName());
                values.put("chapterName", comicDownloadBean.getChapterName());
                //  values.put("state", comicDownloadBean.getState());
                StringBuilder builder = new StringBuilder();
                for (String url : comicDownloadBean.getUrls()) {
                    builder.append(url).append(URL_SPLIT);
                }
                values.put("urls", builder.toString());
                long insert = getWritableDatabase().insert(TABLE_DOWNLOAD_DATA, null, values);
                return insert > 0;
            }
            closeCursor(cursor);
        }
        return false;
    }


    public List<ComicDownloadBean> getDownloadData(String comicId) {
        List<ComicDownloadBean> comicDownloadBeanList = new ArrayList<>();
        if (!TextUtils.isEmpty(comicId)) {
            Cursor cursor = getReadableDatabase().query(TABLE_DOWNLOAD_DATA, null, "comicId=?", new String[]{comicId}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    String comicName = cursor.getString(cursor.getColumnIndex("comicName"));
                    String chapterName = cursor.getString(cursor.getColumnIndex("chapterName"));
                    String urls = cursor.getString(cursor.getColumnIndex("urls"));
                    String[] split = urls.split(URL_SPLIT);
                    String page = 0 + "/" + split.length;
                    ComicDownloadBean comicDownloadBean = new ComicDownloadBean(comicId, comicName, chapterName,
                            BaseApplication.getContext().getResources().getString(R.string.pause_download),
                            page, 0, split.length, false, false, false, Arrays.asList(split));
                    comicDownloadBeanList.add(comicDownloadBean);
                } while (cursor.moveToNext());
            }
            closeCursor(cursor);
        }
        return comicDownloadBeanList;
    }


    public boolean isExistDownloadData(String comicId, String chapterName) {
        if (!EmptyUtils.isStringsEmpty(comicId, chapterName)) {
            Cursor cursor = getReadableDatabase().query(TABLE_DOWNLOAD_DATA, null, "comicId=? and chapterName=?", new String[]{comicId, chapterName}, null, null, null);
            if (cursor != null) {
                int count = cursor.getCount();
                cursor.close();
                return count > 0;
            }
        }
        return false;
    }

    public boolean isExistDownloadData(String comicId) {
        if (!TextUtils.isEmpty(comicId)) {
            Cursor cursor = getReadableDatabase().query(TABLE_DOWNLOAD_DATA, null, "comicId=?", new String[]{comicId}, null, null, null);
            if (cursor != null) {
                int count = cursor.getCount();
                cursor.close();
                return count > 0;
            }
        }
        return false;
    }

    public boolean deleteDownloadData(String comicId, String chapterName) {
        if (!EmptyUtils.isStringsEmpty(comicId, chapterName)) {
            int delete = getReadableDatabase().delete(TABLE_DOWNLOAD_DATA, "comicId=? and chapterName=?", new String[]{comicId, chapterName});
            return delete > 0;
        }
        return false;
    }

    public boolean insertDownloadBook(String comicId, String comicName) {
        if (!EmptyUtils.isStringsEmpty(comicId, comicName)) {
            Cursor cursor = getReadableDatabase().query(TABLE_DOWNLOAD_BOOK, null, "comicId=?", new String[]{comicId}, null, null, null);
            if (cursor != null && cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put("comicId", comicId);
                values.put("comicName", comicName);
                long insert = getReadableDatabase().insert(TABLE_DOWNLOAD_BOOK, null, values);
                return insert > 0;
            }
            closeCursor(cursor);
        }
        return false;
    }

    public boolean deleteDownloadBook(String comicId) {
        if (!EmptyUtils.isStringsEmpty(comicId)) {
            Cursor query = getReadableDatabase().query(TABLE_DOWNLOAD_DATA, null, null, null, null, null, null);
            int nameDelete = getReadableDatabase().delete(TABLE_DOWNLOAD_BOOK, "comicId=?", new String[]{comicId});
            if (query != null && query.getCount() > 0) {
                int dataDelete = getReadableDatabase().delete(TABLE_DOWNLOAD_DATA, "comicId=?", new String[]{comicId});
                closeCursor(query);
                return nameDelete > 0 && dataDelete > 0;
            } else {
                closeCursor(query);
                return nameDelete > 0;
            }
        }
        return false;
    }

    public List<ComicItemBean> getDownloadBook() {
        List<ComicItemBean> comicItemBeanList = new ArrayList<>();
        Cursor cursor = getWritableDatabase().query(TABLE_DOWNLOAD_BOOK, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String comicId = cursor.getString(cursor.getColumnIndex("comicId"));
                String comicName = cursor.getString(cursor.getColumnIndex("comicName"));
                ComicItemBean comicItemBean = new ComicItemBean(comicId, comicName, false, false);
                comicItemBeanList.add(comicItemBean);
            } while (cursor.moveToNext());
        }
        closeCursor(cursor);
        return comicItemBeanList;
    }
}
