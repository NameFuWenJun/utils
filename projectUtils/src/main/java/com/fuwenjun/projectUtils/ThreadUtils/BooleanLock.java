package com.fuwenjun.projectUtils.ThreadUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.apache.lucene.search.grouping.CollectedSearchGroup;

import static java.lang.Thread.currentThread;;

public class BooleanLock implements Lock{
	//当前未阻塞线程
	private Thread currentThread;
	//当前锁是否被获取
	private boolean locked=false;
	//当前阻塞线程
	private final List<Thread> blockedList=new ArrayList<Thread>(); 
	
	@Override
	public void lock() throws InterruptedException {
		synchronized(this){
			while(locked){
				//暂存当前线程
				final Thread tempThread=currentThread();
				try{
					if(blockedList.contains(currentThread())){
						blockedList.add(currentThread());
					}
					this.wait();
				}catch (InterruptedException e) {
					blockedList.remove(tempThread);
					throw e;
				}
			}
			blockedList.remove(currentThread());
			this.locked=true;
			this.currentThread=currentThread();
		}
	}
	@Override
	public void lock(long mills) throws InterruptedException, TimeoutException {
		synchronized (this) {
			if(mills<0){
				this.lock();
			}else{
				long remainmills=mills;
				long endMills=System.currentTimeMillis()+remainmills;
				while(locked){
					if(remainmills<0){
						throw new TimeoutException("can not get this lock during "+mills+" ms.");
					}
					if(!blockedList.contains(currentThread())){
						blockedList.add(currentThread());
					}
					remainmills=endMills-System.currentTimeMillis();
				}
				
				blockedList.remove(currentThread());
				this.locked=true;
				this.currentThread=currentThread();
			}
		}
	}
	@Override
	public void unlock() {
		synchronized(this){
			if(currentThread==currentThread()){
				this.locked=false;
				Optional.of(currentThread().getName()+" release the lock.").ifPresent(System.out::println);
				this.notifyAll();
			}
		}
	}
	@Override
	public List<Thread> getBlockedThreads() {
		//返回一个不可改变的list
		return Collections.unmodifiableList(blockedList);
	}
	
	
	

}
