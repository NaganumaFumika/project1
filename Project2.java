// アルゴリズム班とシミュ班合体済み

/*
！お知らせ！
二次元配列を取り扱う際は、
map[y][x]
というように、左をy座標、右をx座標として扱って下さい。
このように扱わないとシミュレーションが上手く動かないバグが発生してしまうようです。
お手数ですがよろしくお願いします。
 */
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
import java.util.Random;

public class Project2 extends JPanel{
    Map map = new Map();
    Map.human user = map.new human();
    //Map.barrier obs = map.new barrier();
    Map.goal goal = map.new goal();
    Map.button fire = map.new button();
    Map.button tree = map.new button();
    int xl = map.lx;//maisuu  panel
    int yl = map.ly;//maisuu  panel
    int xn = 900/xl;//x no panel no nagasa
    int yn = 900/yl;//y no panel no nagasa
    int n = 1;//user komi
    public static char[][] map_result = new char[30][30];
    ArrayList<Map.human> human = new ArrayList <Map.human>();
    ArrayList<Map> m = new ArrayList<Map>();
    ArrayList<aStar> as = new ArrayList <aStar>();
    ArrayList<Map.barrier> obs = new ArrayList <Map.barrier>();

    Project2 (){
	human.add(user);
	m.add(map);
	setOpaque(false);

	fire.x = 920;
	fire.y = 30;
	fire.name = "火災";

	tree.x = 990;
	tree.y = 30;
	tree.name = "倒木";


	addMouseListener(new MouseAdapter() {
		int i = 0;
		int s_x, s_y, e_x, e_y;
		boolean click_map = true;
		char button_flag;

		public void mouseClicked(MouseEvent e) {

		    if(click_map) {
			if(0<=e.getX() && e.getX()<=900 && 0<=e.getY() && e.getY()<=900) {
			    System.out.println("click");
			    if(map.road(e.getX(),e.getY())){
				if(i == 0){
				    System.out.println("click i=0");    
				    user.movehuman(e.getX()/xn*xn+xn/2,e.getY()/yn*yn+yn/2);
				    map.p.setxy(e.getX()/xn*xn+xn/2,e.getY()/yn*yn+yn/2);
				    map.map[e.getY()/yn][e.getX()/xn] = 'S';
				    s_x = e.getX()/xn;
				    s_y = e.getY()/yn;
				    repaint();
				    i = 1;
				    
				}else if( i == 1 ){
				    //System.out.println("click i=1");	
				    user.x_end = e.getX();
				    user.y_end = e.getY();
				    goal.x = e.getX();
				    goal.y = e.getY();
				    m.get(0).map[e.getY()/yn][e.getX()/xn] = 'G';
				    map.map[s_y][s_x] = 'S';
				    e_x = e.getX()/xn;
				    e_y = e.getY()/yn;
				    
				    //make human & Map
				    Random rnd = new Random();
				    int x_rnd = 0;int y_rnd = 0;;
				    
				    for(int i = 1;i < n ;i++){
					m.add(new Map());
					m.get(i).map[e.getY()/yn][e.getX()/xn] = 'G';
					while(true){
					    x_rnd = rnd.nextInt(xl);
					    y_rnd = rnd.nextInt(yl);
					    if(m.get(i).road(x_rnd*xn , y_rnd*yn) == true ){
						m.get(i).map[y_rnd][x_rnd] ='S';
						break;
					    }
					}
					human.add(m.get(i).new human(x_rnd*xn+xn/2 , y_rnd*xn+xn/2));
				    }

				    aStar as ;
				    for (int i = 0;i<n ;i++){
					as = new aStar(m.get(i).map); 
					for(int a = 0;a < yl;a++){
					    for(int b = 0;b < xl;b++){
						m.get(i).map[a][b] = map_result[a][b];
					    }
					}
					/*
					//new!
					for(int q=0;q<900;q++){
					    String s = String.valueOf(m.get(0).map[q]);
					    System.out.println(s);
					    }*/

					as = null;
				    }
				    
				    i = 2;//changeF
				    click_map = false;
				    System.out.println(click_map);
				 
				}else if(i == 2){
				    //System.out.println("click i=2");
				    obs.add(m.get(0).new barrier());
				    obs.get(obs.size()-1).x = e.getX();
				    obs.get(obs.size()-1).y = e.getY();
				    
				    //obs.x = e.getX();
				    
				    //obs.y = e.getY();
				    /*changeF*/
				    for(int b=0; b<900; b++) {
					for(int a=0; a<900; a++) {
					    if(a >= e.getX()-(obs.get(0).bl_x/2) && a <= e.getX()+(obs.get(0).bl_x/2) && b >= e.getY()-(obs.get(0).bl_y/2) && b <= e.getY()+(obs.get(0).bl_y/2)) {
						// m.get(0).map[a/xn][b/yn] = 'X';
						for(int k = 0;k<n;k++){
						    m.get(k).map[b/yn][a/xn] = 'X';
						    m.get(k).map[e_y][e_x] = 'G';
						    //System.out.println("obs");
						}
					    }
					}
					//m.get(k).map[e.getX()/xn][e.getY()/yn] = 'X';
					
					//m.get(k).map[e_x][e_y] = 'G';
					
				    }
				    /*changeF*/
				    
				    repaint();
				    for(int k = 0; k<n;k++){
					for(int i=0; i<xl; i++) {
					    for(int j=0; j<yl; j++) {
						if(m.get(k).map[j][i] == '+' || m.get(k).map[j][i] == 'S') {
						    m.get(k).map[j][i] = ' ';
						}
					    }
					}
				    }
				    
				    aStar as;
				    for (int k = 0;k<n;k++){
					m.get(k).map[human.get(k).y_now/yn][human.get(k).x_now/xn] = 'S';
					as =  new aStar(m.get(k).map) ;
					for(int a = 0;a < yl;a++){
					    for(int b = 0;b < xl;b++){
						m.get(k).map[a][b] = map_result[a][b];
					    }
					}
				    }
				    i = 2; //changeF
				
				    /*シミュレーション停止もう一度mapの送受信必要*/
				    
				    //
				    click_map = false;
				    /*}else if(i == 3){
				    //System.out.println("click i=3");
				    for (int k = 0; k < human.size();k++){
				    System.out.println(human.get(k).x_now + " ,"+ human.get(k).y_now);
				    }*/
				}else if(i == 3){
				    System.out.println("Please select building");
				}			        
			    }else{
				//System.out.println("Please select road");
			    
			    
				//change niimi
				if(i==3) {
				    for(int k = 0;k<n;k++){
					if(m.get(k).build(e.getX(),e.getY())){
					    m.get(k).map[e.getY()/yn][e.getX()/xn] = 'F';
					}
				    }
				    click_map = false;
				} else {
				    System.out.println("Please select road");
				}
			    }
			    
			} else {
			    System.out.println("Please click map");
			}
			
		    } else {
			if(0<=e.getX() && e.getX()<=900 && 0<=e.getY() && e.getY()<=900) {
			    System.out.println("Please click button");
			} else {
			    if(920<=e.getX() && e.getX()<=980 && 30<=e.getY() && e.getY()<=60) {
				button_flag = 'f';
				click_map = true;
				i = 3;
				System.out.println("火災 is selected");
			    }
			    else if (990<=e.getX() && e.getX()<=1050 && 30<=e.getY() && e.getY()<=60) {
				button_flag = 't';
				i = 2;
				click_map = true;
				System.out.println("倒木 is selected");
			    } else {
				System.out.println("Please click button");
			    }
			}
		    }
		    
		}
		
	    });

	new javax.swing.Timer(1, new ActionListener(){
		public void actionPerformed(ActionEvent evt){

		    //change niimi
		    for(int k = 0; k < m.size(); k++ ){
			m.get(k).fire(5);
		    }
		    for(int t = 0 ; t < human.size(); t++){
			int xm = human.get(t).x_now/xn;
			int ym = human.get(t).y_now/yn;
			if((human.get(t).x_now-xn/2)%xn == 0 && (human.get(t).y_now-yn/2)%yn == 0){
			    int i = 0;
			    m.get(t).map[ym][xm] = ' ';
			    for(int x= -1; x < 2 ; x++ ){
				if (xm + x > -1&& xm + x < xl){				
				    for(int y = -1; y < 2; y++){
					if (ym + y > -1 &&  ym + y < yl){
					    if(x == 0 || y == 0){ 					    						if(m.get(t).map[ym+y][xm+x] == '+'||m.get(t).map[ym+y][xm+x] == 'G' ){
						    //System.out.println(t);
						    human.get(t).vx = x; human.get(t).vy = y; i = 1;
						    human.get(t).movehuman(human.get(t).x_now+human.get(t).vx ,human.get(t).y_now + human.get(t).vy);
						    if(t ==  0){
							m.get(t).p.setxy(human.get(t).x_now+human.get(t).vx ,human.get(t).y_now + human.get(t).vy);
						    }
						    repaint();
						    break;
						}
						if(i == 0&&y==1&&x==1){
						    human.get(t).vx = 0;human.get(t).vy = 0; break;
						}
					    }  
					}
				    }
				}
			    }
			}else{
			    human.get(t).movehuman(human.get(t).x_now + human.get(t).vx ,human.get(t).y_now + human.get(t).vy);
			    if(t == 0){
				m.get(t).p.setxy(human.get(t).x_now+human.get(t).vx ,human.get(t).y_now + human.get(t).vy);
			    }
			    //repaint();
			}
		    }
		}
	    }).start(); 
    }

