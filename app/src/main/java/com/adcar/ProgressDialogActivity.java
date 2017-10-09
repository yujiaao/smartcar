package com.adcar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ProgressDialogActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressdialog);

        progress("下载数据中", "请稍等......");


    }

    public void progress(String title, String message) {
        progressDialog = new ProgressDialog(this);
        updateThread thread = new updateThread();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(message);
        progressDialog.setTitle(title);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        thread.start();

    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what >= 100) {
                progressDialog.cancel();
            }
            progressDialog.setProgress(msg.what);
            super.handleMessage(msg);
        }

    };

    class updateThread extends Thread {
        public void run() {
            handler.sendEmptyMessage(0);
            try {
                updateThread.sleep(1000);
                handler.sendEmptyMessage(10);
                updateThread.sleep(1000);
                handler.sendEmptyMessage(20);
                updateThread.sleep(1000);
                handler.sendEmptyMessage(30);
                updateThread.sleep(1000);
                handler.sendEmptyMessage(40);
                updateThread.sleep(1000);
                handler.sendEmptyMessage(50);
                updateThread.sleep(1000);
                handler.sendEmptyMessage(60);
                updateThread.sleep(1000);
                handler.sendEmptyMessage(70);
                updateThread.sleep(1000);
                handler.sendEmptyMessage(80);
                updateThread.sleep(1000);
                handler.sendEmptyMessage(100);
                updateThread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    ;
}