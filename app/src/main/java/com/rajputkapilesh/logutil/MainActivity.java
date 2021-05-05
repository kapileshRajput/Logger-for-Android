package com.rajputkapilesh.logutil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rajputkapilesh.loggerutil.LoggerUtil;

import java.security.spec.ECField;

public class MainActivity extends AppCompatActivity {
    TextView tvTest;
    Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTest = findViewById(R.id.tvTest);
        btnTest = findViewById(R.id.btnTest);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateError();
            }
        });

    }

    private void generateError() {
        try {
            int result = 1 / 0;
            tvTest.setText(String.valueOf(result));

            LoggerUtil.log(MainActivity.this, String.valueOf(result));

        }catch (Exception e){
            tvTest.setText(e.getMessage());

            LoggerUtil.log(MainActivity.this, e.getMessage());
        }
    }
}