    @Override
	public void paintComponent(Graphics g){

	map.draw(g);
	fire.draw(g);
	tree.draw(g);

	if((user.x_now!=0) &&(user.y_now!=0)) {
	    user.draw(g);
	    for(int i = 1 ;i<human.size();i++){
		human.get(i).drawhuman(g);
	    }
	}

	if((user.x_end!=0) &&(user.y_end!=0)) {
	    goal.draw(g);
	}
	/*changeF*/
	for(int i=0; i<obs.size(); i++) {
	    if((obs.get(i).x != 0) && (obs.get(i).y != 0)) {
		obs.get(i).draw(g);
	    }
	}
	/*changeF*/
    }

    

    public static void main(String[] args) {

	JFrame fr = new JFrame("map");
	fr.setSize(1100, 900);
	fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	fr.getContentPane().setBackground(new Color(255, 255, 255));
        Project2 pr =new Project2();
        pr.setOpaque(false);
	fr.add(pr);
	fr.setVisible(true);
    }

}




class Map extends JPanel{
    public static int lx = 30, ly = 30;
    public char map[][];
    path p = new path();

    //追加部分 
    public static int pix_x = 900/lx;
    public static int pix_y = 900/ly;

    int x_clixked,y_clicked;

    /*changeF*/
    private BufferedImage barrierImg;


