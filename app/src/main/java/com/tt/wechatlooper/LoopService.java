package com.tt.wechatlooper;


import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.tt.wechatlooper.util.AdbUtil;
import com.tt.wechatlooper.util.FileUtil;
import com.tt.wechatlooper.util.PerformUtil;
import com.tt.wechatlooper.util.sp.SPUtil;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Beta-Tan
 * CreateTime: 2017/2/23
 * Description:轮询公众号文章的辅助类
 */
public class LoopService extends BaseService {

    private HashSet<String> accountsSet = new HashSet<>();
    //未遍历到的节点
    private Set<String> unReachedAccounts;
    //查找公众号时 判断这一屏有没有这个节点
    private boolean firstIn = true;
    public float SPEED_FACTOR = 1;
    public static boolean switchState = SPUtil.getSwitchState();
    private static final String TAG = "LoopService";
    public WechatVersion wechatVersion = App.getInstance().version;

    //关机时间和开机时间
    public int closeTime = 0;
    public int openTime = 6;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //STEP 1 点击公众号页

        closeDialogIfOpen(event);


        if (firstIn) {
            startMission();
        }

        if (firstIn && isClickOfficialPage(event)) {
            //初始化数据
            accountsSet = SPUtil.getAccountsName();
            SPEED_FACTOR = SPUtil.getSpeedFactor();
            firstIn = false;
            if (accountsSet == null) {
                //没有公众号节点，就先寻找所有节点
                FileUtil.saveLog("进入公众号页,先查询所有公众号节点名称");
                initAccounts();
            } else {
                FileUtil.saveLog("进入公众号页,读取已记录的所有公众号节点，直接启动轮询");
                //开始轮训
                startPolling();
            }
        }
    }


    private synchronized void closeDialogIfOpen(AccessibilityEvent event) {
        AccessibilityNodeInfo info = getRootInActiveWindow();
        if (info == null) {
            return;
        }

        List<AccessibilityNodeInfo> info1 = info.findAccessibilityNodeInfosByViewId(wechatVersion.id_cancelUpdate);
        if (info1.size() == 0) {
            return;
        }

        Log.e(TAG, "找到弹窗啦:" + event.toString());
        List<AccessibilityNodeInfo> info2 = info.findAccessibilityNodeInfosByText("取消");
        if (info2.size() > 0) {
            PerformUtil.performClick(info2.get(0));
        }

    }

    //STEP 2 查询存在的节点

    private void initAccounts() {
        accountsSet = new HashSet<>();
        //查询所有节点
        do {
            sleep(4);
            //得到公众号页面的ListView节点
            AccessibilityNodeInfo listView = getListView();
            if (listView == null) {
                return;
            }
            //查询所有的节点
            findAccounts(listView);
        } while (scrollDown());

        //得到所有的节点回到顶部
        scrollTop();
        //保存数据供下次使用
        SPUtil.setAccountsName(accountsSet);
        //开始遍历
        startPolling();
    }


    //STEP 3 optional 开始遍历每一个节点
    private int recycledTime = 0;
    private boolean onReset = false;

    protected void startPolling() {
        unReachedAccounts = new HashSet<>();
        unReachedAccounts.addAll(accountsSet);
        sleep(1);
        while (true) {


            //如果是刚刚重启 就从上次崩溃的页面重新开始
            if (onReset) {
                onReset = false;
                findRecentPage();
            }


            for (String account : accountsSet) {
                checkAccount(account);
            }

            sleep(2);
            if (unReachedAccounts.size() == 0 || !scrollDown()) {
                recycledTime++;
                FileUtil.saveLog("第" + recycledTime + "次遍历完毕，开始下一次循环");
                scrollTop();
                unReachedAccounts.addAll(accountsSet);

                //0点到6点为休息时间
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Calendar c = Calendar.getInstance();
//                    int h = c.get(Calendar.HOUR);
//                    if (mTestService != null && h == 0) {
//                        Log.e(TAG, "夜深了，我要休息了");
//                        JobInfo.Builder builder = new JobInfo.Builder(id++, mServiceComponent);
//                        builder.setMinimumLatency(6 * 60 * 60 * 1000);
//                        builder.setOverrideDeadline(6 * 60 * 60 * 1000);
//                        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
//                        mTestService.scheduleJob(builder.build());
//                        return;
//                    }
//                }

            }
            sleep(2);

        }
    }

    //找 点通讯录和公众号进入
    private void startMission(int delaySeconds) {
        AccessibilityNodeInfo info = getRootInActiveWindow();
        if (info != null) {
            List<AccessibilityNodeInfo> info0 = info.findAccessibilityNodeInfosByText("通讯录");
            if (info0.size() > 0) {
                PerformUtil.performClick(info0.get(0));
            }
        }

        sleep(delaySeconds);

        info = getRootInActiveWindow();
        if (info != null) {
            //公众号
            List<AccessibilityNodeInfo> info1 = info.findAccessibilityNodeInfosByText("公众号");
            if (info1.size() > 0) {
                PerformUtil.performClick(info1.get(0));
            }
        }

    }

    private void startMission() {
        startMission(0);
    }


    private int count = 0;

    private void findAccounts(AccessibilityNodeInfo node) {

        if (node.getChildCount() == 0) {
            if (node.getText() != null && node.getText().toString().length() > 1) {
                String account = node.getText().toString();

                if (account.endsWith("个公众号")) {
                    return;
                }

                if (accountsSet.add(account)) {
                    count++;
                    FileUtil.saveLog("第" + count + "个节点:" + account);
                }
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    findAccounts(node.getChild(i));
                }
            }
        }

    }


    /**
     * 判断该时间是不是点击公众号事件
     *
     * @param event
     * @return
     */
    private boolean isClickOfficialPage(AccessibilityEvent event) {
        List<CharSequence> textList = event.getText();
        return textList.size() == 1 && "公众号".equals(textList.get(0).toString());
    }

    private void checkAccount(String accountName) {
        //看这一版有没有这个公众号
        if (!clickAccountName(accountName)) {
            //找不到这个节点就返回
            return;
        }
        SPUtil.setRecentAccount(accountName);

        sleep(2);
        //点击右上角
        FileUtil.saveLog(accountName + " 进入公众号详情，剩余节点数量:" + unReachedAccounts.size());
        //点击右上角的聊天信息按钮
        int temp0 = 0;
        while (!clickIntoOfficialDetail(getRootInActiveWindow())) {
            if (temp0 > 5) {
                break;
            }
            temp0++;

            if (temp0 > 3) {
                FileUtil.errorLog("点击聊天信息失败次数： " + temp0 + "--" + accountName);
            }
        }

        sleep(1);
        //点击历史信息按钮
        FileUtil.saveLog(accountName + " 点击历史信息");
        int temp1 = 0;
        while (!clickHistory()) {
            if (temp1 > 5) {
                break;
            }
            temp1++;
            if (temp1 > 3) {
                FileUtil.errorLog("点击历史信息失败次数： " + temp1 + "--" + accountName);
            }
            //没点到历史信息就滑一下
            sleep(1);
            PerformUtil.findListViewAndScrollOnce(getRootInActiveWindow());
            sleep(1);
        }


        FileUtil.saveLog(accountName + " 轮询返回");
        int temp2 = 0;
        do {
            temp2++;

            if (temp2 > 5) {
                reset();
                return;
            }
            sleep(2);
            PerformUtil.performBack(this);
            if (temp2 > 3) {
                FileUtil.errorLog("点击返回次数:" + temp2 + "--" + accountName);
            }
        } while (!isAtHomePage());

        //检查是否应该休息
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == closeTime) {

            while (true) {
                try {
                    Thread.sleep(1000 * 60 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == openTime) {
                    break;
                }
            }

        }

    }

    /**
     * @param accountName 公众账户名
     * @return
     */
    public boolean clickAccountName(String accountName) {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) {
            return false;
        }

        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText(accountName);
        if (list.size() == 0) {
            //没找到这个节点
            return false;
        }

        if (!unReachedAccounts.contains(accountName)) {
            //这个节点已经遍历过了
            return false;
        }

        AccessibilityNodeInfo info = list.get(0);
        if (!accountName.equals(info.getText().toString())) {
            //模糊匹配也不行 必须精确
            return false;
        }

        if (!PerformUtil.performClick(info)) {
            //点击失败也不行
            return false;
        }

        unReachedAccounts.remove(accountName);
        return true;
    }


    private boolean clickIntoOfficialDetail(AccessibilityNodeInfo node) {
        if (node == null) {
            return false;
        }

        if (node.getChildCount() == 0) {
            if ("聊天信息".equals(node.getContentDescription())) {
                return PerformUtil.performClick(node);
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    if (clickIntoOfficialDetail(node.getChild(i))) {
                        return true;
                    }
                }
            }
            return false;
        }

        return false;
    }

    private boolean clickHistory() {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) {
            return false;
        }

        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText("查看历史消息");
        return list.size() == 1 && PerformUtil.performClick(list.get(0));
    }


    private void sleep(double seconds) {
        try {
            Thread.sleep((long) (SPEED_FACTOR * 1000 * seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //找到上次所在的界面
    private void findRecentPage() {
        String accountName = SPUtil.getRecentAccount();
        FileUtil.saveLog("上次浏览的公众号是: " + accountName);
        if (TextUtils.isEmpty(accountName)) {
            return;
        }

        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) {
            return;
        }

        while (true) {
            List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText(accountName);
            if (list.size() != 0) {
                return;
            }

            if (!scrollDown()) {
                return;
            }
            sleep(2);
        }


    }


    /**
     * @return
     */
    private boolean isAtHomePage() {
        sleep(1);
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) {
            return false;
        }
        //公众号列表页有这三个ID的标签
        List<AccessibilityNodeInfo> node0 = root.findAccessibilityNodeInfosByViewId(wechatVersion.id_lv);
        List<AccessibilityNodeInfo> node1 = root.findAccessibilityNodeInfosByViewId(wechatVersion.id_check);
        if (node0.size() * node1.size() == 0) {
            return false;
        }


        FileUtil.saveLog("返回完成到公众号主页");
        return true;
    }


    //干掉微信
    private void reset() {

        onReset = true;
        FileUtil.errorLog("重启");
        //干掉微信
        AdbUtil.killWechat();
        sleep(4);
        firstIn = true;
        recycledTime = 0;
        unReachedAccounts.clear();

        //拉起微信
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        try {
            startActivity(intent);
            sleep(4);
            startMission(2);
        } catch (Exception e) {
            Toast.makeText(this, "请安装微信", Toast.LENGTH_SHORT).show();
        }
    }


}