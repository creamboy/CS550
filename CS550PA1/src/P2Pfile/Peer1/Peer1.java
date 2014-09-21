package P2Pfile.Peer1;

import resource.*;

import java.util.*;
import java.io.*;


@SuppressWarnings("serial")
public class Peer1 implements Serializable{
	
	public static void main(String[] args) throws Exception{
	String filename=null;
    ArrayList<Peer> pl=null;	
	ArrayList<String> filelist=null;
	ClientRemote cr = new ClientRemote("127.0.0.1", 3000, "/src/P2Pfile/Peer1/filefolder");
	new Thread(new Peer1Server()).start();	
	filelist = cr.filelist("/src/P2Pfile/Peer1/filefolder");
	Peer p1=new Peer("peer1","/src/P2Pfile/Peer1/filefolder",filelist,"127.0.0.1",3001);
	Message ms= new Message("String", p1);
	Message ms1= new Message ("Search File", "message");
	Message ms2= new Message("Download", p1, filename );
	
	int c=0;
	while(true){
		if(c==0){
			if(cr.PeerSearch(ms.MessagePackaging("Peer Search", p1))){
				System.out.println("Peer Exist, No need to register!");
				cr.setpeerexist(true);
			}else{
				System.out.println("Need to register this Peer!");
				cr.setpeerexist(false);
			}
		}else{
			if(cr.PeerSearch(ms.MessagePackaging("Peer Search", p1))){
				cr.setpeerexist(true);
			}
		}
		System.out.println("Enter the main menu, Please select ");
		if(cr.ifhaspeer()){
			System.out.println("AddFile");
			System.out.println("SearchFile");
			System.out.println("DownloadFile");
			System.out.println("DeleteFile");
			System.out.println("Exit");
		}
		else{
			System.out.println("Register");
			System.out.println("Exit");
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String text=br.readLine();
		if(cr.ifhaspeer()){
			if(text.equals("AddFile")){
				System.out.println("Adding a File");
				cr.AddExistingFile(ms.MessagePackaging("AddFile", p1));	
				p1.setFilelist(cr.filelist("/src/P2Pfile/Peer1/filefolder"));
			}
			else if(text.equals("SearchFile")){
				System.out.println("Search File, Please Input a name:");
				filename = br.readLine();
				pl=cr.searchFile(ms1.MessagePackaging("FileSearch", filename));
				System.out.println("The following peers contain this file:");
				for(int i=0; i<pl.size();i++){
					System.out.println(pl.get(i).getName());
				}
				
			}
			else if(text.equals("DownloadFile")){
				if(pl!=null){
					System.out.println("Which node you want to reach? peer1,peer2 or peer3");
					for(int m=0; m<pl.size();m++){
						if(pl.get(m).getName().equals(br.readLine())){
							System.out.println("Donwloading File");
							ClientRemote cr1= new ClientRemote(pl.get(m).getIpAddress(),pl.get(m).getListeningPort(),"/src/P2Pfile/Peer1/filefolder");
							cr1.downloadFile(ms2.MessagePackaging("Download", p1, filename));
							cr.AddExistingFile(ms.MessagePackaging("AddFile", p1));
							p1.setFilelist(cr.filelist("/src/P2Pfile/Peer1/filefolder"));
						    }else{
								System.out.println("This Peer dose not contain this file!");
								break;
							
							
						}
					}
					}else{
						System.out.println("Please Search A File First");
						break;
					
					
				}
				}				
			
			else if(text.equals("DeleteFile")){
				System.out.println("Delete a File");
				cr.DeleteFile(ms.MessagePackaging("DeleteFile", p1));
				p1.setFilelist(cr.filelist("/src/P2Pfile/Peer1/filefolder"));
			}
			else if(text.equals("Exit")){
				System.out.println("Exit the System");
				System.exit(0);
			}
			else{
				System.out.println("Wrong Input, please input again");
				
			}
		}else{
			if(text.equals("Register")){
				System.out.println("Register a Peer!"); 
				cr.Register(ms.MessagePackaging("PeerRegister", p1));
			}
			if(text.equals("Exit")){
				System.out.println("Exit the System");
				System.exit(0);
			}//else{
				//System.out.println("Wrong Input, please input again");
			//}
			}
		
		c++;	
		
	}
	}
}
	
	
		
