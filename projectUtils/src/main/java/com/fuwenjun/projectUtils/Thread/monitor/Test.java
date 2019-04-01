package com.fuwenjun.projectUtils.Thread.monitor;

import java.util.concurrent.TimeUnit;

public class Test {
	
	private static void normaluse(){
		Observable observableThread=new ObservableThread<>(()->{
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("finished");
			return null;
		});
		observableThread.start();
	}
	private static void advanceUse(){
		TaskLifecycle<String> life=new TaskLifecycle.EmptyLifeCycle<String>() {
			@Override
			public void onStart(Thread t) {
				System.out.println("线程开始");
			}
			@Override
			public void onFinish(Thread t, String result) {
				System.out.println(t.getName()+" result is : "+result );
			}
			
			@Override
			public void onError(Thread t, Exception e) {
				System.out.println(t.getName() + e);
			}
		};
		//可以在task的call中获取返回值
		Observable observableThread=new ObservableThread<>(life,()->{
			try{
				TimeUnit.SECONDS.sleep(10);
			}catch (Exception e){
				e.printStackTrace();
			}
			System.out.println("finished");
			return "hello observe";
		});
		observableThread.start();
			
	}
	public static void main(String[] args) {
		advanceUse();
	}
}
