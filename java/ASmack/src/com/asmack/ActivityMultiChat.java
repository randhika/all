package com.asmack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author zhanghaitao
 * @date 2011-7-5
 * @version 1.0
 */
public class ActivityMultiChat extends Activity {

	private ListView listview;
	private HostRoomAdapter hostRoomAdapter;

	private List<HostedRoom> roominfos = new ArrayList<HostedRoom>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.multichat);

		listview = (ListView) findViewById(R.id.listview);
		roominfos.clear();
		hostRoomAdapter = new HostRoomAdapter(this);
		listview.setAdapter(hostRoomAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// 获取用户，跳到聊天室
				HostedRoom room = roominfos.get(position);
				Intent intent = new Intent(ActivityMultiChat.this,
						ActivityMultiRoom.class);
				intent.putExtra("jid", room.getJid());
				startActivity(intent);
			}
		});
		updateHostRoom();
	}

	private void updateHostRoom() {
		Collection<HostedRoom> hostrooms;
		try {
			hostrooms = MultiUserChat.getHostedRooms(ActivityMain.connection,
					"conference.zhanghaitao-pc");
			for (HostedRoom entry : hostrooms) {
				System.out.println(entry.getName() + " - " + entry.getJid());
				roominfos.add(entry);
			}
			hostRoomAdapter.notifyDataSetChanged();
		} catch (XMPPException e) {
			e.printStackTrace();
		}

	}

	private class HostRoomAdapter extends BaseAdapter {
		private Context context;

		public HostRoomAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return roominfos.size();
		}

		@Override
		public Object getItem(int position) {
			return roominfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		private class ViewHolder {
			TextView name;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {
			HostedRoom room = roominfos.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LinearLayout layout = new LinearLayout(context);

				holder.name = new TextView(context);
				layout.setOrientation(LinearLayout.HORIZONTAL);
				layout.addView(holder.name);
				convertView = layout;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(room.getName());

			return convertView;
		}

	}

}
