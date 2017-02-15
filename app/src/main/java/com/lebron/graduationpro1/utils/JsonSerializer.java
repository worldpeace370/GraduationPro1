package com.lebron.graduationpro1.utils;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

/**
 * 用jackson库用于将java对象json化的类
 * Created by wuxiangkun on 2017/2/15.
 * Contact way wuxiangkun2015@163.com
 */

public class JsonSerializer {
    private static final String TAG = JsonSerializer.class.getName();

    private static JsonSerializer instance = new JsonSerializer();

    private ObjectMapper impl;

    private JsonSerializer() {
        impl = new ObjectMapper();
        impl.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static JsonSerializer getInstance() {
        if (instance == null) {
            instance = new JsonSerializer();
        }
        return instance;
    }

    public ObjectMapper getMappter() {
        return impl;
    }

    /**
     * 将任何对象json序列化为字符串
     *
     * @param object 任何对象
     * @return 返回json序列化后的字符串
     */
    public String serialize(Object object) {
        try {
            return impl.writeValueAsString(object);
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 根据json字符串 反序列化为java对象
     *
     * @param json  json字符串
     * @param clazz 类.class
     * @param <T>   类的泛型
     * @return 返回java对象
     */
    public <T> T deserialize(String json, Class<T> clazz) {
        try {
            return impl.readValue(json, clazz);
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 根据reader对象 反序列化为java对象
     *
     * @param reader reader对象
     * @param clazz  类.class
     * @param <T>    类的泛型
     * @return 返回java对象
     */
    public <T> T deserialize(Reader reader, Class<T> clazz) {
        try {
            return impl.readValue(reader, clazz);
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 根据json字符串 反序列化为java集合对象
     *
     * @param json       json字符串
     * @param collection 集合类.class
     * @param data       数据类.class
     * @param <T>        集合类泛型
     * @param <V>        数据类泛型
     * @return 返回集合类对象
     */
    public <T extends Collection<?>, V> Object deserialize(String json,
                                                           Class<T> collection, Class<V> data) {
        try {
            return impl.readValue(json,
                    impl.getTypeFactory().constructCollectionType(collection, data));
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 不吞掉异常
     *
     * @param reader
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T deserializeFrankly(Reader reader, Class<T> clazz) throws IOException, JsonParseException, JsonMappingException {
        return impl.readValue(reader, clazz);
    }
}
