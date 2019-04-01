package com.fuwenjun.projectUtils.Thread.monitor;

public interface Task<T> {
	//任务接口运行有返回值
	T call();
}
