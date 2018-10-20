package com.gvjay.classmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RebootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WorkerManager.restartAllWorkers();
        Toast.makeText(context, "WorkerManager Restarted!!", Toast.LENGTH_LONG).show();
    }
}
