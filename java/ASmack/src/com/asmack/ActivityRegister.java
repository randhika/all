package com.asmack;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author zhanghaitao
 * @date 2011-7-6
 * @version 1.0
 */
public class ActivityRegister extends Activity {
	private Button register;
	private EditText password, username;
	private IQ result;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1: {
				System.out.println("----->register");
				Registration reg = new Registration();
				reg.setType(IQ.Type.SET);
				reg.setTo(ActivityMain.connection.getServiceName());
				reg.setUsername(username.getText().toString());
				reg.setPassword(password.getText().toString());
				reg.addAttribute("android", "geolo_createUser_android");
				System.out.println("reg:" + reg);
				PacketFilter filter = new AndFilter(new PacketIDFilter(reg
						.getPacketID()), new PacketTypeFilter(IQ.class));
				PacketCollector collector = ActivityMain.connection
						.createPacketCollector(filter);
				ActivityMain.connection.sendPacket(reg);

				result = (IQ) collector.nextResult(SmackConfiguration
						.getPacketReplyTimeout());
				// Stop queuing results
				collector.cancel();// 停止请求results（是否成功的结果）

				if (result == null) {
					Toast.makeText(getApplicationContext(), "服务器没有返回结果",
							Toast.LENGTH_SHORT).show();
				} else if (result.getType() == IQ.Type.ERROR) {
					if (result.getError().toString().equalsIgnoreCase(
							"conflict(409)")) {
						Toast.makeText(getApplicationContext(), "这个账号已经存在",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "注册失败",
								Toast.LENGTH_SHORT).show();
					}
				} else if (result.getType() == IQ.Type.RESULT) {
					Toast.makeText(getApplicationContext(), "恭喜你注册成功",
							Toast.LENGTH_SHORT).show();
				}
			}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		register = (Button) findViewById(R.id.register);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		});
	}

}
