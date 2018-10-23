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
		    map.p.setxy(e.getX()/30*30+15,e.getY()/30*30+15);//setxyで始点を入力し、repaintで始点から+が続く限り線を結ぶ(経路)
		    		repaint();
			i = 1;
		    	}else{
			user.x_end = e.getX();
			user.y_end = e.getY();
		    //ここでmap.mapを変える
		    i = 0;}
		    }else{System.out.println("道を選択してください");}
		}
	    });
		
			new javax.swing.Timer(100, new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					
					int xm = user.x_now/30;int ym = user.y_now/30;
					if((user.x_now-15)%30 == 0 && (user.y_now-15)%30 == 0){//タイルの中心に到着した瞬間
						int i = 0;
						map.map[xm][ym] = 1;
							for(int x= -1; x < 2 ; x++ ){//周りの+を探して次の速度ベクトルを決める
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
    
	//道判定
	public  boolean road(int x, int y){
    	int xm = x/30;
    	int ym = y/30;
    	if(map[xm][ym] == 1){return true;}else{return false;}
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
		map[3][y] = 1;
		map[9][y] = 1;
		map[15][y] = 1;
		map[x][4] = 1;
		map[x][10] = 1;
		map[x][19] = 1;
		map[x][23] = 1;
		map[x][25] = 1;
		//if(x == y) {map[x][y] = 1;}
		if(10 <= y) {
		    map[5][y] = 1;
		}
		if(10 <= y) {
		map[3][y] = 1;
		}
		if(9 <= x && x <= 15) {
		    map[x][13] = 1;
		}
		if(4 <= y) {
		    map[22][y] = 1;
		}
		if(x <= 22) {
		    map[x][7] = 1;
		}
		if(19 <= y && y <= 25) {
		    map[18][y] = 1;
		}
	    }
	}
   //map[3][0]='+';map[3][1]='+';map[3][2]='+';map[3][3]='+';map[3][4]='+';map[3][5]='+';//test用に+
	}
	
    
	public void draw(Graphics g) {
	for (int x=0; x<xx; x++) {
	    for (int y=0; y<yy; y++) {
		if(map[x][y] == 1 ||map[x][y] == '+') {
		    g.setColor(Color.gray);
		    g.fillRect(x*30, y*30, 30, 30);
		}
	    }
	}
	p.draw(g);
	}
	
	class path {
		int xm,ym,x1,y1;
		
		public void setxy(int x,int y){x1 = x; y1 = y;xm =x/30;ym = y/30;}//初期座標の設定
		
		public void draw (Graphics g){
		g.setColor(Color.blue);
		if(map[xm][ym]=='+'){ g.drawLine(x1,y1,15+30*(xm),15+30*(ym));}//自分の座標からその座標上のタイルの中心
		int xbf = 2; int ybf = 2;//一個前の移動を記憶
		//経路を書く,到着したら変わる
		int n = 0;//drawlineの一回目だけ違う
		while(true){
			int i = 0;//brekeのため,0方向探索中,1次の方向を見つけた,2もう道はない
			
			
				for(int x= -1; x < 2 ; x++ ){
					if (xm + x > -1&& xm + x < 30){
						for(int y = -1; y < 2; y++){
							if (ym + y > -1 &&  ym + y < 30){
								if(map[xm+x][ym+y] == '+' &&!(x==0 && y == 0) && !(x==xbf && y==ybf)){//+かつ自分じゃないかつ前のじゃない
									if(n == 0){
										if(map[xm][ym]=='+'){ 
										g.drawLine(15+xm*30,15+ym*30,15+30*(xm+x),15+30*(ym+y));
										}else{
											g.drawLine(x1,y1,15+30*(xm+x),15+30*(ym+y));}
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





   


   