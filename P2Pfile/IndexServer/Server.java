package P2Pfile.IndexServer;

import resource.*;


public class Server {
	
	public static void main(String[] args){
		IndexServer is = new IndexServer(3000,"/src/P2Pfile/IndexServer/filefolder" );
		System.out.println("IndexServer Start");
		is.ConnectionAccept();
				
	}
	
	

	 
}
