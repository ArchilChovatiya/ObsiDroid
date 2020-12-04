package com.shrewd.obsidroid.runnable;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.shrewd.obsidroid.activity.MainActivity;
import com.shrewd.obsidroid.utils.CU;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SenderThread implements Runnable {

    private final String host;
    private final int port;
    private final Context mContext;
    private Socket socket;
    private DataInputStream input;
    private final static String TAG = SenderThread.class.getName();
    private String message;
    private int clientTextColor = Color.LTGRAY;
    private String clientMessage;
    private Thread msgThread;

    public SenderThread(Context mContext, String host, int port) {
        this.mContext = mContext;
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try {

            InetAddress serverAddr = InetAddress.getByName(host);
            showMessage("Connecting to Server...");

            socket = new Socket(serverAddr, port);

            if (socket.isBound()) {
                CU.hideProgressDialog();
                showMessage("Connected to Server...");
                SharedPreferences.Editor editor = mContext.getSharedPreferences("GC", Context.MODE_PRIVATE).edit();
                editor.putString("host", host);
                editor.putInt("port", port);
                editor.apply();
            }

            InputStream inputStream = socket.getInputStream();
            this.input = new DataInputStream(inputStream);
            String[] msg = input.readUTF().split(":");
            boolean isValid = msg.length == 5 && mContext instanceof MainActivity;
            if (isValid) {
                for (String str : msg) {
                    if (!CU.isNumeric(str)) {
                        isValid = false;
                        break;
                    }
                }
            }
            if (isValid) {
                ((MainActivity) mContext).setSeekbars(msg);
            }

        } catch (final Exception e) {
            if (mContext != null && mContext instanceof MainActivity) {
                ((MainActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CU.toast(mContext, "Cannot establish connection!\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                        ((MainActivity) mContext).dialog.show();
                    }
                });
            }
            CU.hideProgressDialog();
            showMessage(e.getMessage());
        }
    }

    public void sendMessage(final String message) {

        msgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != socket) {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(message);
                        showMessage("sent: " + message);
                        if (message.contains("close")) {
                            Log.e(TAG, "closed");
                            socket.close();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "run: error: " + e.getMessage());
                }
            }
        });
        if (!msgThread.isAlive()) {
            msgThread.start();
        }
    }

    public void showMessage(final String message) {
        Log.e(TAG, "showMessage: " + message);
        /*((MainActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CU.toast(mContext, message, Toast.LENGTH_LONG).show();
            }
        });*/
    }
}
