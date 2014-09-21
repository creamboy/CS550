package resource;

import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.channels.*;


public class HandleConnection implements Runnable {
     String location;
	Socket sockethandler;
	
	
	public HandleConnection(Socket sockethandler, String location) {
		// TODO Auto-generated constructor stub
		this.sockethandler=sockethandler;
		this.location=location;
	}
	
	//get a file list， all the file
	public ArrayList<String> getfilelist(){
		
	 File directory = new File (System.getProperty("user.dir"));
	 ArrayList<String> filelist = new ArrayList<String>();
	 
	 try {
		 directory= new File(directory.getAbsolutePath()+location);
		 File fl[]=directory.listFiles();
		 for (int i =0; i<fl.length; i++){
		  filelist.add(fl[i].getName());
			 
		 }
	 }catch(Exception e){
		 e.printStackTrace();
	 }
		
		return filelist;		
	}
	
	
	public Peer GetPeer(ArrayList<String> line){
		ArrayList<String> filelist = new ArrayList<String>();
		Peer p = new Peer(location, location, filelist, location, 0);
		p.setName(line.get(0));
		p.location=line.get(1);
		p.ListeningPort=Integer.parseInt(line.get(2));
		p.IpAddress=line.get(3);
		
		for(int i=4;i<line.size();i++){
			filelist.add(line.get(i));
		}
		
		p.filelist=filelist;
		
		return p;		
	}
	
	
	
	
	
