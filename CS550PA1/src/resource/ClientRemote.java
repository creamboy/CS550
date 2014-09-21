package resource;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * This interface is used for peer works as client.The type is protected
 * client can add file,download file,search file, delete file,search peer
 * @author 
 *
 */

@SuppressWarnings("serial")
public class ClientRemote implements Serializable{
	
	protected ObjectInputStream SocketReader;
	protected ObjectOutputStream SocketWriter;
	protected String location;
	//protected boolean peerexist;
	protected String IpAddress;
	protected int PortNumber;
	protected boolean peerexist;
	
	//constructor
	
	public ClientRemote (String IpAddress, int portnum, String location){
		this.location=location;
		this.IpAddress=IpAddress;
		this.PortNumber=portnum;		
	}
	
	public boolean setpeerexist(boolean peerexist){
		this.peerexist=peerexist;
		return peerexist;
	}
	
	public boolean ifhaspeer(){
		return peerexist;
	}
	
	
	
	
	public void ConnectionSetUp(){
		try{
			Socket client= new Socket(this.IpAddress,this.PortNumber);
			SocketWriter = new ObjectOutputStream(client.getOutputStream());
			SocketReader= new ObjectInputStream(client.getInputStream());
			System.out.println("Connection accepted from remote server!");
			Thread.sleep (1000);
		}catch (Exception e)
		{
			System.out.println("Connection failed !" + e);
		}
	}	
		
	public void DestroyConncetion( ) {
		try{
			SocketWriter.close();
			SocketReader.close();
		}catch(Exception e)
		{
			System.out.println("Error destroying socket connection !" + e);
		}
		
	}	

	
	

	
	
	
/**
 * register a peer
 */
  public void Register(Message msg) throws Exception{
	  Message messageOut=msg;
	 
	 this.ConnectionSetUp();
	 try{
		 SocketWriter.writeObject(messageOut);
		 SocketWriter.flush();
	 }catch(Exception e)
	 {
		 e.printStackTrace();
	 }
	 Message messageIn=(Message)SocketReader.readObject();
	 System.out.println(messageIn.RequestFunction);
	  Thread.sleep (1000);
	 Thread.sleep (1000);
	 System.out.println("Registering Peer Successfully");
	 Thread.sleep (1000);
	 
	 this.DestroyConncetion();
  }
	
  
  
  
  
  
  
  
  /**
   * search file on server, return peer lists that contains the file
   */

