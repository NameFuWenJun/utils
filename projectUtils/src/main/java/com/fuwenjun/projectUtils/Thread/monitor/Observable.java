package com.fuwenjun.projectUtils.Thread.monitor;

public interface Observable {
	enum Cycle{
		START,RUNNING,DONE,ERORR
	}
	//获取当前生命周期
	Cycle getCycle();
	
	void start();
	
	void interrupt();
	
}
