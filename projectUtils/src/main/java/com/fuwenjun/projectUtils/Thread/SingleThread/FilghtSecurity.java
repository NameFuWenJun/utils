package com.fuwenjun.projectUtils.Thread.SingleThread;

public class FilghtSecurity {
	private int count =0;
	//dengjipai
	private String boardingPass="null"; 
	
	private String  idCard= "null";
	
	public synchronized void pass(String boardingPass,String idCard){
		System.out.println("check ");
		this.boardingPass=boardingPass;
		this.idCard=idCard;
		this.count++;
		check();
	}

	private void check() {
		if(boardingPass.charAt(0)!=idCard.charAt(0))
			throw new RuntimeException("===exception==="+toString());
		
		System.out.println("check success ");
	}

	@Override
	public String toString() {
		return "the " +count+"passenger ,boarding Pass ["+boardingPass+","+idCard+"]" ;
	}

}
