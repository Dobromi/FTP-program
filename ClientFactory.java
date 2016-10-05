package project;

public class ClientFactory {

	public ClientInterface getClient(String action){
		if(action.equals("Download")){
			return new ClientDown();
		}
		else if(action.equals("Upload")){
			return new ClientUpload();
		}
		return null;
	}
}
