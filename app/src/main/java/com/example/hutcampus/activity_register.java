package com.example.hutcampus;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity_register extends AppCompatActivity {
    public static final int RESULT_CODE_REGISTER=0;
    private Button btn_register;
    private EditText et_register_username,et_register_password,et_again_password;
    /*数据库成员变量*/
    private DBOpenHelper dbOpenHelper;

    String et_name;
    String et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        //注册按钮
        btn_register= findViewById(R.id.bregister);
        //用户名编辑框
        et_register_username= findViewById(R.id.et_username);
        //密码编辑框
        et_register_password=findViewById(R.id.et_password);
        //再次输入密码编辑框
        et_again_password=findViewById(R.id.et_passwords);

        /*实例化数据库变量dbOpenHelper*/
        dbOpenHelper=new DBOpenHelper(activity_register.this,"user.db",null,1);
        //点击返回退回到登陆页面
        findViewById(R.id.back).setOnClickListener(view -> {
            activity_register.this.finish();
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取三个编辑框的内容
                String et_name=et_register_username.getText().toString();
                String et_password=et_register_password.getText().toString();
                String et_confirm=et_again_password.getText().toString();

                //判断异常情况弹窗
                //编辑框为空
                if(TextUtils.isEmpty(et_name)){
                    Toast.makeText(activity_register.this,"用户名不能为空！",Toast.LENGTH_SHORT).show();
                    //对用户名进行手机号正则化验证，调用下面写的idTelPhoneNumber方法
                }
                else if (!isEmail(et_name)&&!isTelPhoneNumber(et_name)){
                    Toast.makeText(activity_register.this,"请输入正确的手机号或邮箱！",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(et_password)){
                    Toast.makeText(activity_register.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                    //两次密码框内容不一致
                }
                else if(!TextUtils.equals(et_password,et_confirm)){
                    Toast.makeText(activity_register.this,"密码不一致！",Toast.LENGTH_SHORT).show();
                }
                else{
                    //存储注册的用户名和密码 把账号密码存储进数据库
                    insertData(dbOpenHelper.getReadableDatabase(),et_name,et_password);
                    Toast.makeText(activity_register.this,"注册成功！",Toast.LENGTH_SHORT).show();
                    //关闭注册页面 跳转到登录页面
                    activity_register.this.finish();
                }

            }
        });
    }
    /*正则化验证手机号码方法*/
    public  boolean isTelPhoneNumber(String mobile) {
        if (mobile != null && mobile.length() == 11) {
            Pattern pattern = Pattern.compile("^1[3|4|5|6|7|8|9][0-9]\\d{8}$");
            Matcher matcher = pattern.matcher(mobile);
            return matcher.matches();
        }else{
            return false;
        }
    }
    public  boolean isEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // 邮箱正则表达式，支持大多数常见格式
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    //创建数据库的insert方法 插入数据方法
    private void insertData(SQLiteDatabase readableDatabase, String username1, String password1){
        ContentValues values=new ContentValues();
        values.put("username",username1);
        values.put("password",password1);
        readableDatabase.insert("user",null,values);
    }
    //重写onDestroy()方法
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbOpenHelper != null) {
            dbOpenHelper.close();
        }
    }
}
