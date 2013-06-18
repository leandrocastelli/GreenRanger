package com.lcs.greenranger.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lcs.greenranger.R;

public class AppRater{
   
    private final static String APP_PNAME = "com.lcs.greenranger";
    
    private final static int DAYS_UNTIL_PROMPT = 2;
    private final static int LAUNCHES_UNTIL_PROMPT = 4;
    private static AlertDialog alertDialog = null ;
    public static void app_launched(Context mContext) {
    	
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }
        
        SharedPreferences.Editor editor = prefs.edit();
        
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + 
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }
        
        editor.commit();
    }   
    
    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
       
        
        dialog.setTitle(mContext.getString(R.string.rate) + mContext.getString(R.string.app_name));
        
        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,0);
        
        ll.setLayoutParams(lp);
        String formatted = mContext.getString(R.string.format_rate, mContext.getString(R.string.app_name));
        TextView tv = new TextView(mContext);
        tv.setText(formatted);
        
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);
        
        Button b1 = new Button(mContext);
        b1.setId(1);
        b1.setText(mContext.getString(R.string.rate) + mContext.getString(R.string.app_name));
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                alertDialog.dismiss();
                
            }
        });        
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setId(2);
        b2.setText(mContext.getString(R.string.remind_later));
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	 Long date_firstLaunch = System.currentTimeMillis();
            	 if (editor != null)
            		 editor.putLong("date_firstlaunch", date_firstLaunch);
            	alertDialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setId(3);
        b3.setText(mContext.getString(R.string.no_thanks));
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                alertDialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setView(ll);        
        alertDialog = dialog.show();        
    }

}
