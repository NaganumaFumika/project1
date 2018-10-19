import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;



public class Sample21 extends JPanel {
    human user = new human();

    public Sample21(){
	setOpaque(false);
	addMouseListener(new MouseAdapter() {
	   
	    int i = 0;
	    public void mouseClicked(MouseEvent e) {
		if(i == 0){
		    user.x_now = e.getX();
		    user.y_now = e.getY();
		    i = 1;
		}else{
		    user.x_end = e.getX();
		    user.y_end = e.getY();
		}
	    }
	});
    }


    public void paintComponent(Graphics g){

	g.setColor(Color.gray);
	g.fillOval(100,50,100,100);
		
    }
	
    public static void main(String[] args){
	JFrame app = new JFrame();
	app.add(new Sample21());
	app.setSize(400,300);
	app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	app.setVisible(true);
    }
    
    class human {
	int x_now ,y_now ,x_end ,y_end ;
    }
}

	

	
	
	




