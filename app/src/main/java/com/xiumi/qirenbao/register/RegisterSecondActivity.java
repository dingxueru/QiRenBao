package com.xiumi.qirenbao.register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.utils.StringUtils;
import com.xiumi.qirenbao.utils.ValitateUtils;
import com.xiumi.qirenbao.widget.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianbailu on 2016/01/21.
 * 注册页
 */

public class RegisterSecondActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.next)
    TextView next;
    @Bind(R.id.et_phone)
    EditText phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second);
        ButterKnife.bind(this);
        setStatusBar();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(getResources().getColor(R.color.title_bg));
    }
    @OnClick(R.id.next)
    public void nextPage(View view){
        keywordMiss();
        if(valitate()){
            int chose=getIntent().getIntExtra("chose",0);
            String name=getIntent().getStringExtra("name");
            Intent intent = new Intent(RegisterSecondActivity.this,RegisterThirdActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("chose",chose);
            intent.putExtra("mobile",phone.getText().toString());
            Log.e("chose",chose+name+phone.getText().toString());

            startActivity(intent);
        }
    }

    @OnClick(R.id.regist)
    public void regist(View view){
        keywordMiss();
    }
    /**
     * 校验手机号是否正确填写
     * @return
     */
    private boolean valitate() {
        String mobileStr=phone.getText().toString();
        //20160426 modi by qianbailu end
        if (StringUtils.isEmpty(mobileStr)) {

            new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.v_error_empty_phone))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();

            return false;
        }
        if (!ValitateUtils.validate(mobileStr, ValitateUtils.PHONE_FORMAT)) {
            new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.v_error_format_phone))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setCancelable(false)
                    .show();

            return false;
        }
        return true;
    }
    /**
     * 键盘收回
     */
    private void keywordMiss(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(phone.getWindowToken(), 0);
    }
}
