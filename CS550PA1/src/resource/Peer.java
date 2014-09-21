package resource;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This interface saves information of one peer,the type is protected
 * eg.peer name, peer location, files in a peer, peer ip address, listening port when being a server.
 * @author shanhuang
 *
 */

@SuppressWarnings("serial")
public class Peer implements Serializable {
    protected String name;
    protected String location;
    protected ArrayList<String> filelist;
    protected String IpAddress;
    protected int ListeningPort  ;
	
    //constructor
    public Peer(String name, String location, ArrayList<String> filelist, String IpAddress, int ListeningPort ){
    this.name=name;
    this.location=location;
    this.filelist=filelist;
    this.IpAddress=IpAddress;
    this.ListeningPort=ListeningPort; 	
    }
	
	public void setFilelist(ArrayList<String> filelist){
		this.filelist=filelist;
	}
	
	public ArrayList<String> getFilelist(){
		return filelist;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	public int getListeningPort(){
		return ListeningPort;
	}
	
	public String getIpAddress(){
		return IpAddress;
	}
	
	public String getLocation(){
		
		return location;
	}

	

	
  	
}
