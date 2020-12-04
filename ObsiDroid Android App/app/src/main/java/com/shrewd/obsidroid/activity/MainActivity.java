package com.shrewd.obsidroid.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SeekBar;

import com.google.android.material.snackbar.Snackbar;
import com.shrewd.obsidroid.R;
import com.shrewd.obsidroid.databinding.ActivityMainBinding;
import com.shrewd.obsidroid.databinding.DgConnectionBinding;
import com.shrewd.obsidroid.runnable.SenderThread;
import com.shrewd.obsidroid.utils.CU;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private SenderThread senderThread;
    private Thread thread;
    private MainActivity mContext;
    public Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = MainActivity.this;

        dialog = new Dialog(mContext);
        final DgConnectionBinding bndConnection = DgConnectionBinding.inflate(getLayoutInflater());
        dialog.setContentView(bndConnection.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.ZoomingDialogAnimation;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bg_inset));
            window.setGravity(Gravity.CENTER);
        }
        dialog.setCancelable(false);
        dialog.show();

        bndConnection.btnConnect.setText("test");
        bndConnection.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bndConnection.etHost.getText() == null || bndConnection.etPort.getText() == null) {
                    CU.snackBar(bndConnection.getRoot(), "Failed to connect!", Snackbar.LENGTH_LONG);
                    return;
                }
                String host = bndConnection.etHost.getText().toString();
                String port = bndConnection.etPort.getText().toString();
                if (CU.isNullOrEmpty(host)) {
                    CU.snackBar(bndConnection.getRoot(), "Please enter host!", Snackbar.LENGTH_LONG);
                    bndConnection.etHost.requestFocus();
                    return;
                }

                if (CU.isNullOrEmpty(port)) {
                    CU.snackBar(bndConnection.getRoot(), "Please enter port!", Snackbar.LENGTH_LONG);
                    bndConnection.etPort.requestFocus();
                    return;
                }

                if (!CU.isNumeric(port)) {
                    CU.snackBar(bndConnection.getRoot(), "Port should be integer!", Snackbar.LENGTH_LONG);
                    bndConnection.etPort.requestFocus();
                    return;
                }

                dialog.dismiss();

                connect(host, Integer.parseInt(port));
            }
        });
    }

    public void setSeekbars(String[] values) {
        for (int i = 0; i < values.length; i++) {
            switch (i) {
                case 0:
                    binding.sbShoulder.setProgress(Integer.parseInt(values[i]));
                    break;
                case 1:
                    binding.sbElbow.setProgress(Integer.parseInt(values[i]));
                    break;
                case 2:
                    binding.sbRotate.setProgress(Integer.parseInt(values[i]));
                    break;
                case 3:
                    binding.sbWrist.setProgress(Integer.parseInt(values[i]));
                    break;
                case 4:
                    binding.sbGrip.setProgress(Integer.parseInt(values[i]));
                    break;
            }
        }
    }

    private void connect(String host, int port) {


        binding.webStream.loadUrl("http://" + host + ":5000");
        senderThread = new SenderThread(mContext, host, port);
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        thread = new Thread(senderThread);
        thread.start();
        CU.showProgressDialog(mContext);

        binding.sbShoulder.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                senderThread.sendMessage(String.valueOf(0 * 1000 + progress)); //servo no. 0
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.sbElbow.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                senderThread.sendMessage(String.valueOf(1 * 1000 + progress)); //servo no. 1
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.sbRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                senderThread.sendMessage(String.valueOf(2 * 1000 + progress)); //servo no. 2
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.sbWrist.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                senderThread.sendMessage(String.valueOf(3 * 1000 + progress)); //servo no. 3
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.sbGrip.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                senderThread.sendMessage(String.valueOf(4 * 1000 + progress)); //servo no. 4
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        senderThread.sendMessage("close");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("Connect");
//        item.setTitle("Connect");
        item.setEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Connect") && dialog != null) {
            senderThread.sendMessage("close");
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}