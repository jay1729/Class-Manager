package com.gvjay.classmanager;

import androidx.work.WorkManager;

public class WorkerManager {
    private static void stopAllWorkers(){
        WorkManager workManager = WorkManager.getInstance();
        workManager.cancelAllWorkByTag(WorkerDispatcher.WORK_TAG);
        workManager.cancelAllWorkByTag(AttendanceEntryWorker.WORK_TAG);
        workManager.cancelAllWorkByTag(AttendanceNotificationWorker.WORK_TAG);
    }

    private static void startAllWorkers(){
        WorkerDispatcher.enqueueNextWorkerDispatcher(0);
    }

    public static void restartAllWorkers(){
        stopAllWorkers();
        startAllWorkers();
    }
}