    public int bl_x = 5;
    public int bl_y = 5;
    /*changeF*/

    public boolean road(int x, int y){
        int xm = x/pix_x;
        int ym = y/pix_y;
        if(map[ym][xm] == ' '|| map[ym][xm] == '+'){return true;}else{return false;}
    }
    
    //change niimi
    public boolean build(int x, int y){
        int xm = x/pix_x;
        int ym = y/pix_y;
        if(map[ym][xm] == '0' ){return true;}else{return false;}
    }
    
    //change niimi
    int fire_sc = 0;
    public void fire(int f){
	if(fire_sc%f == 0){
	    ArrayList<Integer> f_x = new ArrayList <Integer>();
	    ArrayList<Integer> f_y = new ArrayList<Integer>();
	    int yabakyori = 50;
	       
	       
	    for (int y=0; y<ly; y++) {
		for (int x=0; x<lx; x++) {
		    if(map[y][x] == 'F'){ f_x.add(x);f_y.add(y);}
		}
	    }
	       
	    for(int i =0;i < f_x.size();i++){
		for(int x = -1*yabakyori;x < yabakyori; x++){
		    for(int y = -1*yabakyori; y < yabakyori; y++){
			if (f_x.get(i) + x > -1&& f_x.get(i) + x < lx){
			    if (f_y.get(i) + y > -1 &&  f_y.get(i) + y < ly){
				if(map[f_y.get(i)+y][f_x.get(i)+x]==' '){
				    map[f_y.get(i)+y][f_x.get(i)+x]='P';
				}
			    }
			}
		    }
		}
	    }
	      
	       
	    for(int i =0;i < f_x.size();i++){
		for(int x = -1;x < 2; x++){
		    for(int y = -1; y < 2; y++){
			if (f_x.get(i) + x > -1&& f_x.get(i) + x < lx){
			    if (f_y.get(i) + y > -1 &&  f_y.get(i) + y < ly){
				if(map[f_y.get(i)+y][f_x.get(i)+x]=='0'){
				    map[f_y.get(i)+y][f_x.get(i)+x]='F';
				}
			    }
			}
		    }
		}
	    }
	       
	    fire_sc ++;
	}else{fire_sc++;}
    }

