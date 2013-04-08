package com.asmack;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author zhanghaitao
 * @date 2011-7-6
 * @version 1.0
 */
public class ActivityFileTransfer extends Activity implements OnClickListener {

	private Button download, upload;
	private FileTransferManager manager;
	private java.io.File saveFile;

	private IncomingFileTransfer transfer;

	private XmppFileManager _xmppFileMgr;

	private String user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.filetransfer);
		download = (Button) findViewById(R.id.download);
		upload = (Button) findViewById(R.id.upload);

		download.setOnClickListener(this);
		upload.setOnClickListener(this);

		user = getIntent().getStringExtra("user");
		System.out.println("user:" + user);

		// 文件保存目录
		String path = getApplicationContext().getFilesDir().getPath();
		saveFile = new java.io.File(path, "aaa.txt");/* 保存文件 */
		java.io.File dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		// saveFile = new java.io.File(dir, "aaa.txt");
		System.out.println("-----paht:" + path);
		System.out.println("-----Absolutepaht:" + saveFile.getAbsolutePath());
		System.out.println("-----dir:" + dir.getAbsolutePath());

		// _xmppFileMgr = new XmppFileManager(getBaseContext());
		// _xmppFileMgr.initialize(ActivityMain.connection);

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (transfer.getStatus().equals(Status.error)) {
				System.out.println("ERROR!!! " + transfer.getError());

			} else {
				System.out.println(transfer.getStatus());
				System.out.println(transfer.getProgress());
				if (transfer.getProgress() == 1) {
					Toast.makeText(ActivityFileTransfer.this, "下载完成",
							Toast.LENGTH_SHORT);
				} else {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(), 500);
				}
			}
		}

	};

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.download:
			try {
				// _xmppFileMgr = new XmppFileManager(this);
				// _xmppFileMgr.initialize(ActivityLoginAndChat.connection);
				// OutgoingFileTransfer transfer = _xmppFileMgr
				// .getFileTransferManager().createOutgoingFileTransfer(
				// "aaa@asus-pc");
				// FileTransferNegotiator fileTransferNeg =
				// FileTransferNegotiator
				// .getInstanceFor(ActivityLoginAndChat.connection);

				System.out.println("start send file");
				FileTransferManager transfer = new FileTransferManager(
						ActivityMain.connection);
				String destination = user;
				OutgoingFileTransfer out = transfer
						.createOutgoingFileTransfer(destination + "/Smack");
				// OutgoingFileTransfer out = transfer
				// .createOutgoingFileTransfer(destination);

				out.sendFile(saveFile, "you won't like it");
				// while (!out.isDone()) {
				// if (out.getStatus().equals(Status.error)) {
				// System.out.println("ERROR!!! " + out.getError());
				// } else {
				// System.out.println(out.getStatus());
				// System.out.println(out.getProgress());
				// }
				// }

				System.out.println("end send file");
				// Create the file transfer manager
				// FileTransferManager manager = new FileTransferManager(
				// ActivityLoginAndChat.connection);
				// FileTransferNegotiator.setServiceEnabled(
				// ActivityLoginAndChat.connection, true);
			} catch (XMPPException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
	}

}
