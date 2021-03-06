package sse309.bupt.fence.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import sse309.bupt.fence.R;
import sse309.bupt.fence.bean.Fence;
import sse309.bupt.fence.communication.RequestHelper;

public class LoginActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    private static final int RC_CAMERA_AND_WIFI = 123;
    private Button bt_login;
    private Button bt_signup;
    private Button bt_algorithm;
    private EditText username;
    private EditText password;

    public interface onLoginListenner {
        void onLoginSucceed();
        void onLoginFailed();
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        //检查并请求权限
        checkPermission();
        //Date date = new Date();
        //Log.i("sun",date.getTime()+"");
        jumpToSignUp();
        jumpToMainActivity();
        jumpToAlgorithm();
    }

    private void jumpToAlgorithm() {
        bt_algorithm = findViewById(R.id.bt_algorithm);
        bt_algorithm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Fence fence=new Fence();
                        fence.setFenceName("Offline");
                        fence.setOnline(false);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
    }

    private void jumpToMainActivity() {
        bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RequestHelper requestHelper=new RequestHelper();
                        requestHelper.setOnLoginListenner(new onLoginListenner() {
                            @Override
                            public void onLoginSucceed() {
                                Intent intent = new Intent(LoginActivity.this, FencechooseActivity.class);
                                Toast.makeText(getApplicationContext(), "Successfully, please wait", Toast.LENGTH_SHORT).show();
                                new Fence().setOnline(true);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onLoginFailed() {
                                Toast.makeText(getApplicationContext(), "Wrong login information", Toast.LENGTH_SHORT).show();
                            }
                        });
                        requestHelper.login(username.getText().toString(),password.getText().toString());
                    }
                });
            }
        });
    }

    private void jumpToSignUp() {
        bt_signup = findViewById(R.id.bt_signup);
        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.BLUETOOTH
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            //权限全部具有，则延时2s退出


        } else {
            EasyPermissions.requestPermissions(this, "需要权限",
                    RC_CAMERA_AND_WIFI, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
