package com.example.grzegorzkwasniewski.speakloududacity.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.grzegorzkwasniewski.speakloududacity.R;
import com.example.grzegorzkwasniewski.speakloududacity.audioFilesView.AudioFilesActivity;

import java.util.List;


public class Widget extends AppWidgetProvider {

    public static String recordingName;

    public static List<String> recordingsName;
    public static String EXTRA_ITEM = "extra";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        if(recordingsName != null) {

            Intent intentService = new Intent(context, StackWidgetService.class);

            intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intentService.setData(Uri.parse(intentService.toUri(Intent.URI_INTENT_SCHEME)));

            views.setTextViewText(R.id.recipe_name, "All recordings");
            views.setRemoteAdapter(R.id.widgetListView, intentService);

            Intent intent = new Intent(context, AudioFilesActivity.class);

            PendingIntent pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.root_layout, pendingIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}