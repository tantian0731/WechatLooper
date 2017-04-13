package com.tt.wechatlooper.util;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Author: Beta-Tan
 * CreateTime: 2017/3/21
 * Description:
 */
public class PerformUtil {


    public static AccessibilityNodeInfo findViewById(AccessibilityNodeInfo node, String id) {
        if (node == null) {
            return null;
        }

        List<AccessibilityNodeInfo> list = node.findAccessibilityNodeInfosByViewId(id);
        if (list.size() != 1) {
            return null;
        }
        return list.get(0);
    }


    //模拟点击事件
    public static boolean performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }

        if (nodeInfo.isClickable()) {
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            AccessibilityNodeInfo parent = nodeInfo.getParent();
            return performClick(parent);
        }
    }

    //模拟返回事件
    public static void performBack(AccessibilityService service) {

        if (service == null) {
            return;
        }
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    /**
     * 找到ListView控件并滑动一次
     *
     * @param node
     */
    public static void findListViewAndScrollOnce(AccessibilityNodeInfo node) {
        if (node == null) {
            return;
        }

        if (node.getChildCount() != 0) {
            if (node.getClassName().toString().equals("android.widget.ListView")) {
                node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }

            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    findListViewAndScrollOnce(node.getChild(i));
                }
            }
        }
    }

}