    public Map(){
	this.map = new char[ly][lx];
	
	for (int y=0; y<ly; y++) {
	    for (int x=0; x<lx; x++) {
		map[y][x] = '0';
	    }
	}
	
	
	/*for(int x=0; x<lx; x++) {
	    for(int y=0; y<ly; y++) {
		for(int i=110; i<130; i++) {
		    map[y][i] = ' ';
		}
		for(int i=270; i<285; i++) {
		    map[y][i] = ' ';
		}
		for(int i=450; i<460; i++) {
		    map[y][i] = ' ';
		}
		for(int i=100; i<120; i++) {
		    map[i][x] = ' ';
		}
		for(int i=300; i<350; i++) {
		    map[i][x] = ' ';
		}
		for(int i=370; i<400; i++) {
		    map[i][x] = ' ';
		}
		for(int i=650; i<670; i++) {
		    map[i][x] = ' ';
		}
		for(int i=770; i<780; i++) {
		    map[i][x] = ' ';
		}
		if(110<=y) {
		    for(int i=90; i<120; i++) {
			map[y][i] = ' ';
		    }
		}
		if(100<=y) {
		    for(int i=150; i<165; i++) {
			map[y][i] = ' ';
		    }
		}
		if(x<=450){
		    for(int i=390; i<420; i++){
			map[i][x] = ' ';
		    }
		}
		if(120 <= y){
		    for(int i=660; i<750; i++){
			map[y][i] = ' ';
		    }
		}
		if(x<=660) {
		    for(int i=200; i<225; i++){
			map[i][x] = ' ';
		    }
		}
		if(370<=y && y<=780){
		    for(int i=540; i<555; i++){
			map[y][i] = ' ';
		    }
		}
		}*/
		
		for(int x=0; x<30; x++) {
			for(int y=0; y<30; y++) { 
			map[3][y] = ' ';
			map[4][y] = ' ';
			map[9][y] = ' ';
			map[15][y] = ' ';
			map[x][4] = ' ';
			map[x][10] = ' ';
			map[x][19] = ' ';
			map[x][20] = ' ';
			map[x][23] = ' ';
			map[x][25] = ' ';
			if(10 <= y) {
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
				map[23][y] = ' ';
			}
					if(x <= 22) {
				map[x][7] = ' ';
			}
			if(19 <= y && y <= 25) {
				map[18][y] = ' ';
			}
			}   
	}

	try{
	    File barrierFile = new File("./barrier.png");
	    barrierImg = ImageIO.read(barrierFile);
	} catch (IOException e) {
	    System.out.println("Failed to read file");
	}
    }
    public void draw(Graphics g) {

	
        for (int x=0; x<lx; x++) {
            for (int y=0; y<ly; y++) {
                if(map[y][x] == 'S' || map[y][x] == 'G' || map[y][x] == ' ' || map[y][x] == '+' ||map[y][x] == 'X' ) {
                    g.setColor(Color.white);
                    g.fillRect(x*pix_x, y*pix_y, pix_x, pix_y);
		    /*changeF*/
                    //g.setColor(Color.black);
                    if(map[y][x] =='X'){
                        //g.drawLine(x*pix_x, y*pix_y, (x+1)*pix_x, (y+1)*pix_y);
                        //g.drawLine((x+1)*pix_x, y*pix_y, x*pix_x, (y+1)*pix_y);
			//g.drawImage(barrierImg, x-10, y-10, 20, 20, null);
			//g.drawImage(barrierImg, x-(bl_x/2), y-(bl_y/2), bl_x, bl_y, this);
		    }
		    /*changeF*/
                }
                if(map[y][x] == '0') {
                    g.setColor(new Color(200,200,200));
                    g.fillRect(x*pix_x, y*pix_y, pix_x, pix_y);
                }
		//change niimi
		if(map[y][x] == 'F'){
		    g.setColor(Color.red);
		    g.fillRect(x*pix_x, y*pix_y, pix_x, pix_y);
		}
		if(map[y][x] == 'P'){
		    g.setColor(Color.yellow);
		    g.fillRect(x*pix_x, y*pix_y, pix_x, pix_y);
		}
            }
        }
        p.draw(g);
    }
    
