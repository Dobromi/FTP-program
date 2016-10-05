package project;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;



public class Model {
	
	
	private static final String const1="startingPanel";
	private static final String const2="secondPanel";
	private static final String arr[]={"Dobri","123456","Angelo","333"};
	private ClientFactory clFac;
	
 	private String currPanel=const1;
	
 	public Model(){
 		clFac=new ClientFactory();
 	}
 	
 	
	public String getCurrPanel(){
		return currPanel;
	}
	
	public boolean checkUser(String us,String pass){
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals(us)){
				if(arr[i+1].equals(pass))return true;
			}
		}
		return false;
	}
	
	
	public boolean downloadFile(File file,File directory){
		ClientInterface client=clFac.getClient("Download");
		try {
			if(client.action(file, directory)){
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean uploadFile(File file){
		ClientInterface client=clFac.getClient("Upload");
		try {
			if(client.action(file, new File("C:\\Users\\dobromir\\workspace\\sharedFolder"))){
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void setState(){
		if(currPanel.equals(const1)){
			currPanel=const2;
		}
	}
}
