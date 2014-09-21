package P2Pfile.Peer2;

import resource.IndexServer;

	public class Peer2Server implements Runnable{
		
		public Peer2Server(){
			
		}
		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			IndexServer is = new IndexServer(3002,"/src/P2Pfile/Peer2/filefolder" );
			System.out.println("Peer2Server Start");
			is.ConnectionAccept();
			
		
			
		}
			
	}
	
	
	
	
	
	
	
	
	
	
	

