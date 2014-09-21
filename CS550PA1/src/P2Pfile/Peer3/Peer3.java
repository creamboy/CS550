package P2Pfile.Peer3;

import resource.*;

import java.util.*;
import java.io.*;


@SuppressWarnings("serial")
public class Peer3 implements Serializable{
	
	public static void main(String[] args) throws Exception{
	String filename=null;
    ArrayList<Peer> pl=null;	
	ArrayList<String> filelist=null;
	ClientRemote cr = new ClientRemote("127.0.0.1", 3000, "/src/P2Pfile/Peer3/filefolder");
	new Thread(new Peer3Server()).start();	
	filelist = cr.filelist("/src/P2Pfile/Peer3/filefolder");
	Peer p3=new Peer("peer3","/src/P2Pfile/Peer3/filefolder",filelist,"127.0.0.1",3003);
	Message ms= new Message("String", p3);
	Message ms1= new Message ("Search File", "message");
	Message ms2= new Message("Download", p3, filename );
	
	int c=0;
	while(true){
		if(c==0){
		if(cr.PeerSearch(ms.MessagePackaging("PeerSearch", p3))){
			System.out.println("Peer Exist, No need to register!");
			cr.setpeerexist(true);
		}
		else{
			System.out.println("Need to register this Peer!");
			cr.setpeerexist(false);
		}}
		else{
			if(cr.PeerSearch(ms.MessagePackaging("PeerSearch", p3))){
				cr.setpeerexist(true);
		}
		}
		System.out.println("Enter the main menu, Please select ");
		if(cr.ifhaspeer()){
			System.out.println("Add File");
			System.out.println("Search File");
			System.out.println("Download File");
			System.out.println("Delete File");
			System.out.println("Exit");
		}
		else{
			System.out.println("Register");
			System.out.println("Exit");
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		if(cr.ifhaspeer()){
			if(br.readLine().equals("Add File")){
				System.out.println("Adding a File");
				cr.AddExistingFile(ms.MessagePackaging("AddFile", p3));	
				p3.setFilelist(cr.filelist("/src/P2Pfile/Peer3/filefolder"));
			}
			else if(br.readLine().equals("Search File")){
				System.out.println("Search File, Please Input a name:");
				filename = br.readLine();
				pl=cr.searchFile(ms1.MessagePackaging("FileSearch", filename));
				System.out.println("Peers contain this file are:");
				for(int i=0; i<pl.size();i++){
					System.out.println(pl.get(i).getName());
				}
				
			}
			else if(br.readLine().equals("Download File")){
				if(pl!=null){
					System.out.println("Which node you want to reach?");
					for(int m=0; m<pl.size();m++){
						if(pl.get(m).getName().equals(br.readLine())){
							System.out.println("Donwloading File");
							ClientRemote cr3= new ClientRemote(pl.get(m).getIpAddress(),pl.get(m).getListeningPort(),"/src/P2Pfile/Peer3/filefolder");
							cr3.downloadFile(ms2.MessagePackaging("Download", p3, filename));
							cr.AddExistingFile(ms.MessagePackaging("AddFile", p3));
							p3.setFilelist(cr.filelist("/src/P2Pfile/Peer3/filefolder"));
						    }else{
								System.out.println("No Peer contains the file!");
								break;
							
							
						}
					}
					}else{
						System.out.println("Please Search A File First");
						break;
					
					
				}
				}				
			
			else if(br.readLine().equals("Delete File")){
				System.out.println("Delete a File");
				cr.DeleteFile(ms.MessagePackaging("DeleteFile", p3));
				p3.setFilelist(cr.filelist("/src/P2Pfile/Peer3/filefolder"));
			}
			else if(br.readLine().equals("Exit")){
				System.out.println("Exit the System");
				System.exit(0);
			}
			else{
				System.out.println("Wrong Input, please input again");
				
			}
		}else{
			if(br.readLine().equals("Register")){
			System.out.println("Register a Peer!");
			cr.Register(ms.MessagePackaging("PeerRegister", p3));	

			}
			if(br.readLine().equals("Exit")){
				System.out.println("Exit the System");			
				System.exit(0);
			}else{
				System.out.println("Wrong Input, please input again");
			}
			}
		
		c++;	
		
	}
	}
}