    class path {
	int xm,ym,x1,y1;
	
	public void setxy(int x,int y){
            x1 = x;
            y1 = y;
            xm = x/pix_x;
            ym = y/pix_y;
        }
	
        public void draw(Graphics g) {
            g.setColor(Color.blue);
	    
            for(int i=0; i<lx; i++) {
                for(int j=0; j<ly; j++) {
                    if(map[j][i] == '+' || map[j][i] == 'G') {
                        g.fillRect(i*pix_x, j*pix_y, pix_x, pix_y);
                    }
                }
            }
        }
    }
    
    class human {
	int x_now = 0;
	int y_now = 0;
	int x_end = 0;
	int y_end = 0;
	int vx = 0;
	int vy = 0;

	human(){}
	
	human(int x_n,int y_n){
	    x_now = x_n;
	    y_now = y_n;
	}
		    
	public void movehuman(int x , int y){
	    x_now = x; y_now = y;
	}
	
	public void draw(Graphics g){
	    g.setColor(Color.red);
	    g.fillOval(x_now-5,y_now-5,10,10);
	}    
	public void drawhuman(Graphics g){
	    g.setColor(Color.blue);
	    g.fillOval(x_now-5,y_now-5,10,10);
	}
    }
    
    class goal {
	int x = 0;
	int y = 0;
	int xm = 0;
	int ym = 0;

	public void draw(Graphics g) {
	    g.setColor(Color.green);
	    xm = x/30;
	    ym = y/30;
	    g.fillOval(xm*30, ym*30, 30, 30);
	}
    }
    
    /*changeF*/
    class barrier {
	int x, y;
	public int bl_x = 20; 
	public int bl_y = 20;

	public void draw(Graphics g) {
	    g.drawImage(barrierImg, x-(bl_x/2), y-(bl_y/2), bl_x, bl_y, null);
	}
    }

    class button {
	int x,y;
	String name;

	public void draw(Graphics g) {
	    g.setColor(new Color(200,200,200));
	    g.fillRect(x,y,60,30);
	    g.setColor(Color.black);
	    g.drawRect(x,y,60,30);
	    g.drawString(name, x+18, y+18);
	}
    }
    
}

class aStar{
    public static int map_width,map_height;
    public static char[][] aStarmap;
    public static int[] start = new int[2];
    public static int[] goal = new int[2];
    public  static char[][] result = new char[map_height][map_width];
   
    public aStar(char[][] niimimap){
        this.aStarmap = niimimap; 
        this.map_height = aStarmap.length;
        this.map_width = aStarmap[0].length;
        for (int i = 0; i < map_height; i++) {
            for (int j = 0; j < map_width; j++) {
		if ( aStarmap[i][j] == 'S' ) {
		    start[0] = j; 
		    start[1] = i;
		}
		if( aStarmap[i][j] == 'G'){
		    goal[0] = j;
		    goal[1] = i;
		}
            }
        }
        NodeList nodelist = new NodeList();
	//   map_result = nodelist.result;
    }   
}

// Nodeクラス
class Node{
    public int[] pos = new int[2];
    public int hs, fs;
    public Node parent_node;
    public int michihaba;//new
    public int risk;//危険度
    int count1 = 1;//new
    int count2 = 1;//new
    
