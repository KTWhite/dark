package com.github.dark.config;

import com.github.dark.Constants.CommonConstants;
import com.github.dark.utils.StringHelper;
import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.runner.RunWith;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BaseContextHandler {
    public static ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<Map<Object, Object>>();

    public static void set(Object key, Object value) {
        Map<Object, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<Object, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key){
        Map<Object, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<Object, Object>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static String getUserID(){
        Object value = get(CommonConstants.CONTEXT_KEY_USER_ID);
        return returnObjectValue(value);
    }

    public static String getUsername(){
        Object value = get(CommonConstants.CONTEXT_KEY_USERNAME);
        return returnObjectValue(value);
    }


    public static String getName(){
        Object value = get(CommonConstants.CONTEXT_KEY_USER_NAME);
        return StringHelper.getObjectValue(value);
    }

    public static String getToken(){
        Object value = get(CommonConstants.CONTEXT_KEY_USER_TOKEN);
        return StringHelper.getObjectValue(value);
    }

    public static String getUserRole(){
        Object value = get(CommonConstants.CONTEXT_KEY_USER_ROLE);
        return StringHelper.getObjectValue(value);
    }

    public static String getDepartID() {
        Object value = get("currentDepartId");
        return returnObjectValue(value);
    }

    public static void setDepartID(String departID) {
        set("currentDepartId", departID);
    }
    public static void setToken(String token){set(CommonConstants.CONTEXT_KEY_USER_TOKEN,token);}

    public static void setName(String name){set(CommonConstants.CONTEXT_KEY_USER_NAME,name);}

    public static void setUserID(String userID){
        set(CommonConstants.CONTEXT_KEY_USER_ID,userID);
    }

    public static void setUsername(String username){
        set(CommonConstants.CONTEXT_KEY_USERNAME,username);
    }

    private static String returnObjectValue(Object value) {
        return value==null?null:value.toString();
    }

    public static void setUserRole(String userRole){
        set(CommonConstants.CONTEXT_KEY_USER_ROLE,userRole);
    }

    public static void remove(){
        threadLocal.remove();
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class UnitTest {
        private Logger logger = LoggerFactory.getLogger(UnitTest.class);

        @Test
        public void testSetContextVariable() throws InterruptedException {
            BaseContextHandler.set("test", "main");
            new Thread(()->{
                BaseContextHandler.set("test", "moo");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assertEquals(BaseContextHandler.get("test"), "moo");
                logger.info("thread one done!");
            }).start();
            new Thread(()->{
                BaseContextHandler.set("test", "moo2");
                assertEquals(BaseContextHandler.get("test"), "moo2");
                logger.info("thread two done!");
            }).start();

            Thread.sleep(5000);
            assertEquals(BaseContextHandler.get("test"), "main");
            logger.info("main one done!");
        }

        @Test
        public void testSetUserInfo(){
            BaseContextHandler.setUserID("test");
            assertEquals(BaseContextHandler.getUserID(), "test");
            BaseContextHandler.setUsername("test2");
            assertEquals(BaseContextHandler.getUsername(), "test2");
        }
    }
}
