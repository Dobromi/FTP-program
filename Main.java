package project;

public class Main {

	public static void main(String[] args) {
		Model model=new Model();
		Controller contr=new Controller(model);
		View view=new View(contr,model);
	}
}
