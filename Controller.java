package project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Controller implements ActionListener{

	private Model model;
	
	public Controller(Model model){
		this.model=model;
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Download")){
			
		}
	}
	
	public boolean handleAction(String action,File file,File directory){
		
		if(model.downloadFile(file,directory)){
			return true;
		}
		return false;
	}
	
	public boolean handleAction(String action,File file){
		if(model.uploadFile(file))return true;
		return false;
	}
	
}