    // コンストラクタ
    public Node (int x, int y) {
        pos[0] = x;  
	pos[1] = y; 
        hs = (int)(Math.pow(x-aStar.goal[0],2) + Math.pow(y-aStar.goal[1],2));
        fs = 0;
	parent_node = null;

	if(aStar.aStarmap[y][x] == 'F'){
	    risk = 1;
	}else{
	    risk = 0;    
	}
	
	// !NEW! michihaba
	if (aStar.aStarmap[y][x] == '0' || aStar.aStarmap[y][x] == 'X'||aStar.aStarmap[y][x] == 'F') {
	    michihaba = 0;
	}else{
	    for (int i = x+1; i < aStar.map_width; i++) {
		if (aStar.aStarmap[y][i] == '0' || aStar.aStarmap[y][i] == 'X'||aStar.aStarmap[y][i] == 'F') {
		    break;
		}
		count1++;
	    }
	    for (int k = x-1; k > -1; k--) {
		if (aStar.aStarmap[y][k] == '0' || aStar.aStarmap[y][k] == 'X'||aStar.aStarmap[y][k] == 'F') {
		    break;
		}
		count1++;
	    }
	    for (int j = y+1; j < aStar.map_height; j++) {
		if (aStar.aStarmap[j][x] == '0' || aStar.aStarmap[j][x] == 'X'||aStar.aStarmap[j][x] == 'F') {
		    break;
		}
		count2++;
	    }
	    for (int m = y-1; m > -1; m--) {
		if (aStar.aStarmap[m][x] == '0' || aStar.aStarmap[m][x] == 'X'||aStar.aStarmap[m][x] == 'F') {
		    break;
		}
		count2++;
	    }
	    if (count1 > count2) {
		michihaba = count2;
	    }else{
		michihaba = count1;
	    }
	}
	
    }
    
    // 現在地をゴールとするメソッド？
    public static boolean isGoal (Node n) {
	if(n.pos[0] == aStar.goal[0] && n.pos[1] == aStar.goal[1]){	
	    return true;
	}else{
	    return false;
	}
    }
}

//道幅考慮の際、最短経路を格納したArrayListを保持するためのクラス
class Astar_Map{
    ArrayList <Node> p_list = new ArrayList <Node> (); 
    Astar_Map p_node;
    
    Astar_Map(ArrayList<Node> close){
	for(int i=0;i<close.size();i++){
	    p_list.add(close.get(i));
	}
	//p_list = close;  //+となる全てのNodeを格納するArrayList
	p_node = null;   //parent_node
    }

}


class NodeList{
    char[][] newMap = new char[aStar.map_height][aStar.map_width];
    //openリストとcloseリストを設定前半
    ArrayList <Node> open_list = new ArrayList <Node> (); 
    ArrayList <Node> close_list = new ArrayList <Node> (); 
    Node start_node = new Node(aStar.start[0],aStar.start[1]);
    Node end_node; 
    ArrayList <Node> end_list = new ArrayList <Node> (); 
    ArrayList <Node> equal_list = new ArrayList <Node> ();
    ArrayList <Node> path_list = new ArrayList <Node> ();
    char[][] result = new char[aStar.map_height][aStar.map_width];
    Astar_Map a_map; 
	int cycle_num = 1;
	int min_path;

    NodeList(){
	start_node.fs = start_node.hs;
	open_list.add(start_node);
	search_path(open_list);
    }

    public Node findN(int xP,int yP,ArrayList<Node> list){
	Node node;
	for(int i = 0;i<list.size();i++){
	    node = list.get(i);
	    if(node.pos[0] == xP && node.pos[1] == yP){
		return node;
	    }
	}
	return null;
    }

    public boolean find(int xP,int yP,ArrayList<Node> list){
	Node node;
	for(int i = 0;i<list.size();i++){
	    node = list.get(i);
	    if(node.pos[0] == xP && node.pos[1] == yP){
		return true;
	    }
	}
	return false;
    }

    public void delete(ArrayList<Node> list,Node n){
	int num = 0;
	boolean judge = find(n.pos[0],n.pos[1],list);
	if(judge){
	    num = list.indexOf(n);
	    list.remove(num);
	}
    }	

    //pythonにあってjavaにない関数の実装
    public Node min(ArrayList<Node> openM){
	Node minN = openM.get(0);
	Node n;
	int min = minN.fs;
	int ngs = 0;
	boolean flag2 = true;

	for(int i = 0;i<openM.size();i++){
	    n = openM.get(i);
	    if(n.fs < min){
			minN = n;
			min = n.fs;
	    }
	}
	for(int i = 0;i<openM.size();i++){
	    n = openM.get(i);
	    if(n.fs == min && minN != n &&aStar.aStarmap[n.pos[1]][n.pos[0]] != 'S'){
	       	for(int j = 0;j<equal_list.size();j++){
       		if(n.pos[0]==equal_list.get(j).pos[0] &&
		   n.pos[1]==equal_list.get(j).pos[1]){
		    flag2 = false;
		}
		}
		if(flag2==true){
		    ngs = n.fs - n.hs;
		    equal_list.add(n);
		}
	    }
	}
	return minN;
	}
    
