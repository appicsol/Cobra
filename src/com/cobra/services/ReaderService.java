package com.cobra.services;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.cobra.MainActivity;
import com.cobra.R;
import com.cobra.appClass;
import com.cobra.api.Cobra.UnRegisterListeners;
import com.cobra.api.SerialDriver;
import com.cobra.api.SerialDriver.ISerialPortWListener;
import com.cobra.api.SerialDriver.SerialPortWEvent;
import com.cobra.interfaces.OnEventChange;

public class ReaderService extends Service {

	private appClass globV;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		showIcon();
		globV = (appClass) getApplicationContext();
	}

	public String tempText = "";
	Intent intent = null;
	TaskStackBuilder stackBuilder = null;
	PendingIntent pi = null;
	NotificationManager mNotificationManager = null;
	int notifyID = 1;
	Context context = null;

	public void showIcon() {
		try {
			
			intent = new Intent(this, MainActivity.class);
			stackBuilder = TaskStackBuilder.create(this);
			stackBuilder.addParentStack(MainActivity.class);
			stackBuilder.addNextIntent(intent);
			pi = stackBuilder.getPendingIntent(0,
					PendingIntent.FLAG_UPDATE_CURRENT);

			mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// Sets an ID for the notification, so it can be updated

			context = this;
			NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
					context).setContentTitle("Cobra")
					.setContentText(appClass.TitleText).setContentIntent(pi)
					.setSmallIcon(R.drawable.app_icon).setOngoing(true);

			tempText = appClass.TitleText;

			Notification n = mNotifyBuilder.build();

			mNotificationManager.notify(notifyID, n);

			final ScheduledExecutorService exec = Executors
					.newScheduledThreadPool(1);

			exec.schedule(new Runnable() {
				@Override
				public void run() {
					ActivityManager activityManager = (ActivityManager) context
							.getSystemService(Context.ACTIVITY_SERVICE);
					List<RunningTaskInfo> services = activityManager
							.getRunningTasks(Integer.MAX_VALUE);
					
					RunningTaskInfo runningTaskInfo = services.get(0);
					ComponentName componentName = runningTaskInfo.topActivity;
					String packageName = componentName.getPackageName()
							.toString();
					String packageName_Context = context.getPackageName()
							.toString();

					if (!packageName.equalsIgnoreCase(packageName_Context)) {
						try {
							Log.i("ReaderService", "ThreadPool Shutdown");
							exec.shutdown();
							Log.i("ReaderService", "STOPPED");
							stopSelf();
						} catch (Exception e) {

						}
					}

					appClass.UpdateIcon = true;
					if (appClass.UpdateIcon) {
						appClass.UpdateIcon = false;
						NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
								context)
								.setContentTitle("Cobra")
								.setContentText(
										appClass.TitleText )
								.setContentIntent(pi)
								.setSmallIcon(R.drawable.app_icon);
				

						Notification n = mNotifyBuilder.build();
						n.flags |= Notification.FLAG_ONGOING_EVENT;
						mNotificationManager.notify(notifyID, n);
					}
				}
			}, 500, TimeUnit.MILLISECONDS);
		} catch (Exception e) {

		}

	}

	public void UpdateIcon() {
		if (mNotificationManager != null) {
			appClass.UpdateIcon = false;
			NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
					context).setContentTitle("Cobra")
					.setContentText(appClass.TitleText).setContentIntent(pi)
					.setSmallIcon(R.drawable.app_icon);

			Notification n = mNotifyBuilder.build();
			n.flags |= Notification.FLAG_ONGOING_EVENT;
			mNotificationManager.notify(notifyID, n);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		UpdateIcon();
		super.onDestroy();

		appClass.TitleText = "Disconnected";
		appClass.UpdateIcon = true;
		if (mNotificationManager != null)
			mNotificationManager.cancelAll();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
