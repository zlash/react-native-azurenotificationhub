
package com.azure.reactnative.notificationhub;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import android.util.Log;

import com.microsoft.windowsazure.notifications.NotificationsHandler;

public class ReactNativeNotificationsHandler extends NotificationsHandler {
        private static final String LOG_TAG = "AzureNotificationHub";
        public static final int NOTIFICATION_ID = 1;
        private NotificationManager mNotificationManager;
        NotificationCompat.Builder builder;
        Context ctx;

        @Override
        public void onReceive(Context context, Bundle bundle) {
            ctx = context;
            String nhMessage = bundle.getString("message");
            Log.i(LOG_TAG, nhMessage);
            sendNotification(nhMessage);
        }

        private Class getMainActivityClass() {
                String packageName = ctx.getPackageName();
                Intent launchIntent = ctx.getPackageManager().getLaunchIntentForPackage(packageName);
                String className = launchIntent.getComponent().getClassName();
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
        }
        
        private void sendNotification(String msg) {
            Class intentClass = getMainActivityClass();
            Intent intent = new Intent(ctx, intentClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            mNotificationManager = (NotificationManager)
                    ctx.getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                    intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(ctx)
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("TITLE")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(msg))
                            .setSound(defaultSoundUri)
                            .setContentText(msg);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    
    
}
