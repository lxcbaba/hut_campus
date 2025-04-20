package com.example.hutcampus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_splash extends AppCompatActivity {

    // 启动页显示时间（毫秒）
    private static final int SPLASH_DISPLAY_TIME = 3000; // 3秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 使用Handler延迟跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 跳转到主页面
                Intent mainIntent = new Intent(activity_splash.this, MainActivity.class);
                startActivity(mainIntent);
                finish(); // 关闭当前Activity，避免返回时再次看到启动页
            }
        }, SPLASH_DISPLAY_TIME);
    }
}