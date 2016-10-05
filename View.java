package project;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;



import java.awt.image.BufferedImage;
import java.io.File;




import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class View{

	private Controller controller;
	private Model model;
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel startingPanel;
	private JPanel secondPanel;
	
	public View(Controller contr,Model model){
		this.controller=contr;
		this.model=model;
		frame=new JFrame();
		mainPanel=new JPanel(new CardLayout());
		packGui();
	}
	
	private void packGui(){
		EventQueue.invokeLater(()->{
			Toolkit kit=Toolkit.getDefaultToolkit();
			Dimension screenSize=kit.getScreenSize();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(screenSize.width/2, screenSize.height/2);
			frame.setLocationByPlatform(true);
			
			startingPanel=new JPanel(new BorderLayout());
			
			JPanel north=new JPanel();
			JLabel label=new JLabel("Log In To FTP Server");
			north.add(label, BorderLayout.NORTH);
			JLabel us=new JLabel("Username");
			JTextField userName=new JTextField(15);
			JLabel pass=new JLabel("Password");
			JTextField password=new JTextField(20);
			JPanel west=new JPanel();
			west.add(us);west.add(userName);
			
			JPanel center=new JPanel();
			center.add(pass);center.add(password);
			
			JPanel east=new JPanel();
			JButton conn=new JButton("Connect");
			east.add(conn);
			
			JPanel image=new JPanel();
			BufferedImage pic=null;
			try {
				 pic=ImageIO.read(new File("C:\\Users\\dobromir\\workspace\\project\\src\\project\\ftp.jpg"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JLabel picLabel=new JLabel(new ImageIcon(pic));
			image.add(picLabel);
			
			startingPanel.add(north,BorderLayout.NORTH);
			startingPanel.add(west,BorderLayout.WEST);
			startingPanel.add(center,BorderLayout.CENTER);
			startingPanel.add(east,BorderLayout.EAST);
			startingPanel.add(image,BorderLayout.SOUTH);
			
			mainPanel.add(startingPanel,"Panel1");
			
			//Second layout
			
			secondPanel=new JPanel();
			
			//
			mainPanel.add(secondPanel,"Panel2");
			
			frame.getContentPane().add(mainPanel);
			
			conn.addActionListener(e->{
				String usstr=userName.getText();
				String uspass=password.getText();
				
				if(model.checkUser(usstr,uspass)){
					CardLayout cardLayout=(CardLayout)mainPanel.getLayout();
					cardLayout.next(mainPanel);
					try {
						secondGui();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					model.setState();
				}
				else{
					label.setForeground(Color.RED);
					label.setText("Error username or pass,try Again");
				}
				
			});
			
			frame.setVisible(true);
			
		});
	}
	
	
	private void secondGui() throws UnknownHostException, ClassNotFoundException, IOException{
		JPanel guiPanel=new JPanel(new BorderLayout());
		ArrayList<File> files=Server.getAllFiles();
		Vector itemsVector=new Vector(files);
		JList fileList=new JList(itemsVector);
		
		fileList.setCellRenderer(new FileRenderer(true));
		fileList.setVisibleRowCount(9);
		JScrollPane scrollPane=new JScrollPane(fileList);
		
		guiPanel.add(scrollPane,BorderLayout.CENTER);
		
		//Buttons
		
		JButton download=new JButton("Download");
		JButton upload=new JButton("Upload");
		JPanel buttPanel=new JPanel();
		buttPanel.add(download);buttPanel.add(upload);
		JLabel sign=new JLabel();
		sign.setForeground(Color.RED);
		JPanel north=new JPanel();
		north.add(sign);
		
		
		
		download.addActionListener(e->{
			File selectedFile=(File)fileList.getSelectedValue();
			JFileChooser chooser=new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("C:\\Users\\dobromir\\workspace"));
			chooser.setDialogTitle("Select folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			File directoryToDownload = null;
			boolean res = false;

			EventQueue.invokeLater(() -> {
				sign.setText("");
			});
			if(chooser.showOpenDialog(guiPanel)==JFileChooser.APPROVE_OPTION){
				directoryToDownload=chooser.getSelectedFile();
				res=controller.handleAction(download.getActionCommand(), selectedFile,directoryToDownload);
				
			}
			if(res){
				EventQueue.invokeLater(()->{
					sign.setText("Downloading successful");
					
				});
			}
			
		});
		
		upload.addActionListener(e->{
			JFileChooser chooser=new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("C:\\Users\\dobromir\\workspace"));
			chooser.setDialogTitle("Select file");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			boolean res=false;
			EventQueue.invokeLater(()->{
				sign.setText("");
			});
			
			if(chooser.showOpenDialog(guiPanel)==JFileChooser.APPROVE_OPTION){
				File selectedFile=chooser.getSelectedFile();
				res=controller.handleAction(upload.getActionCommand(), selectedFile);
				if(res){
					EventQueue.invokeLater(()->{
						sign.setText("Uploading successful");
						itemsVector.add(selectedFile);
						fileList.setListData(itemsVector);
					});
				}
			}
			
		});
		
		guiPanel.add(buttPanel,BorderLayout.SOUTH);
		guiPanel.add(north,BorderLayout.NORTH);
		secondPanel.add(guiPanel);
	}
	
}
