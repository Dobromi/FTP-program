package project;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

	private ServerSocket server;
	
	private static final String FtpFolder="C:\\Users\\dobromir\\workspace\\sharedFolder\\";
	private static Server serverInstance=null;
	private  Server(){
		
	}
	
	public static ArrayList<File> getAllFiles(){
		final File folder=new File(FtpFolder);
		File[] files=folder.listFiles();
		ArrayList<File> listFiles=new ArrayList<File>();
		for(File f : files){
			listFiles.add(f);
		}
		return listFiles;
	}
	
	public static Server getInstance(){
		if(serverInstance==null){
			serverInstance=new Server();
		}
		return serverInstance;
	}
	
	public static void main(String[] args) throws IOException {
		Server s=Server.getInstance();
		s.listen();
	}
	
	public void listen() throws IOException{
		server=new ServerSocket(5556);
		System.out.println("Listening");
		while(true){
			Socket client=server.accept();
			new Thread(new ClientThread(client)).start();
			System.out.println("here in listen loop");
		}
	}
	
	
	
	class ClientThread implements Runnable{
		
		private Socket clSocket;
		
		public ClientThread(Socket sock){
			this.clSocket=sock;
		}

		@Override
		public void run(){
			
			DataInputStream dis=null;
			try {
				dis=new DataInputStream(clSocket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String line=null;
			try {
				line=dis.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(line!=null){
				String[] arr=line.split(" ");
				String action=arr[0];
				String fileName=arr[1];
				if(action.equals("D")){//D for Download
					File file=new File(FtpFolder + fileName);
					System.out.println("here" + file);
					long length=file.length();
					byte[] bytes = new byte[16 * 1024];
					InputStream in=null;
					try {
						in=new FileInputStream(file);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					OutputStream out=null;
					try {
						out=clSocket.getOutputStream();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					int count;
					try {
						while((count=in.read(bytes))>0){
							out.write(bytes,0,count);
							out.flush();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						out.close();
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				else if(action.equals("U")){
					System.out.println("user uploading file" + fileName);
					DataOutputStream dos=null;
					try {
						dos=new DataOutputStream(clSocket.getOutputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					InputStream in=null;
					OutputStream out=null;
					try {
						in = clSocket.getInputStream();
						out=new FileOutputStream(FtpFolder + fileName);
						byte[] bytes=new byte[16*1024];
						System.out.println("uploading");
						int count;
						while((count=in.read(bytes))>0){
							out.write(bytes, 0, count);
							out.flush();
						}
						System.out.println("file uploaded");
						in.close();
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
		
		
		
	}
}
