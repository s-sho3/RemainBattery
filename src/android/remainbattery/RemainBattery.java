package android.remainbattery;

import android.app.Service;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.RemoteViews;

public class RemainBattery extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appMan, int[] appIds) {
		Intent intent = new Intent(context, WidgetService.class);
		context.startService(intent);
	}

    public static class WidgetService extends Service {
        @Override
        public void onStart(Intent in, int si) {
        	IntentFilter filter = new IntentFilter();
        	filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        	//filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); // when a home button pushed or unlocked.
        	registerReceiver(receiver, filter);
        }
        
        @Override
        public IBinder onBind(Intent in) {
            return null;
        }
    }
    
    private static BroadcastReceiver receiver = new BroadcastReceiver() {
    	int scale = 100;
    	int remain = 0;

    	@Override
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
    			remain = intent.getIntExtra("level", 0);
    			scale = intent.getIntExtra("scale",	0);
    		}
        	AppWidgetManager manager = AppWidgetManager.getInstance(context);
    		ComponentName name = new ComponentName(context, RemainBattery.class);
        	RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.main);
        	view.setTextViewText(R.id.textView, ""+(int)(remain*100/scale)+"%");
        	manager.updateAppWidget(name, view);

   	}
    	
    };
}
