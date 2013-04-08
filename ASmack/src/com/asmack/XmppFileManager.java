package com.asmack;

import java.io.File;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * @author zhanghaitao
 * @date 2011-7-7
 * @version 1.0
 */
public class XmppFileManager implements FileTransferListener {

	private XMPPConnection _connection;
	private FileTransferManager _fileTransferManager = null;
	private String answerTo;
	private static File externalFileDir;
	private static File landingDir;

	private String TAG = "filetransfer";

	private static final String gtalksmsDir = "GTalkSMS";

	public XmppFileManager(Context context) {

		// api level >=8
		externalFileDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		landingDir = new File(externalFileDir, gtalksmsDir);

		System.out.println("-----dir:" + landingDir.getAbsolutePath());

		if (!landingDir.exists()) {
			landingDir.mkdirs();
		}

	}

	public void initialize(XMPPConnection connection) {
		_connection = connection;
		// important: you have to make a dummy service discovery manager.
		// new ServiceDiscoveryManager(ActivityLoginAndChat.connection);
		// now this line does not cause any problems.
		_fileTransferManager = new FileTransferManager(_connection);
		_fileTransferManager.addFileTransferListener(this);
	}

	public FileTransferManager getFileTransferManager() {
		return _fileTransferManager;
	}

	private void send(String msg) {
		Log.i(TAG, msg);
	}

	@Override
	public void fileTransferRequest(FileTransferRequest request) {
		File saveTo;
		// set answerTo for replies and send()
		answerTo = request.getRequestor();
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			send("External Media not mounted read/write");
			return;
		} else if (!landingDir.isDirectory()) {
			send("The directory " + landingDir.getAbsolutePath()
					+ " is not a directory");
			return;
		}
		saveTo = new File(landingDir, request.getFileName());
		if (saveTo.exists()) {
			send("The file " + saveTo.getAbsolutePath() + " already exists");
			// delete
			saveTo.delete();
			// return;
		}
		IncomingFileTransfer transfer = request.accept();
		send("File transfer: " + saveTo.getName() + " - "
				+ request.getFileSize() / 1024 + " KB");
		try {
			transfer.recieveFile(saveTo);
			send("File transfer: " + saveTo.getName() + " - "
					+ transfer.getStatus());
			double percents = 0.0;
			while (!transfer.isDone()) {
				if (transfer.getStatus().equals(Status.in_progress)) {
					percents = ((int) (transfer.getProgress() * 10000)) / 100.0;
					send("File transfer: " + saveTo.getName() + " - "
							+ percents + "%");
				} else if (transfer.getStatus().equals(Status.error)) {
					send(returnAndLogError(transfer));
					return;
				}
				Thread.sleep(1000);
			}
			if (transfer.getStatus().equals(Status.complete)) {
				send("File transfer complete. File saved as "
						+ saveTo.getAbsolutePath());
			} else {
				send(returnAndLogError(transfer));
			}
		} catch (Exception ex) {
			String message = "Cannot receive the file because an error occured during the process."
					+ ex;
			Log.e(TAG, message, ex);
			send(message);
		}

	}

	public File getLandingDir() {
		return landingDir;
	}

	public String returnAndLogError(FileTransfer transfer) {
		String message = "Cannot process the file because an error occured during the process.";

		if (transfer.getError() != null) {
			message += transfer.getError();
		}
		if (transfer.getException() != null) {
			message += transfer.getException();
		}
		return message;
	}
}
