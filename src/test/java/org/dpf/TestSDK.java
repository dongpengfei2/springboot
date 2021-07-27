package org.dpf;

import com.volcano.unicorn.sdk.UnicornSDK;
import com.volcano.unicorn.sdk.bean.UcRegisterInfo;
import com.volcano.unicorn.sdk.bean.base.UcEventInfo;
import com.volcano.unicorn.sdk.bean.UcLoginInfo;
import com.volcano.unicorn.sdk.bean.base.UcOfflineEventInfo;
import com.volcano.unicorn.sdk.bean.base.UcPropertyInfo;
import com.volcano.unicorn.sdk.config.UcConfigInfo;
import com.volcano.unicorn.sdk.exception.UcError;
import com.volcano.unicorn.sdk.exception.UcFullException;
import com.volcano.unicorn.sdk.utils.UGDIDUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestSDK {

    static UcConfigInfo configInfo = new UcConfigInfo();

    static {
        configInfo.setAppId(1);
        configInfo.setSandbox(true);
        configInfo.setHttpTimeout(60000);
        UnicornSDK.startup(configInfo, new UcFullException.funcImpl(){

            @Override
            public void onError(UcError ucError, String resMessage) {
                System.out.println("onError: " + resMessage);
            }
        });
    }

    public void testOfflineEvent()
    {
        UcOfflineEventInfo info = new UcOfflineEventInfo("mikechao123123","10000011","login");
        info.put("loginWay",123123);
        info.put("asdad11","dasdada");
        UnicornSDK.track(info);
    }
    /**
     * 测试用户登陆后发送事件打点数据
     * @throws InterruptedException
     */
    public void testEvent() throws InterruptedException {
        UcConfigInfo configInfo = new UcConfigInfo();
//        String ugdid = UnicornSDK.generateUGDID();
        String ugdid = "device-002";
        String roleId = "role-001";
        //注册
        UcRegisterInfo registerInfo = new UcRegisterInfo(ugdid);
        registerInfo.put("temp", 1);
        UnicornSDK.trackRegister(registerInfo);
        //登陆
        UcLoginInfo loginInfo = new UcLoginInfo(ugdid, roleId);
        loginInfo.put("ip", "192.168.0.1");
        UnicornSDK.trackLogin(loginInfo);
        Thread.sleep(1000);
//        //登陆2
//        UcLoginInfo loginInfo2 = new UcLoginInfo(ugdid, roleId);
//        loginInfo2.put("ip", "192.168.0.1");
//        UnicornSDK.trackLogin(loginInfo2);
//        Thread.sleep(1000);
        //任务完成
        for (int i=0;i<100;i++){
            UcEventInfo eventInfo = new UcEventInfo(ugdid, "questComplete");
            eventInfo.put("questId", 1);
            eventInfo.put("order", 1);
            eventInfo.put("nextQuestID", i);
            UnicornSDK.track(eventInfo);
            Thread.sleep(10);
        }
        Thread.sleep(1000);
        //单位使用技能
        for (int i=0;i<100;i++){
            UcEventInfo eventInfo = new UcEventInfo(ugdid, "unitUseSkill");
            eventInfo.put("unitUseSkill", 2);
            eventInfo.put("skillLevel", 2);
            eventInfo.put("targetInstanceId", 2);
            eventInfo.put("weaponId", i);
            UnicornSDK.track(eventInfo);
            Thread.sleep(10);
        }
        //单位添加buff
        for (int i=0;i<100;i++){
            UcEventInfo eventInfo = new UcEventInfo(ugdid, "unitAddBuff");
            eventInfo.put("buffId", 3);
            eventInfo.put("buffLevel", 3);
            eventInfo.put("instanceId", i);
            UnicornSDK.track(eventInfo);
            Thread.sleep(10);
        }
        //登出
        Thread.sleep(100);
        UnicornSDK.trackLogout(ugdid, 3, true);
        Thread.sleep(5000);
    }

    public void testProperty() throws InterruptedException {
//        String gudid = UGDIDUtils.generateUGDID();
        String gudid = "device-001";
        String roleId = "role-001";
        //登陆
        UcLoginInfo loginInfo = new UcLoginInfo(gudid, roleId);
        loginInfo.put("ip", "192.168.0.1");
        UnicornSDK.trackLogin(loginInfo);
        Thread.sleep(100);
        int size = 2000;
        while (size-- > 0){
            UcPropertyInfo propertyInfo = new UcPropertyInfo(gudid);
            propertyInfo.put("serverId", 100 - size);
            propertyInfo.put("playerId", 1);
            UnicornSDK.track(propertyInfo);
        }
        Thread.sleep(10000);
        UnicornSDK.shutdown();
    }

    public void testPayment() {

    }

    public static String[] arr = new String[]{"a", "b", "c"};
    public void testStack() {
        String gudid = UGDIDUtils.generateUGDID();
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("version", "1.28.1");
        extraMap.put("ip", "127.0.0.1");
        try {
//            String i = arr[3];
            int i = 1/0;
        } catch (Exception e) {
            UnicornSDK.errorStack(gudid, e, null);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TestSDK testSDK = new TestSDK();
        testSDK.testEvent();
//        for (int i=0;i<10;i++) {
//            testSDK.testStack();
//            System.out.println(i);
//            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(10));
//        }
        UnicornSDK.shutdown();
    }
}
