package resource;

import java.io.*;
import java.util.ArrayList;

/**
 * This interface is used for self-defined message.The type is protected. 
 * it deals with the message passing between client and server.
 * @author shanhuang
 *
 */

@SuppressWarnings("serial")
public class Message implements Serializable{
	protected String RequestFunction;
	protected Peer peer;
	protected String message;
	protected boolean PeerExist;
	protected String filename ;	
	protected ArrayList<Peer> peerlist = new ArrayList<Peer>(); 
	protected int[] bytenumber; // byte number of file
	protected byte[][] bytetemp; //used for transferring files
	
	
	//constructor
	
	//used for searching a peer
	public Message (String RequestFunction ,Peer peer){
		this.RequestFunction=RequestFunction;
		this.peer=peer;
	}
	
	public Message (String RequestFunction, boolean PeerExist){
		this.RequestFunction=RequestFunction;
		this.PeerExist=PeerExist;
		
	}
	
	public Message(String RequestFunction,String filename, int[] bytenumber, byte[][] bytetemp){
		this.RequestFunction=RequestFunction;
		this.bytenumber=bytenumber;
		this.filename=filename;
		this.bytetemp=bytetemp;
	}
	
	public Message(String RequestFunction, Peer peer, String filename, int[] bytenumber, byte[][] bytetemp){
		this.RequestFunction=RequestFunction;
		this.peer=peer;
		this.bytenumber=bytenumber;
		this.filename=filename;
		this.bytetemp=bytetemp;
	}

	////used for registering a peer
	public Message (String RequestFunction, String message){
		this.RequestFunction = RequestFunction;
		this.message = message;
	}
	
	//used for downloading a file
	public Message (String RequestFunction, Peer peer, String filename ){
		this.RequestFunction=RequestFunction;
		this.peer=peer;
		this.filename=filename;}
		
    public Message (String RequestFunction, ArrayList<Peer> pl, String mes ){
			this.RequestFunction=RequestFunction;
		    this.peerlist=pl;
		    this.message=mes;
				
	}


	/**
	 * function used for ClientRemote message packaging
	 */
	//public Message MessagePackaging(String FunctionName, String file ){
		//searching a file
		//Message message= new Message(FunctionName, file);
		//return message;
	//}
	
    public Message MessagePackaging(String FunctionName, Peer p){
    	//searching a peer
    	Message message =new Message(FunctionName, p);
    	return message;
    }
    
    public Message MessagePackaging(String FunctionName, Peer p, String file){
    	//require downloading a file
    	Message message = new Message(FunctionName,p, file );
    	return message;
    }
    
    
    /**
     * 
     * Function for HandleConnection message packaging
     */
    public Message MessagePackaging(String request, boolean PeerExist){
    	Message msg= new Message(request,PeerExist);
    	return msg;
    }
    
    public Message MessagePackaging(String request, String mm){
    	Message msg = new Message (request, mm);
    	return msg;
    }
    
    //for downloading
    public Message MessagePackaging(String RequestFunction,String filename, int[] bytenumber, byte[][] bytetemp){
    	Message msg=new Message(RequestFunction, filename,bytenumber,bytetemp);
    	return msg;
    }
    
    public Message MessagePackaging(String RequestFunction, ArrayList<Peer> pl, String message){
    	Message msg=new Message(RequestFunction,pl,message);
    	return msg;
    	
    	
    }
    
    
}
