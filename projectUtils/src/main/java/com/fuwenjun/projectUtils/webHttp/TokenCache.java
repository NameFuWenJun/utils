package com.fuwenjun.projectUtils.webHttp;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 保存规则需要拿到结果的时间,以及规则类型,周期任务的周期或者阈值触发任务的检查周期
 * key为ruleId
 *
 */
public class TokenCache {
	
	private Cache<String, String> tokenCache = null;
	
	private static final long MAX_SIZE = 1000;
	
	private static final long EXPIRED_TIME = 12;
	
	private static TokenCache cache = null;
	
	public static TokenCache getExcuteTimeCache(){
		if(cache == null){
			synchronized(TokenCache.class){
				if(cache == null){
					cache = new TokenCache();
				}
			}
		}
		return cache;	
	}
	
	private TokenCache(){
		this.tokenCache = CacheBuilder
				.newBuilder()
				.maximumSize(MAX_SIZE)
				.expireAfterAccess(EXPIRED_TIME, TimeUnit.HOURS)
				.build();
	}
	
	/**
	 * userId是否存在缓存中
	 * @param ruleId
	 * @return
	 */
	public boolean hasCache(String userId){
		return tokenCache.getIfPresent(userId) != null;
	}
	
	/**
	 * 获取缓存
	 */
	public String getValue(String userId){
		return tokenCache.getIfPresent(userId);
	}
	
	/**
	 * 移除缓存
	 * @param ruleId
	 */
	public void remove(String userId){
		tokenCache.invalidate(userId);
	}
	
	/**
	 * 更新缓存
	 */
	public void update(String userId,String value){
		tokenCache.invalidate(userId);
		tokenCache.put(userId, value);
	}
	
	/**
	 * 添加缓存
	 */
	public void put(String userId,String value){
		tokenCache.put(userId, value);
	}
}
