package com.tt.wechatlooper;

/**
 * Author: Beta-Tan
 * CreateTime: 2017/4/5
 * Description:
 */
public enum WechatVersion {


    v6_5_4("com.tencent.mm:id/sk", "com.tencent.mm:id/sl","com.tencent.mm:id/abf"),
    v6_5_7("com.tencent.mm:id/t0", "com.tencent.mm:id/t1","I_DONT_KNOW");



    //公众号列表页ListViewId
    public String id_lv;
    //公众号列表页右边的导航条Id
    public String id_check;

    //取消更新按钮
    public final String id_cancelUpdate;

    WechatVersion(String id_lv, String id_check, String id_cancelUpdate) {
        this.id_lv = id_lv;
        this.id_check = id_check;
        this.id_cancelUpdate=id_cancelUpdate;
    }
}