    //equal_list内に同じ経路になりうる候補をここで消してる
    public void equal_list_arrangement(ArrayList<Node> equal){
	Node n;
	Node parent;
	ArrayList<Node> delete_list = new ArrayList<Node>();
	

	for(int i= 0;i<equal.size();i++){
	    n = equal.get(i);
	    parent = n.parent_node;
	    while(true){
		if(parent == null) break;
		for(int j = 0;j<equal.size();j++){
		    if(parent == equal.get(j)){
			delete_list.add(equal.get(j));
		    }
		}
		parent = parent.parent_node;
	    }
	}

	for(int i =0;i<delete_list.size();i++){
	    delete(equal_list,delete_list.get(i));
	}
    }

    //2つ目以降の経路探索をする前にclose_listの中身を整理する関数
    public void close_list_change(Node m){
	Node n = m;
	
	close_list.clear();
	while(true){
	    n = n.parent_node;
	    if(n == null){
		break;
	    }
	    close_list.add(n);
	}
    }

    //経路探索
    public void search_path(ArrayList<Node> open){//hikisuu
	Node n;
	Node v;//(2,1)
	Node new_start_node;
	ArrayList <Node> new_open_list = new ArrayList <Node> (); //引数リスト
	int dist,n_gs;//
	boolean naname;
	boolean flag;
	int x = 0 ;
	int y = 0;
	//boolean naname = false;
	int sum_width = 0;

	while(true){
	    if(open.size() == 0){
		System.out.println("There is no route until reaching a goal.");
		help_search_path2();
		break;
	    }

	    n = min(open);
	    delete(open,n);
	    close_list.add(n); 

	    //new!	    
	    if(Node.isGoal(n)){	    
			end_node = n;
	       	help_search_path1();
			cycle_num++;
			equal_list_arrangement(equal_list);
		if(equal_list.size() == 0){
		    System.out.println("探索終了!");
		    help_search_path2();
		    break;
		}else{
		    new_start_node = equal_list.get(equal_list.size()-1);
		    close_list_change(new_start_node);
		    delete(equal_list,equal_list.get(equal_list.size()-1));
		    new_open_list.add(new_start_node);  		       
		    search_path(new_open_list);
		    break;		    
		}
	    }
	    
	    n_gs = n.fs - n.hs; 
	    
	    /*ノードnの移動可能方向のノードを調べる
	      for v in ((1,0),(-1,0),(0,1),(0,-1)):*/
	    for(int i=0;i<4;i++){
		if(i==0){
		    x = n.pos[0] + 1;
		    y = n.pos[1] + 0;
		    naname = false;
		}else if(i==1){
		    x = n.pos[0] + -1;
		    y = n.pos[1] + 0;
		    naname = false;
		}else if (i==2){
		    x = n.pos[0] + 0;
		    y = n.pos[1] + 1;
		    naname = false;
		}else if(i==3){
		    x = n.pos[0] + 0;
		    y = n.pos[1] + -1;
		    naname = false;
		}else if (i==4){
		    x = n.pos[0] + 1;
		    y = n.pos[1] + 1;
		    naname = true;
		}else if (i==5){
		    x = n.pos[0] + -1;
		    y = n.pos[1] + 1;
		    naname = true;
		}else if (i==6){
		    x = n.pos[0] + 1;
		    y = n.pos[1] + -1;
		    naname = true;
		}else{
		    x = n.pos[0] + -1;
		    y = n.pos[1] + -1;
		    naname = true;
		} 
		
		/*マップが範囲外または壁(0)の場合はcontinue*/
		if (y <= 0 || y >= aStar.map_height ||
		    x <= 0 || x >= aStar.map_width ||
		    (aStar.aStarmap[y][x] == '0'||aStar.aStarmap[y][x] == 'X')) {
		    continue;
		}
		
		/*移動先のノードがOpen,Closeのどちらのリストに
		  格納されているか、または新規ノードなのかを調べる*/
		flag = find(x,y,open);
		v = findN(x,y,open);
		dist = (int)(Math.pow((n.pos[0]-x),2) + Math.pow((n.pos[1]-y),2));
		/*	if(naname){
			dist = dist+6;
			}*/
		
		
		if(flag){
		    /*移動先のノードがOpenリストに格納されていた場合、
		      より小さいf*ならばノードmのf*を更新し、親を書き換え*/
		    if (v.fs > n_gs + v.hs + dist){
			v.fs = n_gs + v.hs + dist;
			v.parent_node = n;
		    }
		}else{
		    flag = find(x,y,close_list);
		    v = findN(x,y,close_list);
		    if(flag){
			/*移動先のノードがCloseリストに格納されていた場合、
			  より小さいf*ならばノードmのf*を更新し、親を書き換え
			  かつ、Openリストに移動する*/
			if(v.fs > n_gs + v.hs + dist){
			    v.fs = n_gs + v.hs + dist;
			    v.parent_node = n;
			    open.add(v);
			    delete(close_list,v);
			}
		    }else{
			/*新規ノードならばOpenリストにノードに追加*/
			v = new Node(x,y);
			v.fs = n_gs + v.hs + dist;
			v.parent_node = n;
			open.add(v);
		    }
		}
	    }
	}
    }
    
