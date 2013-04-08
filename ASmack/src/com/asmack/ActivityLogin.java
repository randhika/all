package com.asmack;

import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ActivityLogin extends Activity {

	private Button login, logout;
	private EditText etPassword, etUsername;

	public static String mCurrentAccount;

	public static Util util;

	private final String ACCOUNT_KEY = "login_account";
	private final String PASSWORD_KEY = "login_password";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);

		login = (Button) findViewById(R.id.login);
		logout = (Button) findViewById(R.id.logout);
		etPassword = (EditText) findViewById(R.id.password);
		etUsername = (EditText) findViewById(R.id.username);

		ActivityLogin.util = new Util(this);
		etUsername.setText(ActivityLogin.util.getString(ACCOUNT_KEY));
		etPassword.setText(ActivityLogin.util.getString(PASSWORD_KEY));

		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ActivityMain.connection.disconnect();
			}
		});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					ActivityMain.connection.connect();// connect
					String account = etUsername.getText().toString();
					String password = etPassword.getText().toString();
					// 保存用户和密码
					ActivityLogin.util.saveString(ACCOUNT_KEY, account);
					ActivityLogin.util.saveString(PASSWORD_KEY, password);
					ActivityMain.connection.login(account, password);// login
					// login success 暂时的这样判断
					System.out.println("login success");
					ActivityLogin.mCurrentAccount = account;
					System.out.println(ActivityMain.connection.getUser());
					// 登陆成功后，通知服务器用户处于在线状态
					Presence presence = new Presence(Presence.Type.available);
					ActivityMain.connection.sendPacket(presence);

					// 开始跳转到main
					Intent intent = new Intent(ActivityLogin.this,
							ActivityMain.class);
					startActivity(intent);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	@Override
	protected void onDestroy() {
		ActivityMain.connection.disconnect();
		super.onDestroy();
	}

}