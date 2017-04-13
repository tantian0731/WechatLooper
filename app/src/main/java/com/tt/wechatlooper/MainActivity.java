package com.tt.wechatlooper;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tt.wechatlooper.util.AdbUtil;
import com.tt.wechatlooper.util.sp.SPUtil;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Timer t;
    private Button bt_set;
    private TextView tv_current, tv_switch, tv_version;
    private Switch mSwitch;
    private EditText et_set;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setTimeToTextView(SPUtil.getSpeedFactor());
        setSwitch(SPUtil.getSwitchState());

        //BETA注：指定时间内秒内 不设置好，天成大魔王将在后台做一些羞羞的事情了！
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                beginLoop();
            }
        }, 1000 * 15);
    }


    private void initView() {
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText(getVersion());
        bt_set = (Button) findViewById(R.id.bt_set);
        tv_current = (TextView) findViewById(R.id.tv_current);
        et_set = (EditText) findViewById(R.id.et_set);
        et_set.setText(SPUtil.getSpeedFactor() + "");
        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (t != null) {
                    t.cancel();
                }

                String etStr = et_set.getText().toString();
                if (TextUtils.isEmpty(etStr)) {
                    Toast.makeText(MainActivity.this, "速率必须大于0", Toast.LENGTH_SHORT).show();
                }

                float time = Float.valueOf(etStr);
                if (time == 0) {
                    Toast.makeText(MainActivity.this, "速率必须大于0", Toast.LENGTH_SHORT).show();
                } else {
                    saveData(time);
                    setTimeToTextView(time);
                    beginLoop();

                }
            }
        });

        tv_switch = (TextView) findViewById(R.id.tv_switch);
        mSwitch = (Switch) findViewById(R.id.switch_log);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtil.setSwitchState(isChecked);
                setSwitchText(isChecked);

            }
        });

        initPermission();

    }


    private void beginLoop() {
        //最新的方式 如果没有开启辅助功能，
        int count = 0;
        while (!checkAccessibilityPermission() && count < 5) {
            //打开辅助功能
            requestPermission();
            //点JF微信小工具  400*480分辨率
            sleep(2);
            AdbUtil.tap(190, 130);
            sleep(2);
            //点右上角开关
            AdbUtil.tap(330, 50);
            sleep(2);
            //对话框点确定
            AdbUtil.tap(290, 375);
            sleep(2);
            //点返回键 2下
            AdbUtil.back();
            sleep(2);
            AdbUtil.back();
            sleep(2);
            count++;
        }

//        打开微信
        startWechat();
    }

    private void initPermission() {
        //请求权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 2);
            //判断是否需要 向用户解释，为什么要申请该权限
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS);
        }
    }

    private void saveData(float speedFactor) {
        SPUtil.putFloat(SPUtil.SPEED_FACTOR, speedFactor);
    }


    private void setTimeToTextView(float time) {
        tv_current.setText("当前速度为初始速度的1/" + time);
    }

    private void setSwitchText(boolean switchState) {
        if (switchState) {
            tv_switch.setText("打印日志-开启");
        } else {
            tv_switch.setText("打印日志-关闭");
        }
    }

    private void setSwitch(boolean switchState) {
        setSwitchText(switchState);
        mSwitch.setChecked(switchState);
    }

    private void startWechat() {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "请安装微信", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermission() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);

    }

    private boolean checkAccessibilityPermission() {
        final String service = getPackageName() + "/" + LoopService.class.getCanonicalName();
        int accessibilityEnabled = 0;

        try {
            accessibilityEnabled = Settings.Secure.getInt(getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null && settingValue.contains(service)) {
                return true;
            }
        }

        return false;
    }

    private String getVersion() {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "未知版本";
    }

    private void sleep(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
