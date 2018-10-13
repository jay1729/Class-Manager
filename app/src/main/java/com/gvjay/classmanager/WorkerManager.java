package com.gvjay.classmanager;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Executor;

import androidx.work.WorkManager;

public class WorkerManager {
    public static void restartAllWorkers(){
        final WorkManager workManager = WorkManager.getInstance();
        workManager.cancelAllWorkByTag(WorkerDispatcher.WORK_TAG).addListener(new Runnable() {
            @Override
            public void run() {
                workManager.cancelAllWorkByTag(AttendanceEntryWorker.WORK_TAG).addListener(new Runnable() {
                    @Override
                    public void run() {
                        workManager.cancelAllWorkByTag(AttendanceNotificationWorker.WORK_TAG).addListener(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Next Worker Dispatcher", "Enqueued");
                                WorkerDispatcher.enqueueNextWorkerDispatcher(0);
                            }
                        }, new Executor() {
                            @Override
                            public void execute(@NonNull Runnable runnable) {
                                runnable.run();
                            }
                        });
                    }
                }, new Executor() {
                    @Override
                    public void execute(@NonNull Runnable runnable) {
                        runnable.run();
                    }
                });
            }
        }, new Executor() {
            @Override
            public void execute(@NonNull Runnable runnable) {
                runnable.run();
            }
        });
    }
}
