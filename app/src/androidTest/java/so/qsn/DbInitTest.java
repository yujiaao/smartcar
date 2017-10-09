package com.llzg;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;

import com.llzg.sqlite.database.DatabaseManager;

import org.hqu.indoor_pos.bean.RoomInfo;
import org.hqu.indoor_pos.rmi.RoomManage;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbc.mobile.news.v3.common.CommonManager;
import bbc.mobile.news.v3.common.util.BBCLog;

/**
 * Created by xingwx on 17-2-20.
 */
@RunWith(AndroidJUnit4.class)
public class DbInitTest{


    private static final String TAG = DbInitTest.class.getSimpleName();

    @Test
    public void fillDataToDb() throws DatabaseManager.DatabaseException{

        try {
            DatabaseManager dm = DatabaseManager.get();
            String[] sql = {
                    "delete from room where room_id='2'",
                    "INSERT INTO `room` VALUES ('2', '1105',X'',66)",
                    "delete from room where room_id='2'"
            };
            dm.run(sql);
        }catch (Exception e){
            BBCLog.d(TAG, "error:", e);
            throw e;
        }


    }
}
