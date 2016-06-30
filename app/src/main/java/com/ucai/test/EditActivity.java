package com.ucai.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ucai.test.control.DeviceControlActivity;

public class EditActivity extends AppCompatActivity {

    private EditText et_name;
    private static EditText et_height;
    private static EditText et_age;
    private RadioGroup rg_sex;
    private RadioButton rb_man, rb_woman;
    private Button btn_start;
    private String name;
    private int sex = 1;
    private static int age;//年龄
    private static int height;//身高


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getActionBar().hide();
        setContentView(R.layout.activity_edit);
        initView();

        //对RadioGroup实现监听
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_man:
                        Toast.makeText(EditActivity.this, "选择——男", Toast.LENGTH_SHORT).show();
                        sex = 1;
                        break;
                    case R.id.rb_woman:
                        Toast.makeText(EditActivity.this, "选择——女", Toast.LENGTH_SHORT).show();
                        sex = 0;
                        break;
                }
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().trim().length() == 0) {
                    et_name.setError("请输入姓名");
                    return;// 不再继续往下运行，返回当前操作
                }
                if (et_height.getText().toString().trim().length() == 0) {
                    et_height.setError("请输入身高");
                    return;// 不再继续往下运行，返回当前操作
                }
                //
                if (et_age.getText().toString().trim().length() == 0) {
                    et_age.setError("请输入年龄");
                    return;// 不再继续往下运行，返回当前操作
                }
                name = et_name.getText().toString().trim();


                Intent intentToControl = new Intent(EditActivity.this, DeviceControlActivity.class);
                intentToControl.putExtra("sex", sex);
                Log.d("/se", sex + "-->");
                startActivity(intentToControl);
                Toast.makeText(EditActivity.this, et_name.getText().toString().trim() + "您好，欢迎体验", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    //采用静态方法传值，因为Intent传值，经过二次跳转，值会丢失
    public static int toHeight() {
        if (et_height.getText().toString().trim() != null) {
            height = Integer.parseInt(et_height.getText().toString().trim());
        }
        return height;
    }

    public static int toAge() {
        if (et_age.getText().toString().trim() != null) {
            age = Integer.parseInt(et_age.getText().toString().trim());
        }
        return age;
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_height = (EditText) findViewById(R.id.et_height);
        et_age = (EditText) findViewById(R.id.et_age);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rb_woman = (RadioButton) findViewById(R.id.rb_woman);
        btn_start = (Button) findViewById(R.id.btn_start);
    }
}
