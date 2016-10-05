package project;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientUpload implements ClientInterface{

	public boolean action(File file,File directory) throws IOException{
		Socket socket=new Socket("localhost",5556);
		DataOutputStream out=new DataOutputStream(socket.getOutputStream());
		out.writeUTF("U " + file.getName());
		out.flush();
		InputStream in=socket.getInputStream();
		
		long length = file.length();
		byte[] bytes = new byte[16 * 1024];
		InputStream fileIn = new FileInputStream(file);
		int count;
		while ((count = fileIn.read(bytes)) > 0) {
			out.write(bytes, 0, count);
			out.flush();
		}
		in.close();
		out.close();	
		socket.close();
		return true;
	}
}
