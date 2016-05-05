package hr.magicpot.projectpliva.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.v7.app.NotificationCompat;

import java.util.Date;

import hr.magicpot.projectpliva.R;
import hr.magicpot.projectpliva.constants.Action;
import hr.magicpot.projectpliva.constants.Constants;
import hr.magicpot.projectpliva.receivers.ActionReceiver;
import hr.magicpot.projectpliva.receivers.DeleteNotificationReceiver;

/**Service koji pokrece Alarm jednom u danu i notifikaciju*/
public class AlarmService extends IntentService {
    //ne znam kako koristiti Butterknife ovdje

    public AlarmService() {
        super("ALARM THREAD");
    }

    //TODO ne gasi reciever, nekad gasi
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle b = new Bundle();

        Intent close = new Intent(this, ActionReceiver.class);
        close.putExtra("action", Action.CLOSE.name());
        PendingIntent intentClose = PendingIntent.getBroadcast(this, Constants.ACTION_CLOSE, close, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent confirm = new Intent(this, ActionReceiver.class);
        confirm.putExtra("action", Action.CONFIRM.name());
        PendingIntent intentConfirm = PendingIntent.getBroadcast(this, Constants.ACTION_CONFIRM, confirm, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent delete = new Intent(this, DeleteNotificationReceiver.class);
        delete.putExtra("date", new Date().getTime());
        PendingIntent deletePIntent = PendingIntent.getBroadcast(this, Constants.DELETE, delete, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Action.Builder actionBuilder1 = new NotificationCompat.Action.Builder(-1, getResources().getString(R.string.notification_close), intentClose);
        NotificationCompat.Action actionClose = actionBuilder1.build();

        NotificationCompat.Action.Builder actionBuilder2 = new NotificationCompat.Action.Builder(-1, getResources().getString(R.string.notification_confirm), intentConfirm);
        NotificationCompat.Action actionConfrim = actionBuilder2.build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.blue_pill)
                .setColor(Color.rgb(53, 153, 219))
                .setContentTitle(getResources().getString(R.string.notification_title))
                .setContentText(getResources().getString(R.string.notification_content))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(2) //priority_max
                .setAutoCancel(true)
                .setSound(null)
                .addAction(actionClose)
                .addAction(actionConfrim)
                .setDeleteIntent(deletePIntent);

        Notification notification = builder.build();

        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(Constants.ALARM_SERVICE_ID, notification);
    }
}