  public ArrayList<Peer> searchFile(Message msg){
	  Message messageOut=msg;
	  Message messageIn=null;
	  ArrayList<Peer> peerlist = null;
	  PrintWriter out = null;
	  
	  this.ConnectionSetUp();
	  try{
		  SocketWriter.writeObject(messageOut);
		  SocketWriter.flush();
	  }catch (Exception e){
		  e.printStackTrace();
	  }
	  try{
		  messageIn=(Message)SocketReader.readObject();
	  }catch(Exception e)
	  {
		  e.printStackTrace();
	  }
	  if(messageIn.message.equals("file is found")){
		  System.out.println("File Found, Peer Found:");
		  peerlist=messageIn.peerlist;
		  File Foo = new File( System.getProperty("user.dir")+ "/src/P2Pfile/Display file foo");
		  if(Foo.exists()==false){
			  Foo.canExecute();
		  }
		//append to file
		  try {
			out = new PrintWriter(new FileWriter(Foo, true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	  
	  for (int i =0; i<peerlist.size();i++){
		  System.out.println("Peer name:  " + peerlist.get(i).name);
		  System.out.println("Location:  " + peerlist.get(i).location);
		  System.out.println("IP:  " + peerlist.get(i).IpAddress);
		  System.out.println("Listening port  " + peerlist.get(i).name);
		  System.out.println(peerlist.get(i).name + " contains:");
		  
		 out.println("Peer name:  " + peerlist.get(i).name);
		  out.println("Location:  " + peerlist.get(i).location);
		 out.println("IP:  " + peerlist.get(i).IpAddress);
		  out.println("Listening port  " + peerlist.get(i).name);
		  out.println(peerlist.get(i).name + " contains:");
		  
		  for (int m=0; m <peerlist.get(i).filelist.size(); m++){
			  System.out.println(peerlist.get(i).filelist.get(i));
			 out.println(peerlist.get(i).filelist.get(i));
		  }		  
		  
	  }
	
	  out.close();
	  
  }
	  else if (messageIn.message.equals("file is not found")){
		  System.out.println("No peers contain this file");
	  }
	  this.DestroyConncetion();
      return peerlist;
				
  }		
	

  
  
  
  
  
  
/**
 * download a file
 */

public void downloadFile(Message msg){
	Message messageOut=msg;
	Message messageIn=null;
	PrintWriter out = null;
	
	this.ConnectionSetUp();
	
	try{
		SocketWriter.writeObject(messageOut);
		SocketWriter.flush();
	}catch(Exception e){
		e.printStackTrace();
	}
	try {
		messageIn=(Message)SocketReader.readObject();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	File file = new File(System.getProperty("user.dir") + location + "/" + messageIn.message);
	System.out.println("Save to " + file.getAbsolutePath());
	
	try{
		if(file.createNewFile()){
			byte[][] temp=messageIn.bytetemp;
			int[] readbyte=messageIn.bytenumber;
			
			FileOutputStream Out= new FileOutputStream(file);
			
			int N = 0;
			for(int i = 0; i<readbyte.length; i++){
				if(readbyte[i] == -1){
					N = i;
				}
			}
			for(int i = 0;i<N;i++){
				Out.write(temp[i], 0, readbyte[i]);
			}
			   
			Out.close();
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	File Foo = new File( System.getProperty("user.dir")+ "/src/P2Pfile/Display file foo");
	  if(Foo.exists()==false){
		  Foo.canExecute();
	  }
	  
	  try {
		out=new PrintWriter(new FileWriter(Foo,true));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  out.println(messageOut.peer.name + "save to " + file.getAbsolutePath());
	  out.close();
	
			this.DestroyConncetion();
}








/**
 * search peer, if exists, return true.
 * 
 */
public boolean PeerSearch(Message msg){
	Message messageOut=msg;
	Message messageIn=null;
	this.ConnectionSetUp();
	try{
		SocketWriter.writeObject(messageOut);
		SocketWriter.flush();
		messageIn=(Message)SocketReader.readObject();
	
		if(messageIn.RequestFunction.equals("PeerSearch")){
			if(messageIn.PeerExist){
				return true;
					
			}
				
		}
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	this.DestroyConncetion();
	return false;
}	







/**
 * get file list
 * 
 */
public ArrayList<String> filelist (String location){
	
	ArrayList<String> filelist = new ArrayList<String>();
	
	try{
		File file=new File(System.getProperty("user.dir") + location);
		File list[]=file.listFiles();
		for(int i=0; i<list.length; i++)
		{
			filelist.add(list[i].getName());
		}
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	return filelist;	
}








/**
 * Adding Existing file, synchronizing the index server.(not adding the new file)
 */
public void AddExistingFile(Message msg) {
	Message messageOut=msg;
	Message messageIn=null;
	this.ConnectionSetUp();
	
	//add file name to one peer
    messageOut.peer.setFilelist(this.filelist(location));
    System.out.println("File Adding to Peer");
    messageOut.message="File Created";
	
	try{
		SocketWriter.writeObject(messageOut);
		SocketWriter.flush();
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	try {
		messageIn=(Message)SocketReader.readObject();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	if(messageIn.message.equals("Adding Successfully")){
	System.out.println("File Created on the server successfully");}
	else {//if(messageIn.message.equals("Adding Unsuccessfully")) {
		System.out.println("File Created unsuccessfully on the server");
	}
	
	this.DestroyConncetion();
}







/**
 * delete a file from peer and synchronize he index server
 * 
 */

public void DeleteFile(Message msg){
	Message messageOut=msg;
	Message messageIn=null;
	BufferedReader BR=new BufferedReader(new InputStreamReader(System.in));
	String filename = null ;
	boolean find=false;
	this.ConnectionSetUp();
	
	this.ConnectionSetUp();
	System.out.println("Please input the file name you want to delete");
	try {
		filename= BR.readLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//delete from a peer
	for(int i=0; i<messageOut.peer.filelist.size();i++ ){
		if(messageOut.peer.filelist.get(i).equals(filename)){
			//remove from node
			messageOut.peer.filelist.remove(i);
			find=true;
			break;
		}
		
		
	}
	
	if(find){
		messageOut.message=filename;
		File Dfile= new File (System.getProperty("user.dir")+"/"+location+"/"+filename);
		Dfile.delete();
		if(Dfile.exists()){
			messageOut.message="Can not delete the file!";
		    System.out.println("Can not delete the file!");
		}
		else{
			System.out.println("File Delete Successfully!");

		}
	}
	else{
		messageOut.message="Can not find the File";
		System.out.println("Can not find the File!");
			
	}
	
  try{
	  //synchronizing index-server
	  SocketWriter.writeObject(messageOut);
	  SocketWriter.flush();
  }catch(Exception e){
	  e.printStackTrace();
  }
try {
	messageIn=(Message)SocketReader.readObject();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (ClassNotFoundException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	
try {
	messageIn=(Message)SocketReader.readObject();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (ClassNotFoundException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

 if(messageIn.message.equals("Delete Complete from Server")){
	 
	 System.out.println("Delete Compelete!");
	 
 }else if(messageIn.message.equals("No Change")){
	 System.out.println("Dose no change anything !");
 }


	this.DestroyConncetion();	
}


}

		
		
	


	
	
	
	
	

	
	
	
	
	
	
	
	


