package com.lcs.greenranger.widget;

import com.lcs.greenranger.*;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;



public class DaggerWidgetProvider extends AppWidgetProvider{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		final int N = appWidgetIds.length;
		
		for (int i=0; i<N; i++)
		{
			int appWidgetId = appWidgetIds[i];
			
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			//views.setOnClickPendingIntent(viewId, pendingIntent)
		}
	}
	
}
