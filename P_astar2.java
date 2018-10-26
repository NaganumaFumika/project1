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


//xm->何マス目にいるか、x→実際の座標
public class P_astar extends JPanel{
    Map map = new Map();
    human user = new human();
    int s_x,s_y;
    String[][] map2 = new String[30][30];
    
    P_astar(){
	setOpaque(false);
	addMouseListener(new MouseAdapter() {
		int i = 0;
		/*int s_x,s_y; */
		public void mouseClicked(MouseEvent e) {
		    if(map.road(e.getX(),e.getY())){
		    	if(i == 0){			   
 			    user.movehuman(e.getX()/30*30+15,e.getY()/30*30+15); //?
			    map.p.setxy(e.getX()/30*30+15,e.getY()/30*30+15);//?
			    map.map[e.getY()/30][e.getX()/30] = 'S';
			    s_x = e.getX()/30;
			    s_y = e.getY()/30;
			    repaint();
			    i = 1;
		    	}else{
			    user.x_end = e.getX();
			    user.y_end = e.getY();
			    map.map[e.getY()/30][e.getX()/30] = 'G';
			    map.map[s_y][s_x] = 'S';
			    /*
			      System.out.println("経路記入前:");
			      for(int i = 0;i<30;i++){
			      for(int j = 0;j< 30;j++){
			      String s = String.valueOf(map.map[i][j]);
			      System.out.print(s);
				}
				System.out.println();
				}
			    */
			    aStar astar = new aStar(map.map);
			    System.out.println("");
			    for(int i=0; i<30; i++){
				for(int j=0; j<30; j++){
				    System.out.print(map.map[i][j]);
				}
				System.out.println();
			    }
			    i = 0;}
		    }else{System.out.println("Plese select road");}
		}
	    });
	
	new javax.swing.Timer(50, new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		    
		    int xm = user.x_now/30;int ym = user.y_now/30;
		    if((user.x_now-15)%30 == 0 && (user.y_now-15)%30 == 0){
			int i = 0;
			map.map[ym][xm] = ' ';
			for(int y= -1; y < 2 ; y++ ){
			    if (ym + y > -1&& ym + y < 30){
				for(int x = -1; x < 2; x++){
				    if(x == 0|| y == 0 ){
					if (xm + x > -1 &&  xm + x < 30){
					    if(map.map[ym+y][xm+x] == '+'||map.map[ym+y][xm+x] == 'G' ){
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
	P_astar pr =new P_astar();
	pr.setOpaque(false);
	fr.add(pr);
	fr.setVisible(true);	
    }
}

class Map {
    
    public static char map[][];
    private int xx = 30, yy = 30;
    path p = new path();
    
    public  boolean road(int x, int y){
    	int xm = x/30;
    	int ym = y/30;
    	if(map[ym][xm] == ' '){return true;}else{return false;}
    }
    
    public Map(){
	map = new char[yy][xx];
	
	for (int y=0; y<yy; y++) {
	    for (int x=0; x<xx; x++) {
		map[y][x] = '0';
	    }
	}
	for(int y=0; y<yy; y++) {
	    for(int x=0; x<xx; x++) { 
		map[y][3] = ' ';
		map[y][9] = ' ';
		map[y][15] = ' ';
		map[4][x] = ' ';
		map[10][x] = ' ';
		map[19][x] = ' ';
		map[23][x] = ' ';
		map[25][x] = ' ';
		if(10 <= y) {
		    map[y][5] = ' ';
		}
		if(10 <= y) {
		    map[y][3] = ' ';
		}
		if(9 <= x && x <= 15) {
		    map[13][x] = ' ';
		}
		if(4 <= y) {
		    map[y][22] = ' ';
		}
		if(x <= 22) {
		    map[7][x] = ' ';
		}
		if(19 <= y && y <= 25) {
		    map[y][18] = ' ';
		}
		map[0][y] = '0';
		map[29][y] = '0';
		map[x][0] = '0';
		map[x][29] = '0';
	    }
	}
    }
    
    
    public void draw(Graphics g) {
	for (int y=0; y<yy; y++) {
	    for (int x=0; x<xx; x++) {
		if(map[y][x] == 'S' || map[y][x] == 'G' || map[y][x] == ' ' || map[y][x] == '+') {//ここ！
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
	
	public void draw (Graphics g){ //ここ！
	    g.setColor(Color.blue);
	    if(map[ym][xm]=='+'){ g.drawLine(x1,y1,15+30*(xm),15+30*(ym));}
	    int xbf = 2; int ybf = 2;
	    int n = 0;
	    while(true){
		int i = 0;
				
		for(int y= -1; y < 2 ; y++ ){
		    if (ym + y > -1&& ym + y < 30){
			for(int x = -1; x < 2; x++){
			    if (xm + x > -1 &&  xm + x < 30){
				if(x == 0|| y == 0){
				    if(map[ym+y][xm+x] == '+' &&!(x==0 && y == 0) && !(x==xbf && y==ybf)){
					if(n == 0){
					    if(map[ym][xm]=='+'){ 
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
			}
			    if(i == 1){break;}
			    if(y == 1){i = 2; break;}
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


