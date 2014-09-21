package P2Pfile.Peer3;

import resource.IndexServer;

public class Peer3Server implements Runnable{
	
	public Peer3Server(){
		
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		IndexServer is = new IndexServer(3003,"/src/P2Pfile/Peer3/filefolder" );
		System.out.println("Peer3Server Start");
		is.ConnectionAccept();
		
	
		
	}
}