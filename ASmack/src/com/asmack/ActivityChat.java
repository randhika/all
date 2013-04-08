package com.asmack;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author zhanghaitao
 * @date 2011-7-10
 * @version 1.0
 */
public class ActivityChat extends Activity {
	private Button send;
	private TextView record;
	private EditText message;

	private Chat newChat;

	public static Util util;

	private MessageListenerEx ml = new MessageListenerEx();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singlechat);

		send = (Button) findViewById(R.id.send);
		record = (TextView) findViewById(R.id.record);
		message = (EditText) findViewById(R.id.message);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					newChat.sendMessage(message.getText().toString());
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}

		});

		ChatManager chatmanager = ActivityMain.connection.getChatManager();

		// get user
		Intent intent = getIntent();
		String user = intent.getStringExtra("user");
		System.out.println("user:" + user);
		// new a session
		newChat = chatmanager.createChat(user, null);
		// 监听被动接收消息，或广播消息监听器
		//  chatmanager.addChatListener(new ChatManagerListenerEx());

		// send message
		try {
			newChat.sendMessage("im bird man");

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public class ChatManagerListenerEx implements ChatManagerListener {

		@Override
		public void chatCreated(Chat chat, boolean arg1) {
			// TODO Auto-generated method stub
			chat.addMessageListener(ml);
		}

	}

	public class MessageListenerEx implements MessageListener {

		@Override
		public void processMessage(Chat arg0, Message message) {
			String result = message.getFrom() + ":" + message.getBody();
			System.out.println(result);
			android.os.Message msg = new android.os.Message();
			msg.what = 0;
			Bundle bd = new Bundle();
			bd.putString("msg", result);
			msg.setData(bd);
			handler.sendMessage(msg);

		}
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0: {
				String result = msg.getData().getString("msg");
				record.setText(record.getText() + "\n" + result);
			}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		newChat.removeMessageListener(ml);
		super.onDestroy();
	}

	// class MessageListenerService extends Service {
	//
	// @Override
	// public IBinder onBind(Intent arg0) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public void onCreate() {
	//			
	// // TODO Auto-generated method stub
	// super.onCreate();
	// }
	//
	// @Override
	// public void onStart(Intent intent, int startId) {
	// // TODO Auto-generated method stub
	// super.onStart(intent, startId);
	// }
	//
	// @Override
	// public int onStartCommand(Intent intent, int flags, int startId) {
	// // TODO Auto-generated method stub
	// return super.onStartCommand(intent, flags, startId);
	// }
	//
	// }

}
