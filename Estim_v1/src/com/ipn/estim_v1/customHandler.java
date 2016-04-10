package com.ipn.estim_v1;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class customHandler extends BroadcastReceiver {

	public customHandler() {
	}

	private static final String TAG = "customHandler";

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		Log.d(TAG, "action=" + action);
		// Handle Push Message when opened
		if (action.equals(PBConstants.EVENT_MSG_OPEN)) {
			// Check for Pushbots Instance
			Pushbots pushInstance = Pushbots.sharedInstance();
			if (!pushInstance.isInitialized()) {
				Log.d(TAG, "Initializing Pushbots");
				Pushbots.sharedInstance().init(context.getApplicationContext());
			}

			// Clear Notification array
			if (PBNotificationIntent.notificationsArray != null) {
				PBNotificationIntent.notificationsArray = null;
			}

			HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_OPEN);
			Log.w(TAG, "User clicked notification with Message: " + PushdataOpen.get("message"));

			// Report Opened Push Notification to Pushbots
			if (Pushbots.sharedInstance().isAnalyticsEnabled()) {
				Pushbots.sharedInstance().reportPushOpened((String) PushdataOpen.get("PUSHANALYTICS"));
			}

			// Start lanuch Activity
			String packageName = context.getPackageName();
			Intent resultIntent = new Intent(context.getPackageManager().getLaunchIntentForPackage(packageName));
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

			resultIntent.putExtras(intent.getBundleExtra("pushData"));
			Pushbots.sharedInstance().startActivity(resultIntent);

			// Handle Push Message when received
		} else if (action.equals(PBConstants.EVENT_MSG_RECEIVE)) { // "com.google.android.c2dm.intent.RECEIVE"
			HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_RECEIVE);
			String msj = PushdataOpen.get("message").toString();
			// Log.w(TAG, "User Received notification with Message: " +
			// PushdataOpen.get("message"));
//			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
//			editor.putString("mensaje", msj);
//			editor.commit();
//			Toast.makeText(context, "Notificaci�n Recibida: " + msj, Toast.LENGTH_SHORT).show();
		}

	}

}