package com.llzg;

import android.app.Application;

import com.llzg.sqlite.database.DatabaseManager;

public class MyMockApplication extends Application {

    @Override
    public void onCreate() {
       // do something important for your tests here
        try {
            new DbInitTest().fillDataToDb();
        } catch (DatabaseManager.DatabaseException e) {
            e.printStackTrace();
        }
    }
}