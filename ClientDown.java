package project;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientDown implements ClientInterface{

	public boolean action(File fileName,File directoryToDownload) throws IOException{
		Socket socket=new Socket("localhost",5556);
		DataOutputStream out=new DataOutputStream(socket.getOutputStream());
		File file=new File(directoryToDownload,fileName.getName());
		out.writeUTF("D " + fileName.getName());
		InputStream in = socket.getInputStream();
		OutputStream dout = new FileOutputStream(file);
		byte[] bytes=new byte[16*1024];
		
		int count;
		while((count=in.read(bytes))>0){
			dout.write(bytes, 0, count);
			dout.flush();
		}
		
		dout.close();
		in.close();
		socket.close();
		return true;
	}

	
}
