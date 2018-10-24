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

public class Project_niimi extends JPanel{
	Map map = new Map();
	human user = new human();
	
	
	
	Project_niimi(){
		setOpaque(false);
	addMouseListener(new MouseAdapter() {
		int i = 0;
		public void mouseClicked(MouseEvent e) {
		    if(map.road(e.getX(),e.getY())){
		    	if(i == 0){
			    
			    user.movehuman(e.getX()/30*30+15,e.getY()/30*30+15);
			    map.p.setxy(e.getX()/30*30+15,e.getY()/30*30+15);
			    repaint();
			    i = 1;
		    	}else{
			    user.x_end = e.getX();
			    user.y_end = e.getY();
			    i = 0;}
		    }else{System.out.println("Plese select road");}
		}
	    });
	
	new javax.swing.Timer(100, new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		    
		    int xm = user.x_now/30;int ym = user.y_now/30;
		    if((user.x_now-15)%30 == 0 && (user.y_now-15)%30 == 0){
			int i = 0;
			map.map[xm][ym] = 1;
			for(int x= -1; x < 2 ; x++ ){
								if (xm + x > -1&& xm + x < 30){
								    for(int y = -1; y < 2; y++){
									if (ym + y > -1 &&  ym + y < 30){
											if(map.map[xm+x][ym+y] == '+' ){
												user.vx = x; user.vy = y; i = 1;
												user.movehuman(user.x_now+user.vx ,user.y_now + user.vy);
												map.p.setxy(user.x_now+user.vx ,user.y_now + user.vy);
												repaint();
												break;
											}
											if(i == 0&&y==1&&x==1){user.vx = 0;user.vy = 0; break;}
										}	
									}
								}
							}
					}else{
						user.movehuman(user.x_now+user.vx ,user.y_now + user.vy);
						map.p.setxy(user.x_now+user.vx ,user.y_now + user.vy);
						repaint();
						
					}
				}
			}).start();
	}
	public void paintComponent(Graphics g){
		map.draw(g);
		user.draw(g);
	}

    public static void main(String[] args) {
	JFrame fr = new JFrame("map");
	fr.setSize(900, 900);
	fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	fr.getContentPane().setBackground(new Color(255, 255, 255));
    Project_niimi pr =new Project_niimi();
    pr.setOpaque(false);
	fr.add(pr);
	fr.setVisible(true);	
    }
}

class Map {
    
    public char map[][];
    private int xx = 30, yy = 30;
    path p = new path();
    
    public  boolean road(int x, int y){
    	int xm = x/30;
    	int ym = y/30;
    	if(map[xm][ym] == ' '){return true;}else{return false;}
    }
    
    public Map(){
	map = new char[xx][yy];
	
	for (int x=0; x<xx; x++) {
	    for (int y=0; y<yy; y++) {
		map[x][y] = 0;
	    }
	}
	
	for(int x=0; x<xx; x++) {
	    for(int y=0; y<yy; y++) { 
		map[3][y] = ' ';
		map[9][y] = ' ';
		map[15][y] = ' ';
		map[x][4] = ' ';
		map[x][10] = ' ';
		map[x][19] = ' ';
		map[x][23] = ' ';
		map[x][25] = ' ';		if(10 <= y) {
		    map[5][y] = ' ';
		}
		if(10 <= y) {
		    map[3][y] = ' ';
		}
		if(9 <= x && x <= 15) {
		    map[x][13] = ' ';
		}
		if(4 <= y) {
		    map[22][y] = ' ';
		}
		if(x <= 22) {
		    map[x][7] = ' ';
		}
		if(19 <= y && y <= 25) {
		    map[18][y] = ' ';
		}
	    }
	}
    }
    
    
    public void draw(Graphics g) {
	for (int x=0; x<xx; x++) {
	    for (int y=0; y<yy; y++) {
		if(map[x][y] == ' ' || map[x][y] == '+') {
		    g.setColor(Color.gray);
		    g.fillRect(x*30, y*30, 30, 30);
		}
	    }
	}
	p.draw(g);
    }
    
    class path {
	int xm,ym,x1,y1;
	
	public void setxy(int x,int y){x1 = x; y1 = y;xm =x/30;ym = y/30;}
	
	public void draw (Graphics g){
	    g.setColor(Color.blue);
	    if(map[xm][ym]=='+'){ g.drawLine(x1,y1,15+30*(xm),15+30*(ym));}
	    int xbf = 2; int ybf = 2;
	    int n = 0;
	    while(true){
		int i = 0;
		
		
		for(int x= -1; x < 2 ; x++ ){
		    if (xm + x > -1&& xm + x < 30){
			for(int y = -1; y < 2; y++){
			    if (ym + y > -1 &&  ym + y < 30){
				if(map[xm+x][ym+y] == '+' &&!(x==0 && y == 0) && !(x==xbf && y==ybf)){
					if(n == 0){
					    if(map[xm][ym]=='+'){ 
						g.drawLine(15+xm*30,15+ym*30,15+30*(xm+x),15+30*(ym+y));
					    }else{
						g.drawLine(x1,y1,15+30*(xm+x),15+30*(ym+y));
					    }
					}else{
					    g.drawLine(15+xm*30,15+ym*30,15+30*(xm+x),15+30*(ym+y));
					}
					xm = xm+x; ym = ym+y; xbf = -x; ybf = -y;
					i = 1;
					n++;
					break;
				    }
				    }	
			    }
			    if(i == 1){break;}
			    if(x == 1){i = 2; break;}
			}
			
		    }
		    if(i==2){break;}
		}
	    }
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





   


   
