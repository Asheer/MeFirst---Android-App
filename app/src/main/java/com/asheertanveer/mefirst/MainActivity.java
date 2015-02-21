package com.asheertanveer.mefirst;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.util.Vector;


public class MainActivity extends FragmentActivity {

    private EditText username,password;
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = (Button) findViewById(R.id.button);

        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);

        username.setHint("CUNYfirst Username");
        password.setHint("CUNYfirst Password");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View view) {
                           if(isEmpty(username) || isEmpty(password)) {
                            return;
                        } else {
                               Intent intent = new Intent(MainActivity.this, ClassSchedule.class);
                               intent.putExtra("username", username.getText().toString());
                               intent.putExtra("password", password.getText().toString());
                               startActivity(intent);
                        }
            }
        });
    }

    public boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}