	@Override
	public void run(){
		// TODO Auto-generated method stub
    
		//File Dir= new File (System.getProperty("user.dir"));

		
		
		
		try{ 
			
			ObjectOutputStream StreamWriter= new ObjectOutputStream(sockethandler.getOutputStream());
			ObjectInputStream StreamReader= new ObjectInputStream(sockethandler.getInputStream());
			Message messageOut = null;
			Message messageIn=(Message)StreamReader.readObject();
		    
			
			//handle the request
			
			
			
			/**
			 * PeerSearch
			 */
			
			
			if(messageIn.RequestFunction.equals("PeerSearch"));{
				Message ms = new Message ("PeerSearch", true);
				//record peer information
				File peerinfo= new File(System.getProperty("user.dir") + location + "/" + messageIn.peer.name + ".d");
				
				if(peerinfo.exists()){
					messageOut=ms.MessagePackaging("PeerSearch", true);
					StreamWriter.writeObject(messageOut);
					StreamWriter.flush();
					
				}
				else{
					System.out.println("handle search");
					Thread.sleep(13000);
					System.out.println(messageIn.peer.name + "needs to register first! ");
					messageOut=ms.MessagePackaging("PeerSearch", false);
					StreamWriter.writeObject(messageOut);
					//StreamWriter.flush();
					
				}
				
			}
			
			
			
			
			/**
			 * PeerRegister
			 */
			
			
			
		    if(messageIn.RequestFunction.equals("PeerRegister")){
		    	System.out.println("handleregister!!!!!!!!!!!!!!!");
				String request="PeerRegister";
		    	String message1="Register Peer Successfully!";
		    	String message2="Registering Unsuccessfully!";
		    	Message mg= new Message (request,message1);
				File dfile = new File(System.getProperty("user.dir")+ location + "/" + messageIn.peer.name +".d");
				if(dfile.createNewFile()){
					try{
						//appending to a file
						PrintWriter pw = new PrintWriter( new FileWriter(dfile,true));
						pw.println(messageIn.peer.name);
						pw.println(messageIn.peer.location);
					
					    String s = String.valueOf(messageIn.peer.ListeningPort);
						pw.println(s);
						pw.println(messageIn.peer.IpAddress);
						
						for(int i=0; i<messageIn.peer.filelist.size();i++){
							pw.println(messageIn.peer.filelist.get(i));
						}
						
						
						pw.close();
					
						
					messageOut= mg.MessagePackaging(request,message1);
					messageOut.message="Register Peer Successfully!";
				  // MessagePackaging("PeerRegister", "Registering Peer Successfully!");
					StreamWriter.writeObject(messageOut);
					StreamWriter.flush();
					
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				else{
					messageOut=mg.MessagePackaging(request,message2);
					StreamWriter.writeObject(messageOut);
					StreamWriter.flush();
				}			
			}
			
		    /**
		     * Downloading file
		     */
		    
		    if(messageIn.RequestFunction.equals("Download")){
		    	byte[][] tempbyte = null;
		    	int[] byteread = null ;
		    	Message mg= new Message(messageIn.RequestFunction,messageIn.filename,byteread, tempbyte);
		    	File file=new File("");
		    	file = new File(file.getAbsoluteFile()+location+"/"+messageIn.message);
		    	
		    	FileInputStream fis= new FileInputStream(file);
		    	FileChannel fc = fis.getChannel();
		    	FileLock flock = fc.tryLock();
		    	
		    	if(flock!=null){
		    	 System.out.println("server: Uploading File:" + messageIn.message);	
		         tempbyte= new byte[50000][50000];
		          byteread = new int[50000];
		    	 int m=0;
		    	 while((byteread[m]=fis.read(tempbyte[m]))!=-1){
		    		 m++;
		    	 }
		    	}
		    	messageOut=mg.MessagePackaging(messageIn.RequestFunction, messageIn.filename, byteread, tempbyte);
		    	StreamWriter.writeObject(messageOut);
		    	StreamWriter.flush();
		    	
		    	System.out.println("Finish Downloading file " + messageIn.filename);
		    	flock.release();
		    	fis.close();
		    		
			}
		    	
		  
		    	
		    	
		    	
		  /**
		   * Search a File
		   */
		    	
		   if (messageIn.RequestFunction.equals("FileSearch")){
			   String S=null;
			   Message me= new Message ("FileSearch","message");
			   ArrayList<String> filelist = this.getfilelist() ;
			   ArrayList<Peer> peerlist = new ArrayList<Peer> ();
			   ArrayList<String> line = new ArrayList<String>();
			   boolean find = false;
			   for(int i =0; i<filelist.size();i++){
				   File dfile= new File(System.getProperty("user.dir")+location + "/"+ filelist.get(i));
				   //read .d file
		     	  BufferedReader reader= new BufferedReader(new FileReader(dfile));
		     	  
		     	  while((S=reader.readLine())!=null){
				  line.add(S.replaceAll("\n", ""));
		     	  }
			   
			   
			   for(int n=4; n< line.size();n++){
				   if(line.get(n).equals(messageIn.message)){
					   peerlist.add(this.GetPeer(line));
					   System.out.println("File Found!");
					   find=true;
					   break;
				   }
			   }
			   
			   reader.close();
			   }
			   if(find==false){
				   
				   messageOut=me.MessagePackaging("FileSearch", "file is not found");
			   }
			   else{
				   messageOut=me.MessagePackaging("FileSearch", peerlist, "file is found");
			   }
			 StreamWriter.writeObject(messageOut);
			 StreamWriter.flush();		   
			   
		   }
	
		   
		   
		   /**
		    * Add a File
		    * 
		    */
		   
		   
		   if(messageIn.RequestFunction.equals("AddFile")){
			   String request= "AddFile";
			   String message1="Adding Successfully";
			   String message2="Adding Unsuccessfully";
					 
			   
			   Message me1= new Message (request,message1);
			  if(messageIn.message.equals("File Created")) {				  
				  File dfile = new File(System.getProperty("user.dir")+location+"/"+messageIn.peer.name + ".d");
				  PrintWriter pw = new PrintWriter(new FileWriter(dfile,true));
				  pw.println(messageIn.peer.filelist.get(messageIn.peer.filelist.size() -1));
				  pw.close();
				  
				  messageOut=me1.MessagePackaging(request,message1);
				  StreamWriter.writeObject(messageOut);
				  StreamWriter.flush();
			   }else{
   				messageOut=me1.MessagePackaging(request, message2); 
				   StreamWriter.writeObject(messageOut);
				   StreamWriter.flush();
			   }
			   
			   
		   }
		   
		
		   
		   
		   /**
		    * Delete a File
		    * 
		    */
		   
		   if(messageIn.RequestFunction.equals("DeleteFile")){
			   Message me2= new Message ("DeleteFile","message"); 
			if(messageIn.message.equals("Can not delete the file!")||messageIn.message.equals("Can not find the File")){
				messageOut=me2.MessagePackaging("DeleteFile", "No Change");
				StreamWriter.writeObject(messageOut);
				StreamWriter.flush();
			}else{
				File dfile = new File(System.getProperty("user.dir")+location+"/"+messageIn.peer.name + ".d");
				BufferedReader Br=new BufferedReader(new FileReader(dfile));
				String temp=null;
				ArrayList<String> line= new ArrayList<String>();
				while ((temp=Br.readLine())!=null){
					
					if(!temp.equals(messageIn.message)){
						line.add(temp);
					}
					
				}
				
				PrintWriter pw = new PrintWriter(new FileWriter(dfile));
				
				for(int n=0; n<line.size();n++){
					pw.println(line.get(n));
				}
				pw.close();
				messageOut = me2.MessagePackaging("DeleteFile", "Delete Complete from Server");
				StreamWriter.writeObject(messageOut);
				pw.close();
				Br.close();
			}
			   
			   
		   }
		    	
		    	
		 StreamWriter.close();   	
		  StreamReader.close();
		  
		
		    
		}catch(Exception e)
		{
			//System.out.println("ERROR Handling! ");
			//e.printStackTrace();
		}
		
	}	
			
}	
		
	