    //最短経路の候補の経路を保存する関数
    public void help_search_path1(){
	Node path = end_node.parent_node;
	Astar_Map m;
	
	// ルートとなるノードに印をつける
	while (true) {
	    if (path.parent_node == null) break;
	    path_list.add(path);
	    path = path.parent_node;
	}

	if(cycle_num == 1){
		min_path = path_list.size();
		m = new Astar_Map(path_list);
		a_map = m;
	}else{
		if(min_path<path_list.size()){
			//min_pathより大きい時は格納しない
			return;
		}else{
			//マップ情報を格納・保持
			m = new Astar_Map(path_list);
			m.p_node = a_map;
			a_map = m;
		}
	}
	path_list.clear();  

    }

    //道幅が最も大きい道を通る経路を候補から選び出し、その経路を表示する関数
    public void help_search_path2(){
	ArrayList<Integer> michihaba_width = new ArrayList<Integer>();
	ArrayList <Integer> sum_risk_list  = new ArrayList <Integer> (); 
	ArrayList<Astar_Map> arg = new ArrayList<Astar_Map>();
	int haba = 0;
	int danger = 0;
	int candidate_num = 0;//候補の数
	int standard_num = 0;//足切りの基準値。これで候補を5つ位に絞る
	int max = 0;
	int min = 0;
	Astar_Map m1 = a_map;
	Astar_Map m2 = a_map;
	int count =0;

	while(true){
	    if(m1 ==null) break;
	    for(int i=0;i<m1.p_list.size();i++){
		haba += m1.p_list.get(i).michihaba;
		danger += m1.p_list.get(i).risk;
	    }
	    michihaba_width.add(haba);
	    sum_risk_list.add(danger);
	    haba = 0;
	    danger = 0;
	    m1 = m1.p_node;
	}

	min = sum_risk_list.get(0);
	for(int i = 0;i<sum_risk_list.size();i++){
	    if(sum_risk_list.get(i)<min){
		min = sum_risk_list.get(i);
	    }
	}
	
	if(min == 0){
	    standard_num = min + 2;
	}else{
	    standard_num = min + 1;
	}

	for(int i = 0;i<michihaba_width.size();i++){
	    if(max<michihaba_width.get(i)&&sum_risk_list.get(i)<=standard_num){
		max = michihaba_width.get(i);
		count = i;
	    }
	}

	while(count >0){
	    m2 = m2.p_node;
	    count--;
	}

	//map informatin making
	for (int i = 0; i < aStar.map_height; i++) {
	    for (int j = 0; j < aStar.map_width; j++) {
		result[i][j] = aStar.aStarmap[i][j];
	    }
	}
		
	  for (int i = 0; i < aStar.map_height; i++) {
	    String s = String.valueOf(result[i]);
	    System.out.println(s);
	}	
	
	for(int i = 0;i<m2.p_list.size();i++){
	    result[m2.p_list.get(i).pos[1]][m2.p_list.get(i).pos[0]] = '+';
	}

	for (int i = 0; i < aStar.map_height; i++) {
	    for (int j = 0; j < aStar.map_width; j++) {
		Project2.map_result[i][j] = result[i][j];
	    }
	}		

	/*
	  System.out.println("新しい道幅考慮したマップ:");
	  for (int i = 0; i < aStar.map_height; i++) {
	    String s = String.valueOf(result[i]);
	    System.out.println(s);
	    }*/
	


    }
}
