package com.asmack;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.os.Handler;

/**
 * @author zhanghaitao
 * @date 2011-7-5
 * @version 1.0 在编写MessageReceiver类时需要注意如下两点
 *          messageReceiver和updateContactState一样，并不直接处理聊天消息，
 *          而是通过调用一个processMessage事件方法进行处理
 *          获得聊天消息要通过packetCollector.nextResult方法。 如果当前没有聊天消息，nextResult方法会被阻塞。
 *          有两个地方需要接收消息。一个是在聊天界面， 另一个是在联系人列表界面 在chatRoom类中接收到消息后会直接显示在聊天记录中
 */
public class MessageReceiver implements Runnable {
	private final static String TAG = "MessageReceiver";
	private String mAccount;
	private PacketFilter filter;
	private OnMessageListener mOnMessageListener;
	public PacketCollector mCollector;

	public boolean flag = true;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Message message = (Message) msg.obj;
			if (mOnMessageListener != null) {
				mOnMessageListener.processMessage(message);
			}
			super.handleMessage(msg);
		}

	};

	public MessageReceiver(String account) {
		mAccount = account;
		// 用于过滤只包含帐户信息的聊天消息

		// 我们可以使用一个AndFilter来结合其它两个过滤器。
		filter = new AndFilter(new PacketTypeFilter(Message.class),
				new FromContainsFilter(mAccount));
		mCollector = ActivityMain.connection
				.createPacketCollector(filter);

	}

	@Override
	public void run() {
		while (flag) {
			Packet packet = mCollector.nextResult();
			if (packet instanceof Message) {
				System.out.println(((Message) packet).getBody());
				Message msg = (Message) packet;
				android.os.Message message = new android.os.Message();
				message.obj = msg;
				handler.sendMessage(message);
			}
		}
	}

	public void setOnMessageListener(OnMessageListener listener) {
		mOnMessageListener = listener;
	}

}
