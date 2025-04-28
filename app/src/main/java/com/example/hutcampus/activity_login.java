package com.example.hutcampus;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity_login extends AppCompatActivity {
    //定义登录Button 编辑框
    private Button btn_login;
    private EditText et_password,et_userName;
    /*定义数据库所需成员变量 */
    private DBOpenHelper dbOpenHelper;
    //数据库里存储的password
    String dbpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        //初始化
        initView();
        //注册完之后更新

        /*定义数据库对象 */
        dbOpenHelper = new DBOpenHelper(activity_login.this, "user.db", null, 1);

        /*点击跳转至注册页面 【点击注册】按钮*/
        findViewById(R.id.register).setOnClickListener(view -> {
            Intent intent = new Intent(activity_login.this, activity_register.class);
            startActivity(intent);
        });

        //登录按钮单击事件
        btn_login =  findViewById(R.id.login);
        btn_login.setOnClickListener(view -> {
            //获取输入的密码框内容
            String etpassword = et_password.getText().toString();
            /*获取数据库里的数据*/
            //登录按钮获取要查询的账号
            String key = et_userName.getText().toString();
            Cursor cursor = dbOpenHelper.getReadableDatabase().query("user", null, "username = ?", new String[]{key}, null, null, null);
            //创建ArrayList对象，用于保存用户数据结果
            ArrayList<Map<String, String>> resultList = new ArrayList<Map<String, String>>();//不用测试的话，直接遍历取值getstring（2）就行，创建数组可以省去。
            while (cursor.moveToNext()) {
                //将结果集中的数据存入HashMap
                Map<String, String> map = new HashMap<>();
                //取出查询结果第二列和第三列的值
                //用户名
                map.put("username", cursor.getString(1));
                //密码
                map.put("password", cursor.getString(2));
                resultList.add(map);

                //获取数据库中符合用户名的对应的密码
                dbpassword = map.get("password");
            }
            //正则化判断输入的账号是否符合手机号格式
            if (!isTelPhoneNumber(key)&&!isEmail(key)) {
                Toast.makeText(activity_login.this, "请输入正确的手机号或邮箱！", Toast.LENGTH_SHORT).show();
            } else if (resultList == null || resultList.size() == 0) { //如果数据库中没有查询的用户数据
                //显示提示信息，没有相关记录
                Toast.makeText(activity_login.this,
                        "该用户名未注册，请先注册", Toast.LENGTH_LONG).show();
            }
            else {
                //查到了用户 对比输入的密码与数据库的密码是否一致 如果相等跳转到主页面去
                if (etpassword.equals(dbpassword)) {
                    Toast.makeText(activity_login.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                    //跳转到Mainactivity
                    Intent intent = new Intent(activity_login.this, MainActivity.class);
                    startActivity(intent);
                    //关闭登录页面
                    activity_login.this.finish();
                } else {
                    Toast.makeText(activity_login.this, "密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
            ;
        });
    }
        /*正则化验证手机号码*/
        public boolean isTelPhoneNumber(String mobile) {
            if (mobile != null && mobile.length() == 11) {
                Pattern pattern = Pattern.compile("^1[3|4|5|6|7|8|9][0-9]\\d{8}$");
                Matcher matcher = pattern.matcher(mobile);
                return matcher.matches();
            }else{
                return false;
            }
        }
    public boolean isEmail(String email) {
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

        //定义初始化
        private void initView(){
            btn_login=findViewById(R.id.login);
            et_userName=findViewById(R.id.et_username);
            et_password=findViewById(R.id.et_password);
        }

//    //重写onDestroy()方法
        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (dbOpenHelper != null) {
                dbOpenHelper.close();
            }
        }

}
