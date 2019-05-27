package com.cn.tianxia.data.base.redis;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import cn.hutool.json.JSONUtil;

/**
 * @ClassName RedisUtils
 * @Description redis工具类
 * @author Hardy
 * @Date 2019年5月3日 上午10:59:09
 * @version 1.0.0
 */
@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Object> hashOperations;
    @Resource(name = "redisTemplate")
    private ListOperations<String, Object> listOperations;
    @Resource(name = "redisTemplate")
    private SetOperations<String, Object> setOperations;
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, Object> zSetOperations;

    /** 默认过期时长，单位：秒 (24小时) */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;

    /** 不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    private static final String UNLOCK_LUA;

    /**
     * 缓存脚本
     */
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    public void set(String key, Object value, long expire) {
        valueOperations.set(key, toJson(value));
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * @Description put一个map
     * @param key
     * @param data
     */
    public void hset(String key, Map<String, Object> map) {
        hset(key, map, DEFAULT_EXPIRE);
    }

    public void hset(String key, Map<String, Object> map, long expire) {
        hashOperations.putAll(key, map);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void hmset(String key, Map<String, String> map) {
        hmset(key, map, DEFAULT_EXPIRE);
    }

    public void hmset(String key, Map<String, String> map, long expire) {
        hashOperations.putAll(key, map);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public String hget(String key, String field) {
        String value = (String) hashOperations.get(key, field);
        return value;
    }

    /**
     * @Description zset
     * @param key
     * @param value
     */
    public void sadd(String key, String value, long expire) {
        setOperations.add(key, value);
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public boolean isMember(String key, Object o) {
        return setOperations.isMember(key, o);
    }

    public void sadd(String key, String value) {
        sadd(key, value);
    }

    public void srem(String key, String value) {
        setOperations.remove(key, value);
    }

    public Set<Object> smembers(String key) {
        return setOperations.members(key);
    }

    /**
     * hgetAll
     *
     * @param key
     * @return
     */
    public Map<String, Object> hgetAll(String key) {
        return hashOperations.entries(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
                || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSONUtil.toJsonStr(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        return JSONUtil.toBean(json, clazz);
    }

    /** ===================================redis分布式锁操作方式=========================================== */
    /**
     * @Description 设置缓存分布式锁
     * @param lockKey
     *            锁key
     * @param uuid
     *            唯一标识符
     * @param expireTime
     *            过期时间
     * @return true 则没有锁 , false 则锁没有释放
     */
    public boolean hasLock(String lockKey, String uuid, int expireTime) {
        try {
            RedisCallback<Boolean> callback = (connection) -> {
                return connection.set(lockKey.getBytes(Charset.forName("UTF-8")),
                        uuid.getBytes(Charset.forName("UTF-8")),
                        Expiration.seconds(TimeUnit.SECONDS.toSeconds(expireTime)),
                        RedisStringCommands.SetOption.SET_IF_ABSENT);
            };
            boolean hasLock = redisTemplate.execute(callback);
            if(hasLock){
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * @Description 释放分布式锁
     * @param lockKey
     * @param uuid
     * @return
     */
    public boolean releaseLock(String lockKey, String uuid) {
        RedisCallback<Boolean> callback = (connection) -> {
            return connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN, 1,
                    lockKey.getBytes(Charset.forName("UTF-8")), uuid.getBytes(Charset.forName("UTF-8")));
        };
        return (Boolean) redisTemplate.execute(callback);
    }
}
