/**
 * Copyright (C) 2015-2016 Neva ventures .
 */
package com.example.anandmjoseph.myapplication.DatabaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Database helper class Created by nano coders Created on 03/01/2017.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    public static final String DATABASE_NAME = "nanob2c.db";
    private static final int DATABASE_VERSION = 1;


    /**
     * Database Helper
     *
     * @param context Context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
           // TableUtils.createTable(connectionSource, WeatherCodes.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        deleteAndRecreateTables(db, connectionSource);
    }

    private void deleteAndRecreateTables(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
           // TableUtils.dropTable(connectionSource, WeatherCodes.class, true);

            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        //weatherCodesDao = null;

    }
}