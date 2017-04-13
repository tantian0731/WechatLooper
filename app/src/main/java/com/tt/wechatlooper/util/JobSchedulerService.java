package com.tt.wechatlooper.util;

/**
 * Author: Beta-Tan
 * CreateTime: 2017/3/21
 * Description: 定时器
 */

public class JobSchedulerService {

}

//public class JobSchedulerService extends JobService {
//    private static final String TAG = "MyJobService";
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Messenger callback = intent.getParcelableExtra("messenger");
//        Message m = Message.obtain();
//        m.what = LoopService.MSG_SERVICE_OBJ;
//        m.obj = this;
//        try {
//            Log.e(TAG, "onStartCommand: ");
//            callback.send(m);
//        } catch (RemoteException e) {
//            Log.e(TAG, "Error passing service object back to activity.");
//        }
//        return START_NOT_STICKY;
//    }

//    @Override
//    public boolean onStartJob(JobParameters params) {
////        jobParamsMap.add(params);
//        if (mService != null) {
//            mService.onReceivedStartJob(params);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onStopJob(JobParameters params) {
//        // Stop tracking these job parameters, as we've 'finished' executing.
////        jobParamsMap.remove(params);
//        if (mService != null) {
//            mService.onReceivedStopJob();
//        }
//        return true;
//    }

//    BaseService mService;
//    private final LinkedList<JobParameters> jobParamsMap = new LinkedList<>();

//    public void setUiCallback(BaseService service) {
//        mService = service;
//    }


    /**
     * Send job to the JobScheduler.
     */
//    public void scheduleJob(JobInfo t) {
//        if (mService == null) {
//            return;
//        }
//
//        Log.d(TAG, "Scheduling job");
//        JobScheduler tm =
//                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        tm.schedule(t);
//    }

//    * Not currently used, but as an exercise you can hook this
//     * up to a button in the UI to finish a job that has landed
//     * in onStartJob().
//     */
//    public boolean callJobFinished() {
//        JobParameters params = jobParamsMap.poll();
//        if (params == null) {
//            return false;
//        } else {
//            jobFinished(params, false);
//            return true;
//        }
//    }
//
//}
