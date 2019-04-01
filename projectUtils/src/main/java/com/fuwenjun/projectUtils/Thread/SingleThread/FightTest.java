package com.fuwenjun.projectUtils.Thread.SingleThread;

public class FightTest {
	static final class Passengers extends Thread{
		private final FilghtSecurity FILGHT_SECURITY;
		
		private final String idCard;
		
		private final String boardingPass;

		public Passengers(FilghtSecurity fILGHT_SECURITY, String idCard, String boardingPass) {
			super();
			FILGHT_SECURITY = fILGHT_SECURITY;
			this.idCard = idCard;
			this.boardingPass = boardingPass;
		}
		
		@Override
		public void run() {
			while(true){
				FILGHT_SECURITY.pass(boardingPass, idCard);
			}
		}
		
	}
	public static void main(String[] args) {
		final FilghtSecurity filghtSecurity=new FilghtSecurity();
		new Passengers(filghtSecurity, "A123", "Ar231").start();
		new Passengers(filghtSecurity, "B123","Bw123").start();
		new Passengers(filghtSecurity, "CE132", "C213").start();
	}

}	
