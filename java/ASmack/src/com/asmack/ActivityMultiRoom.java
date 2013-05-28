package com.asmack;

import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DelayInformation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author zhanghaitao
 * @date 2011-7-11
 * @version 1.0
 */
public class ActivityMultiRoom extends Activity implements OnClickListener,
		OnMessageListener {
	private String TAG = "chat";
	private Thread mThread;

	private final int RECEIVE = 1;

	private Button send;
	private EditText record, etMessage;

	private MultiUserChat muc;
	private MessageReceiver mUpdateMessage;

	private String jid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiroom);

		send = (Button) findViewById(R.id.send);
		record = (EditText) findViewById(R.id.record);
		etMessage = (EditText) findViewById(R.id.message);

		send.setOnClickListener(this);

		jid = getIntent().getStringExtra("jid");

		// 后面服务名称必需是创建房间的那个服务
		String multiUserRoom = jid;
		try {
			muc = new MultiUserChat(ActivityMain.connection, multiUserRoom);
			// 创建聊天室,进入房间后的nickname
			muc.join(ActivityLogin.mCurrentAccount);

			Log.v(TAG, "join success");

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ChatPacketListener chatListener = new ChatPacketListener(muc);
		muc.addMessageListener(chatListener);

		// mUpdateMessage = new MessageReceiver(multiUserRoom);
		// mUpdateMessage = new MessageReceiver("admin");
		// mUpdateMessage.setOnMessageListener(this);
		// mThread = new Thread(mUpdateMessage);
		// mThread.start();

	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case RECEIVE: {
				Bundle bd = msg.getData();
				String from = bd.getString("from");
				String body = bd.getString("body");
				receiveMsg(from, body);
			}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.send: {
			String msg = etMessage.getText().toString();
			if (!"".equals(msg)) {
				try {
					// System.out.println("我：" + msg);
					// receiveMsg("我", msg);
					muc.sendMessage(msg);
				} catch (Exception e) {
				}
			}
		}
			break;
		default:
			break;
		}
	}

	@Override
	public void processMessage(Message message) {
		receiveMsg(message.getFrom(), message.getBody());
	}

	private void receiveMsg(String from, String msg) {

		Log.v(TAG, "receiveMsg():from=" + from);
		Log.v(TAG, "receiveMsg():msg=" + msg);

		record.setText(record.getText() + from + ":" + msg + "\n");
		// record.append(from + ":" + msg + "\n");
	}

	@Override
	protected void onDestroy() {
		// try {
		// muc.destroy(null, null);
		// mUpdateMessage.flag = false;
		// mUpdateMessage.mCollector.cancel();
		// } catch (XMPPException e) {
		// // TODO Auto-generated catch block
		// Log.v(TAG, "Exception:", e);
		// }
		super.onDestroy();
	}

	class ChatPacketListener implements PacketListener {
		private String _number;
		private Date _lastDate;
		private MultiUserChat _muc;
		private String _roomName;

		public ChatPacketListener(MultiUserChat muc) {
			_number = "0";
			_lastDate = new Date(0);
			_muc = muc;
			_roomName = muc.getRoom();
		}

		@Override
		public void processPacket(Packet packet) {
			Message message = (Message) packet;
			String from = message.getFrom();

			if (message.getBody() != null) {
				DelayInformation inf = (DelayInformation) message.getExtension(
						"x", "jabber:x:delay");
				Date sentDate;
				if (inf != null) {
					sentDate = inf.getStamp();
				} else {
					sentDate = new Date();
				}

				Log.w(TAG, "Receive old message: date="
						+ sentDate.toLocaleString() + " ; message="
						+ message.getBody());

				android.os.Message msg = new android.os.Message();
				msg.what = RECEIVE;
				Bundle bd = new Bundle();
				bd.putString("from", from);
				bd.putString("body", message.getBody());
				msg.setData(bd);
				handler.sendMessage(msg);
			}
		}
	}
}
