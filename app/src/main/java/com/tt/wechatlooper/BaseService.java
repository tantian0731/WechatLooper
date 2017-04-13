package com.tt.wechatlooper;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.tt.wechatlooper.util.PerformUtil;

/**
 * Author: Beta-Tan
 * CreateTime: 2017/4/13
 * Description:
 */
public abstract class BaseService extends AccessibilityService {
    private static final String TAG = "BaseService";

    private WechatVersion version = App.getInstance().version;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }


    protected void scrollTop() {
        AccessibilityNodeInfo listView = getListView();
        while (scrollListView(listView, AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)) {

        }
    }

    protected boolean scrollUp() {
        return scrollListView(getListView(), AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }


    protected boolean scrollDown() {
        return scrollListView(getListView(), AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
    }


    protected boolean scrollListView(AccessibilityNodeInfo listView, int way) {
        return listView != null && listView.performAction(way);
    }

    protected AccessibilityNodeInfo getListView() {
        return PerformUtil.findViewById(getRootInActiveWindow(), version.id_lv);
    }

    protected abstract void startPolling();


    //---------------下面是我的定时器-------------//

    ComponentName mServiceComponent = null;
    public static final int MSG_START = 0;
    public static final int MSG_STOP = 1;
    public static final int MSG_SERVICE_OBJ = 2;
//    JobSchedulerService mTestService;


//    public void onReceivedStartJob(JobParameters params) {
//        Log.e(TAG, "onReceivedStartJob: ");
//        Message m = Message.obtain(mHandler, MSG_START);
//        mHandler.sendMessage(m);
//    }
//
//    public void onReceivedStopJob() {
//        Log.e(TAG, "onReceivedStopJob: ");
//        Message m = Message.obtain(mHandler, MSG_STOP);
//        mHandler.sendMessage(m);
//    }


//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_START:
//                    Log.e(TAG, "休息完毕，开始继续循环");
//                    startPolling();
//                    break;
//                case MSG_STOP:
//                    Log.e(TAG, "handleMessage: STOP");
//                    break;
//                case MSG_SERVICE_OBJ:
//                    mTestService = (`) msg.obj;
//                    mTestService.setUiCallback(BaseService.this);
//            }
//        }
//    };

//    @Override
//    protected void onServiceConnected() {
//        super.onServiceConnected();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mServiceComponent = new ComponentName(getApplicationContext(), JobSchedulerService.class);
//             Start service and provide it a way to communicate with us.
//            Intent startServiceIntent = new Intent(getApplicationContext(), JobSchedulerService.class);
//            startServiceIntent.putExtra("messenger", new Messenger(mHandler));
//            startService(startServiceIntent);
//        }
//    }
}


