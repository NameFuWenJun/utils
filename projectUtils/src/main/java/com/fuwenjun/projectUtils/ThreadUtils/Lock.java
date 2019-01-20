package com.fuwenjun.projectUtils.ThreadUtils;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 定义一个自己的显示锁,通过对sychornized进行封装
 * 实现一个可设置阻塞时长以及可中断线程(对于synchronized关键字:如果为了获取某个monitor锁而进入阻塞状态,那么是无法中断的)
 * @author Administrator
 *本例来源于java并发线程详解
 */
public interface Lock {
	void lock() throws InterruptedException;
	/**
	 * 含有阻塞时长的阻塞方法
	 * @param mills
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	void lock(long mills) throws InterruptedException,TimeoutException;
	
	void unlock();
	
	List<Thread> getBlockedThreads();
}
