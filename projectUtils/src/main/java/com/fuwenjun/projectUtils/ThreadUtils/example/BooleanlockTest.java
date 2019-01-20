package com.fuwenjun.projectUtils.ThreadUtils.example;

import static java.util.concurrent.ThreadLocalRandom.current;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

import static java.lang.Thread.currentThread;

import com.fuwenjun.projectUtils.ThreadUtils.BooleanLock;
import com.fuwenjun.projectUtils.ThreadUtils.Lock;


/**
 * BooleanLock 示例
 * @author Administrator
 *
 */
public class BooleanlockTest {
	private final Lock lock =new BooleanLock();
	
	public void syncMehod(){
		//加锁
		try {
			lock.lock();
			int randomInt=current().nextInt(10);
			System.out.println(currentThread()+" get the lock.");
			TimeUnit.SECONDS.sleep(randomInt);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			//确定锁会释放
			lock.unlock();
		}
	}
	public void syncMethodTimeoutable(){
			try {
				lock.lock(1000);
				int randomInt=current().nextInt(10);
				System.out.println(currentThread()+" get the lock.");
				TimeUnit.SECONDS.sleep(randomInt);
			} catch (InterruptedException|TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
	}
	public static void main(String[] args) throws InterruptedException {
	/*	BooleanlockTest blt=new BooleanlockTest();
		IntStream.range(0, 10).mapToObj(i -> new Thread(blt::syncMehod)).forEach(Thread::start);*/
		
		
		//可被中断属性
		/*BooleanlockTest blt=new BooleanlockTest();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				blt.syncMehod();
			}
		},"T1").start();
		TimeUnit.SECONDS.sleep(2);
		Thread t2=new Thread(blt::syncMehod,"T2");
		t2.start();
		TimeUnit.SECONDS.sleep(10);
		t2.interrupt();*/
		
		
		//带有阻塞时间
		BooleanlockTest blt=new BooleanlockTest();
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				blt.syncMehod();
			}
		},"T1").start();
		TimeUnit.SECONDS.sleep(2);
		Thread t2=new Thread(blt::syncMethodTimeoutable,"T2");
		t2.start();
		TimeUnit.SECONDS.sleep(10);
		
	}
}
