package com.app.darren.logapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.darren.loglibs.ToolLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_MSG = "ToolLog is a so cool Log Tool!";
    private static final String TAG = "ToolLog";
    private static final String URL_XML = "https://raw.githubusercontent.com/3642072/LogApp/master/app/src/main/AndroidManifest.xml";
    private static String XML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><!--  Copyright w3school.com.cn --><note><to>George</to><from>John</from><heading>Reminder</heading><body>Don't forget the meeting!</body></note>";
    private static String JSON;
    private static String JSON_LONG;
    private static String STRING_LONG;
    private AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        httpClient = new AsyncHttpClient();
        JSON_LONG = getResources().getString(R.string.json_long);
        JSON = getResources().getString(R.string.json);
        STRING_LONG = getString(R.string.string_long);
        if (Build.VERSION.SDK_INT >= 23) {
            int hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (123 == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "请允许读写权限！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void log(View view) {
        ToolLog.v();
        ToolLog.d();
        ToolLog.i();
        ToolLog.w();
        ToolLog.e();
    }

    public void logWithMsg(View view) {
        ToolLog.v(LOG_MSG);
        ToolLog.d(LOG_MSG);
        ToolLog.i(LOG_MSG);
        ToolLog.w(LOG_MSG);
        ToolLog.e(LOG_MSG);
    }

    public void logWithTag(View view) {
        ToolLog.v(TAG, LOG_MSG);
        ToolLog.d(TAG, LOG_MSG);
        ToolLog.i(TAG, LOG_MSG);
        ToolLog.w(TAG, LOG_MSG);
        ToolLog.e(TAG, LOG_MSG);
    }

    public void logWithLong(View view) {
        ToolLog.d(TAG, STRING_LONG);
    }

    public void logWithParams(View view) {
        ToolLog.v(TAG, LOG_MSG, "params1", "params2", this);
        ToolLog.d(TAG, LOG_MSG, "params1", "params2", this);
        ToolLog.i(TAG, LOG_MSG, "params1", "params2", this);
        ToolLog.w(TAG, LOG_MSG, "params1", "params2", this);
        ToolLog.e(TAG, LOG_MSG, "params1", "params2", this);
    }

    public void logWithNull(View view) {
        ToolLog.v(null);
        ToolLog.d(null);
        ToolLog.i(null);
        ToolLog.w(null);
        ToolLog.e(null);
    }

    public void logWithJson(View view) {
        ToolLog.json("12345");
        ToolLog.json(null);
        ToolLog.json(JSON);
    }

    public void logWithLongJson(View view) {
        ToolLog.json(JSON_LONG);
    }

    public void logWithJsonTag(View view) {
        ToolLog.json(TAG, JSON);
    }

    public void logWithFile(View view) {
        ToolLog.file(Environment.getExternalStoragePublicDirectory("LogApp"), JSON_LONG);
        ToolLog.file(TAG, Environment.getExternalStoragePublicDirectory("LogApp"), JSON_LONG);
        ToolLog.file(TAG, Environment.getExternalStoragePublicDirectory("LogApp"), "test.txt", JSON_LONG);
    }

    public void logWithXml(View view) {
        ToolLog.xml("12345");
        ToolLog.xml(null);
        ToolLog.xml(XML);
    }

    public void logWithXmlFromNet(View view) {
        httpClient.get(this, URL_XML, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ToolLog.e(responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ToolLog.xml(responseString);
            }
        });
    }

    public void onExceprionMethd(View view) {
        try {
            throw new RuntimeException("test throw Runtime Exception!");
        } catch (Exception e) {
            ToolLog.e(TAG, e);
            ToolLog.log(e);
            ToolLog.log(TAG, e);
        }
    }

    public void onGetLocalLogFileDir(View view) {
        Toast.makeText(this, ToolLog.getLocalLogFile().getPath() + ":exists=" + ToolLog.getLocalLogFile().exists(), Toast.LENGTH_SHORT).show();
    }

    public void onGetLocalLogBackupFileDir(View view) {
        Toast.makeText(this, ToolLog.getLocalLogBackupFile().getPath() + ":exists=" + ToolLog.getLocalLogBackupFile().exists(), Toast.LENGTH_SHORT).show();
    }

    public void sendLog(View view) {
        startActivity(new Intent(this, DebugFeedbackActivity.class));
    }

    ///////////////////////////////////////////////////////////////////////////
    // MENU
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_github:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/3642072/LogApp")));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
