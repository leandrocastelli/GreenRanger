package com.lcs.greenranger.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View.OnCreateContextMenuListener;
import android.widget.RemoteViews;

import com.lcs.greenranger.R;
import com.lcs.greenranger.service.MediaService;
import com.lcs.greenranger.service.SoundPlayer;
import com.lcs.greenranger.service.MediaService.LocalBinder;



public class DaggerWidgetProvider extends AppWidgetProvider {
	
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds)
	{
		ComponentName thisWidget = new ComponentName(context,
				DaggerWidgetProvider.class);
		
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		
		for (int widgetId : allWidgetIds) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			Intent intent = new Intent(context,MediaService.class);

			PendingIntent pendingIntent = PendingIntent.getService(context,
					0, intent, 0);
			remoteViews.setOnClickPendingIntent(R.id.dagger_widget, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}

	}

	
		
	
}
