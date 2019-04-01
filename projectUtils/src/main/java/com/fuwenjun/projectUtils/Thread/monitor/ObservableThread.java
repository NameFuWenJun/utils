package com.fuwenjun.projectUtils.Thread.monitor;


public class ObservableThread<T> extends Thread implements Observable{
	
	private final TaskLifecycle<T> lifeCycle;
	
	private final Task<T> task;
	
	private Cycle cycle;
	
	public ObservableThread(Task<T> task) {
		this(new TaskLifecycle.EmptyLifeCycle<>(), task);
	}
	public ObservableThread(TaskLifecycle<T> lifeCycle,Task<T> task) {
		super();
		if(task==null){
			throw new IllegalArgumentException("The task is required");
		}
		this.lifeCycle = lifeCycle;
		this.task=task;
	}

	@Override
	public final void run() {
		//线程开始
		this.update(Cycle.START, null, null);
		try{
			this.update(Cycle.RUNNING, null, null);
			
			T result=this.task.call();
			this.update(Cycle.DONE, result, null);
		}catch (Exception e){
			this.update(Cycle.ERORR, null, e);
		}
	}
	
	private void update(Cycle cycle,T result,Exception e){
		this.cycle=cycle;
		if(lifeCycle==null){
			return ;
		}
		try{
			switch(cycle){
			case START:
				this.lifeCycle.onStart(currentThread());
				break;
			case RUNNING:
				this.lifeCycle.onRunning(currentThread());
				break;
			case DONE:
				this.lifeCycle.onFinish(currentThread(), result);
				break;
			case ERORR:
				this.lifeCycle.onError(currentThread(),e);
				break;
			
			}
		}catch (Exception ex) {
			if(cycle == Cycle.ERORR){
				throw ex;
			}
		}
	}
	
	
	@Override
	public Cycle getCycle() {
		return this.cycle;
	}
	
}
