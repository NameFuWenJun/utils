package com.fuwenjun.projectUtils.Thread.monitor;

public interface TaskLifecycle<T> {
	
	 void onStart(Thread t);
	 
	 void onRunning(Thread t);
	 
	 void onFinish(Thread t, T result);
	 
	 void onError(Thread t,Exception e);
	 //提供一个空的实现
	 class EmptyLifeCycle<T> implements TaskLifecycle<T>{

		@Override
		public void onStart(Thread t) {
			
			
		}

		@Override
		public void onRunning(Thread t) {
			
			
		}

		@Override
		public void onFinish(Thread t, T result) {
			
			
		}

		@Override
		public void onError(Thread t, Exception e) {
			
			
		}
		 
	 }
}
