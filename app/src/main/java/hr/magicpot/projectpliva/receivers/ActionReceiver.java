package hr.magicpot.projectpliva.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import hr.magicpot.projectpliva.services.ActionService;
import hr.magicpot.projectpliva.services.AlarmService;

/**
 * Created by xxx on 11.4.2016..
 */
public class ActionReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ActionService.class);
        i.putExtra("action", intent.getStringExtra("action"));
        context.startService(i);
    }
}
