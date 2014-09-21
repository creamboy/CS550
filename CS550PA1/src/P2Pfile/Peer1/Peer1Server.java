package P2Pfile.Peer1;

import resource.*;


public class Peer1Server implements Runnable{
	
	public Peer1Server(){
		
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		IndexServer is = new IndexServer(3001,"/src/P2Pfile/Peer1/filefolder" );
		System.out.println("Peer1Server Start");
		is.ConnectionAccept();
		
	
		
	}
	
	
	
	

}
