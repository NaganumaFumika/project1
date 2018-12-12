import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.RenderingHints;

public class DK_syumi extends JPanel{
    ArrayList<Integer> x = new ArrayList<Integer>();
    ArrayList<Integer> y = new ArrayList<Integer>();
    human user = new human();
    DK_syumi (){
	x.add(0);
	x.add(600);
	x.add(200);
	y.add(300);
	y.add(500);
	y.add(800);


	user.x_now = x.get(0);
	user.y_now = y.get(0);

	new javax.swing.Timer(10, new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		   
		    move(x,y,user);//引数大きいほど、人が動くのが早い
		   repaint();
		}
	    }).start(); 

    }

    public void  move(ArrayList<Integer> a,ArrayList<Integer> b, human h){
	int x_p = x.get(0);
	int y_p = y.get(0);
	
	int x_s,y_s ;
	if(x.size()>0){
	    if(h.x_now == x_p && h.y_now == y_p){
		x.remove(0);
		y.remove(0);
		x_p = x.get(0);
		y_p = y.get(0);
		
	    }else{
		x_s = x_p-h.x_now;
		y_s = y_p-h.y_now;
		if(x_s!=0){
		    x_s = x_s/Math.abs(x_s);
		}
		if(y_s != 0){
		    y_s = y_s/Math.abs(y_s);
		    
		}
		
		h.movehuman(h.x_now + x_s,h.y_now + y_s);
	    }
	}
	
	
    }
    
    @Override
	public void paintComponent(Graphics g){
	user.draw(g);
	g.setColor(Color.blue);
	for(int i = 0;i < x.size();i++){
	    
	    g.fillOval(x.get(i),y.get(i),10,10);
	}
	g.drawLine(user.x_now, user.y_now,x.get(0), y.get(0));
	for(int i = 0;i < x.size()-1;i++){
	    g.drawLine(x.get(i), y.get(i),x.get(i+1), y.get(i+1));
	}
	  
    }


    
    public static void main(String[] args) {
	JFrame fr = new JFrame("map");
	fr.setSize(900, 900);
	fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	fr.getContentPane().setBackground(new Color(255, 255, 255));
	DK_syumi pr =new DK_syumi();
	pr.setOpaque(false);
	fr.add(pr);
	fr.setVisible(true);	
    }
}

class human {

	int x_now = 0;
	int y_now = 0;
    int x_end ,y_end ;
	int vx = 0;
	int vy = 0;
	public void movehuman(int x , int y){
		x_now = x; y_now = y;
	}
	
	public void draw(Graphics g){
		g.setColor(Color.red);
		g.fillOval(x_now-5,y_now-5,10,10);
	}
}
