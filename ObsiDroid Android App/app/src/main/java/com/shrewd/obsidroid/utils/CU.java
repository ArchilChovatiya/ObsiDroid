package com.shrewd.obsidroid.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.shrewd.obsidroid.R;
import com.shrewd.obsidroid.databinding.ProgressBarBinding;

public class CU {

    public static final String TAG = CU.class.getName();
    private static Dialog progressDialog;
    private static Snackbar snackbar;
    private static Toast globeToast;

    public static void showProgressDialog(Context mContext) {
        showProgressDialog(mContext, false);
    }

    public static void showProgressDialog(Context mContext, boolean isCancelable) {
        if (mContext == null || ((Activity) mContext).isDestroyed())
            return;
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Log.e(TAG, "showProgressDialog: ");
        progressDialog = new Dialog(mContext);
        ProgressBarBinding binding = ProgressBarBinding.inflate(((Activity) mContext).getLayoutInflater());
        progressDialog.setContentView(binding.getRoot());
        Window window = progressDialog.getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.ZoomingDialogAnimation;
            window.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.color.transparent));
            window.setLayout(mContext.getResources().getDimensionPixelSize(R.dimen._120sdp), mContext.getResources().getDimensionPixelSize(R.dimen._120sdp));
            window.setGravity(Gravity.CENTER);
        }
        progressDialog.setCancelable(isCancelable);
        Glide.with(mContext)
                .asGif()
                .circleCrop()
                .load(R.drawable.giphy)
                .into(binding.ivLoader);
        try {
            progressDialog.show();
        } catch (Exception ignored) {
        }
    }

    public static void hideProgressDialog() {
        Log.e(TAG, "hideProgressDialog: ");
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            } catch (Exception ignored) {
            }
        }
    }

    public static void snackBar(Context mContext, String msg, int duration) {
        snackBar(((Activity) mContext).findViewById(android.R.id.content), msg, duration, "", null);
    }

    public static Snackbar snackBar(View rootView, String msg, int duration) {
        return snackBar(rootView, msg, duration, "", null);
    }

    public static Snackbar snackBar(Context mContext, String msg, int duration, String action, View.OnClickListener onClickListener) {
        return snackBar(((Activity) mContext).findViewById(android.R.id.content), msg, duration, action, onClickListener);
    }

    public static Snackbar snackBar(View rootView, String msg, int duration, String action, View.OnClickListener onClickListener) {
        try {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
            snackbar = Snackbar
                    .make(rootView, msg, duration);
            if (!isNullOrEmpty(action)) {
                snackbar.setAction(action, onClickListener);
            }
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setMaxLines(5);  //Or as much as you need
            snackbar.show();
            return snackbar;
        } catch (Exception ignored) {

        }
        return null;
    }

    public static boolean isNullOrEmpty(String action) {
        return isNullOrEmpty(action, true);
    }

    public static boolean isNullOrEmpty(String action, boolean withTrim) {
        return action == null || (withTrim ? action.trim().equals("") : action.equals(""));
    }

    public static void hideSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    public static Toast toast(Context mContext, String msg, int duration) {
        try {
            if (globeToast != null) {
                globeToast.cancel();
            }
            Toast toast = Toast.makeText(mContext, msg, duration);
            View view = toast.getView();
            view.getBackground().setColorFilter(mContext.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(mContext.getColor(R.color.white));
            globeToast = toast;
            return globeToast;
        } catch (Exception ex) {
            return Toast.makeText(mContext, msg, duration);
        }
    }

    public static boolean isNumeric(String str) {
        if (str == null)
            return false;
        try {
            Double id = Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void showKeyboard(Dialog dialog) {
        boolean isShowing = false;
        try {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            isShowing = true;
        } catch (Exception ex) {
            Log.e(TAG, "showKeyboard: " + ex.getMessage());
        }
    }

    public static void showKeyboard(Context mContext, View view) {
        boolean isShowing = false;
        try {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm != null) {
                if (view != null) {
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                    isShowing = true;
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "showKeyboard: " + ex.getMessage());
        }
        if (!isShowing) {
            showKeyboardForceFully(mContext);
        }
    }

    public static void showKeyboardForceFully(Context mContext) {
        try {
            if (!isKeyboardActive(((Activity) mContext).findViewById(android.R.id.content))) {
                Log.e(TAG, "showKeyboardForceFully: keyboard showed");
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                Log.e(TAG, "showKeyboardForceFully: keyboard already showing");
            }
        } catch (Exception ex) {
            Log.e(TAG, "showKeyboardForceFully: " + ex.getMessage());
        }
    }

    public static void hideKeyboardForceFully(Context mContext) {
        try {
            if (isKeyboardActive(((Activity) mContext).findViewById(android.R.id.content))) {
                Log.e(TAG, "hideKeyboardForceFully: keyboard hide");
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            } else {
                Log.e(TAG, "hideKeyboardForceFully: keyboard already hidden");
            }
        } catch (Exception ex) {
            Log.e(TAG, "hideKeyboardForceFully: " + ex.getMessage());
        }
    }

    private static boolean isKeyboardActive(View mainLayout) {
        Rect r = new Rect();
        mainLayout.getWindowVisibleDisplayFrame(r);
        int screenHeight = mainLayout.getRootView().getHeight();
        int keypadHeight = screenHeight - r.bottom;
        return (keypadHeight > screenHeight * 0.15);
    }
}
