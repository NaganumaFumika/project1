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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class experiment2 extends JPanel{
    Map map = new Map();
    Map.human user = map.new human();
    //Map.barrier obs = map.new barrier();
    Map.goal goal = map.new goal();
    Map.button fire = map.new button();
    Map.button tree = map.new button();
    Map.button research = map.new button();
    int xl = map.lx;//maisuu  panel
    int yl = map.ly;//maisuu  panel
    int xn = 900/xl;//x no panel no nagasa
    int yn = 900/yl;//y no panel no nagasa
    int n = 1;//user komi
    ArrayList<Map.human> human = new ArrayList <Map.human>();
    ArrayList<Map> m = new ArrayList<Map>();
    ArrayList<aStar> as = new ArrayList <aStar>();
    ArrayList<Map.barrier> obs = new ArrayList <Map.barrier>();
    public static char[][] map_result = new char [900][900];
    public char button_flag;
    int x_g =272;
    int y_g =142;

    experiment2 (){
	human.add(user);
	m.add(map);
	setOpaque(false);
	fire.x = 920;
	fire.y = 30;
	fire.name = "火災";
	tree.x = 990;
	tree.y = 30;
	tree.name = "倒木";
	research.x = 920;
	research.y = 230;
	research.name = "再検索";
	//char button_flag;
	int x_s =413;
	int y_s = 348;
	int x_g =272;
	int y_g =142;
	map.map[y_s][x_s] = 'S'; 
	map.map[y_g][x_g] = 'G';

	user.movehuman(x_s/xn*xn+xn/2,y_s/yn*yn+yn/2);
	map.p.setxy(x_s/xn*xn+xn/2,y_s/yn*yn+yn/2);
	Random rnd = new Random();
	int x_rnd = 0;int y_rnd = 0;;
	for(int i = 1;i < n ;i++){
	    m.add(new Map());
	    m.get(i).map[y_g][x_g] = 'G';
	    while(true){
		x_rnd = rnd.nextInt(xl);
		y_rnd = rnd.nextInt(yl);
		if(m.get(i).road(x_rnd*xn , y_rnd*yn) == true ){
		    /*for (int k = 0; k < 150; k++) {
		      for(int j=0; j<150; j++) {
		      String s = String.valueOf(map.map[j][k]);
		      System.out.print(s);
		      }
		      System.out.println();
		      }*/
		    m.get(i).map[y_rnd][x_rnd] ='S';
		    break;
		}						
	    }
	    human.add(m.get(i).new human(x_rnd*xn+xn/2 , y_rnd*xn+xn/2));
	}
	long start = System.currentTimeMillis();
	aStar as ;
	for (int i = 0;i<n ;i++){
	    as = new aStar(m.get(i).map); 
	    for(int a = 0;a < xl;a++){
		for(int b = 0;b < yl;b++){
		    m.get(i).map[b][a] = map_result[b][a];
		}
		
	    }
	    /*for (int k = 0; k < 150; k++) {
	      for(int j=0; j<150; j++) {
	      String s = String.valueOf(m.get(0).map[j][k]);
	      System.out.print(s);
	      }
	      System.out.println();
	      }*/					
	    as = null;
	}
	long end = System.currentTimeMillis();
	System.out.println("かかった時間 "+ (end - start)  + "ms");
	repaint();
	
	addMouseListener(new MouseAdapter() {
		int i = -1;
		int s_x, s_y, e_x, e_y;
		boolean click_map = false;
		//char button_flag;
		
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
				    //System.out.println(e.getX()/xn);
				    //System.out.println(e.getY()/yn);
				    s_x = e.getX()/xn;
				    s_y = e.getY()/yn;
				    repaint();
				    i = 1;
				}else if( i == 1 ){
				    System.out.println("click i=1");	
				    user.x_end = e.getX();
				    user.y_end = e.getY();
				    goal.x = e.getX();
				    goal.y = e.getY();
				    System.out.println(e.getX()/xn);
                                    System.out.println(e.getY()/yn);
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
						/*for (int k = 0; k < 150; k++) {
						    for(int j=0; j<150; j++) {
							String s = String.valueOf(map.map[j][k]);
							System.out.print(s);
						    }
						    System.out.println();
						    }*/
						m.get(i).map[y_rnd][x_rnd] ='S';
						break;
					    }						
					}
					human.add(m.get(i).new human(x_rnd*xn+xn/2 , y_rnd*xn+xn/2));
				    }
				    long start = System.currentTimeMillis();
				    aStar as ;
				    for (int i = 0;i<n ;i++){
					as = new aStar(m.get(i).map); 
					for(int a = 0;a < xl;a++){
					    for(int b = 0;b < yl;b++){
						m.get(i).map[b][a] = map_result[b][a];
					    }
					    
					}
					/*for (int k = 0; k < 150; k++) {
					    for(int j=0; j<150; j++) {
						String s = String.valueOf(m.get(0).map[j][k]);
						System.out.print(s);
					    }
					    System.out.println();
					    }*/					
					as = null;
				    }
				    long end = System.currentTimeMillis();
				    System.out.println("かかった時間 "+ (end - start)  + "ms");
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
				    for(int a=0; a<900; a++) {
					for(int b=0; b<900; b++) {
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
				    long start = System.currentTimeMillis();
				    aStar as;
				    for (int k = 0;k<n;k++){
					m.get(k).map[human.get(k).y_now/yn][human.get(k).x_now/xn] = 'S';
					as =  new aStar(m.get(k).map) ;
					for(int a = 0;a < xl;a++){
					    for(int b = 0;b < yl;b++){
						m.get(k).map[b][a] = map_result[b][a];
					    }
					}
				    }
				    long end = System.currentTimeMillis();
				    System.out.println("かかった時間 "+ (end - start)  + "ms");
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
				//change
				repaint();
			    }
			    else if (990<=e.getX() && e.getX()<=1050 && 30<=e.getY() && e.getY()<=60) {
				button_flag = 't';
				i = 2;
				click_map = true;
				System.out.println("倒木 is selected");

				//change
				repaint();
			    } else if(research.x<=e.getX() && e.getX()<=research.x+60 && research.y<=e.getY() && e.getY()<=research.y+60){
				//for(int k = 0; k<n;k++){
				    
				    for(int i=0; i<xl; i++) {
					
					for(int j=0; j<yl; j++) {
					    
					    if(m.get(0).map[j][i] == '+' || m.get(0).map[j][i] == 'S') {

						m.get(0).map[j][i] = ' ';
						
					    }
					    
					}
					
				    }
				    
				    //}
				
				
				    long start = System.currentTimeMillis();
				    aStar as;
				
				//for (int k = 0;k<n;k++){
				    
				    m.get(0).map[human.get(0).y_now/yn][human.get(0).x_now/xn] = 'S';
				    
				    as =  new aStar(m.get(0).map) ;
				    
				    for(int a = 0;a < xl;a++){
					
					for(int b = 0;b < yl;b++){
					    
					    m.get(0).map[b][a] = map_result[b][a];
					    
					}
					
				    }
				    long end = System.currentTimeMillis();
				    System.out.println("かかった時間 "+ (end - start)  + "ms");
				    //}
			    }else {
				System.out.println("Please click button");
			    }
			}
		    }
		}
	    });

	

	new javax.swing.Timer(30, new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		    //change niimi
		    for(int k = 0; k < m.size(); k++ ){
			m.get(k).fire(5);
		    }
		   speedy_move(1);
		   repaint();
		}
	    }).start(); 
    }

    public void speedy_move(int s){
     for(int t = 0 ; t < human.size(); t++){//humanでループ
	    int xm = human.get(t).x_now/xn;//マスをだす	    
	    int ym = human.get(t).y_now/yn;//マスを出す
	    if((human.get(t).x_now-xn/2)%xn == 0 && (human.get(t).y_now-yn/2)%yn == 0){
		//int i = 0;
		m.get(t).map[ym][xm] = ' ';
		int x_max=0;//移動する場所
		int y_max=0;
		for(int x= -s; x <s+1  ; x++ ){//x向きに+探索
		    if (xm + x > -1&& xm + x < xl){//枠外にでないようにする、でそうなら範囲を長方形にする
			if(ym-s >-1){//sマイナス側チェック
			    if(m.get(t).map[ym-s][xm+x] == '+'||m.get(t).map[ym-s][xm+x] == 'G' ){
				if(x*x+s*s>x_max*x_max+y_max*y_max){
				    x_max =x;
				    y_max =-s;
				}
			    }
			}else{
			    if(m.get(t).map[0][xm+x] == '+'||m.get(t).map[0][xm+x] == 'G' ){
				if(x*x+s*s>x_max*x_max+y_max*y_max){
				    x_max =x;
				    y_max =-ym;
				}
			    }
			}
			
			if(ym+s < yl){//sプラス側チェック
			    if(m.get(t).map[ym+s][xm+x] == '+'||m.get(t).map[ym+s][xm+x] == 'G' ){
				if(x*x+s*s>x_max*x_max+y_max*y_max){
				    x_max =x;
				    y_max =s;
				}
			    }
			}else{
			    if(m.get(t).map[yl-1][xm+x] == '+'||m.get(t).map[yl-1][xm+x] == 'G' ){
				if(x*x+s*s>x_max*x_max+y_max*y_max){
				    x_max =x;
				    y_max =yl-ym;
				}
			    }
			}
		    }
		}
		
		for(int y= -s; y <s+1  ; y++ ){//y向きに+探索
		    
		    if (ym + y > -1&& ym + y < yl){//枠外にでないようにする、でそうなら範囲を長方形にする
			if(xm-s >-1){
			    if(m.get(t).map[ym+y][xm-s] == '+'||m.get(t).map[ym+y][xm-s] == 'G' ){
				if(s*s+y*y>x_max*x_max+y_max*y_max){
				    x_max =-s;
				    y_max =y;
				}
			    }
			}else{
			    if(m.get(t).map[ym+y][0] == '+'||m.get(t).map[ym+y][0] == 'G' ){
				if(s*s+y*y>x_max*x_max+y_max*y_max){
				    x_max =-xm;
				    y_max =y;
				}
			    }
			}
			if(xm+s < xl){
			    if(m.get(t).map[ym+y][xm+s] == '+'||m.get(t).map[ym+y][xm+s] == 'G' ){
				if(s*s+y*y>x_max*x_max+y_max*y_max){
				    x_max =s;
				    y_max =y;
				}
			    }
			}else{
			    if(m.get(t).map[ym+y][xl-1] == '+'||m.get(t).map[ym+y][xl-1] == 'G' ){
				if(s*s+y*y>x_max*x_max+y_max*y_max){
				    x_max =xl-xm;
				    y_max =y;
				}
			    }
			}
		    }
		}

		//枠内の+を消す
		if(x_max > -1){
		    for(int x =0; x < x_max + 1; x++){
			if(y_max > -1){
			    for(int y = 0; y < y_max + 1; y++){
				if(m.get(t).map[ym+y][xm+x] == '+' ){
				    m.get(t).map[ym+y][xm+x] = ' '	;
				}
			    }
			}else{
			    for(int y = y_max; y < 1; y++){
				if(m.get(t).map[ym+y][xm+x] == '+' ){
				    m.get(t).map[ym+y][xm+x] = ' '	;
				}
			    }
			}
		    }
		}else{
		    for(int x = x_max; x <  1; x++){
			if(y_max > -1){
			    for(int y = 0; y < y_max + 1; y++){
				if(m.get(t).map[ym+y][xm+x] == '+' ){
				    m.get(t).map[ym+y][xm+x] = ' '	;
				}
			    }
			}else{
			    for(int y = y_max; y <  1; y++){
				if(m.get(t).map[ym+y][xm+x] == '+' ){
				    m.get(t).map[ym+y][xm+x] = ' '	;
				}
			    }
			}
		    }
		}
		if(x_max == 0&&y_max==0){//探索枠内にゴールある
		    for(int x= -s; x <s+1  ; x++ ){//x向きに+探索
			if (xm + x > -1&& xm + x < xl){//枠外にでないようにする、でそうなら範囲を長方形にする
			    for(int y= -s; y <s+1  ; y++ ){//y向きに+探索
				if (ym + y > -1&& ym + y < yl){//枠外にでないようにする、でそうなら範囲を長方形にする
				    if(m.get(t).map[ym+y][xm+x] == 'G' ){
					x_max = x;y_max = y;
				    }
				    
				}
			    }
			}
		    }
		}
		
		//これで移動場所は決定、これから移動する。
		human.get(t).vx = x_max; human.get(t).vy = y_max; //i = 1;
		human.get(t).movehuman(human.get(t).x_now+human.get(t).vx ,human.get(t).y_now + human.get(t).vy);
		/*if(t ==  0){
		  m.get(t).p.setxy(human.get(t).x_now+human.get(t).vx ,human.get(t).y_now + human.get(t).vy);
			}
			repaint();
		break;*/
		/*if(i == 0&&y==1&&x==1){
		  human.get(t).vx = 0;human.get(t).vy = 0; break;
		    }*/
	    }else{
		human.get(t).movehuman(human.get(t).x_now + human.get(t).vx ,human.get(t).y_now + human.get(t).vy);
		/*if(t == 0){
		  m.get(t).p.setxy(human.get(t).x_now+human.get(t).vx ,human.get(t).y_now + human.get(t).vy);
		    }*/
		//repaint();
	    }
	}
 }
    
    
    @Override
	public void paintComponent(Graphics g){
	g.drawImage(map.mapImg2,0,0,900,900,this);
	map.draw(g);
	fire.draw(g);
	tree.draw(g);
	research.draw(g);

	//change
	if(button_flag == 'f') {
		g.setColor(Color.gray);
		g.fillRect(fire.x, fire.y,60,30);
		g.setColor(Color.black);
		g.drawRect(fire.x, fire.y,60,30);
		g.drawString(fire.name, fire.x+18, fire.y+18);
	}

	if(button_flag == 't') {
	    g.setColor(Color.gray);
	    g.fillRect(tree.x, tree.y,60,30);
	    g.setColor(Color.black);
	    g.drawRect(tree.x, tree.y,60,30);
	    g.drawString(tree.name, tree.x+18, tree.y+18);
	}

	if((user.x_now!=0) &&(user.y_now!=0)) {
	    user.draw(g);
	    for(int i = 1 ;i<human.size();i++){
		human.get(i).drawhuman(g);
	    }
	}

	/*if((user.x_end!=0) &&(user.y_end!=0)) {
	    goal.draw(g);
	    }*/
	g.setColor(Color.green);
	g.fillOval(x_g-15, y_g-15, 30, 30);
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
	experiment2 pr =new  experiment2();
        pr.setOpaque(false);
	fr.add(pr);
	fr.setVisible(true);
    }
}

class Map extends JPanel{
    public static int lx = 900, ly = 900;
    public char map[][];
    path p = new path();

    //追加部分 
    public static int pix_x = 900/lx;
    public static int pix_y = 900/ly;

    int x_clixked,y_clicked;

    /*changeF*/
    private BufferedImage barrierImg;
    public int bl_x = 20;
    public int bl_y = 20;
    /*changeF*/
    public BufferedImage mapImg2;
    public int mapColor[][];
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
	    for (int x=0; x<lx; x++) {
		for (int y=0; y<ly; y++) {
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
        for (int x=0; x<lx; x++) {
            for (int y=0; y<ly; y++) {
                map[y][x] = '0';
            }
        }

	try{
	    File barrierFile = new File("./barrier.png");
            barrierImg = ImageIO.read(barrierFile);
	    File mapFile = new File("./map5.png");
	    BufferedImage mapImg = ImageIO.read(new File("./map5.png"));
	    mapImg2 = ImageIO.read(mapFile);
	    
	    for(int j=0; j<ly; j++) {
		for(int i=0; i<lx; i++) {
		    Color color = new Color(mapImg.getRGB(i, j));
		    if(color.getRed() >= 253 && color.getGreen() >= 253 && color.getBlue() >= 253 ){
			map[j][i] = ' ';
		    }
		}
	    }
	} catch (IOException e) {
	    System.out.println("Failed to read file");
	}
	
	/*for(int x=0; x<lx; x++) {
	    for(int y=0; y<ly; y++) {
		for(int i=110; i<130; i++) {
		    map[i][y] = ' ';
		}
		for(int i=270; i<285; i++) {
		    map[i][y] = ' ';
		}
		for(int i=450; i<460; i++) {
		    map[i][y] = ' ';
		}
		for(int i=100; i<120; i++) {
		    map[x][i] = ' ';
		}
		for(int i=300; i<350; i++) {
		    map[x][i] = ' ';
		}
		for(int i=370; i<400; i++) {
		    map[x][i] = ' ';
		}
		for(int i=650; i<670; i++) {
		    map[x][i] = ' ';
		}
		for(int i=770; i<780; i++) {
		    map[x][i] = ' ';
		}
		if(110<=y) {
		    for(int i=90; i<120; i++) {
			map[i][y] = ' ';
		    }
		}
		if(100<=y) {
		    for(int i=150; i<165; i++) {
			map[i][y] = ' ';
		    }
		}
		if(x<=450){
		    for(int i=390; i<420; i++){
			map[x][i] = ' ';
		    }
		}
		if(120 <= y){
		    for(int i=660; i<750; i++){
			map[i][y] = ' ';
		    }
		}
		if(x<=660) {
		    for(int i=200; i<225; i++){
			map[x][i] = ' ';
		    }
		}
		if(370<=y && y<=780){
		    for(int i=540; i<555; i++){
			map[i][y] = ' ';
		    }
		}
	    }
	    }
	try{
	    File barrierFile = new File("./barrier.png");
	    barrierImg = ImageIO.read(barrierFile);
	} catch (IOException e) {
	    System.out.println("Failed to read file");
	    }*/
    }
    public void draw(Graphics g) {
        for (int x=0; x<lx; x++) {
            for (int y=0; y<ly; y++) {
                /*if(map[x][y] == 'S' || map[x][y] == 'G' || map[x][y] == ' ' || map[x][y] == '+' ||map[x][y] == 'X' ) {
		  g.setColor(Color.white); //?
		  g.fillRect(x*pix_x, y*pix_y, pix_x, pix_y);
		  g.setColor(Color.black);
		  if(map[x][y] =='X'){
		  g.drawLine(x*pix_x, y*pix_y, (x+1)*pix_x, (y+1)*pix_y);
		  g.drawLine((x+1)*pix_x, y*pix_y, x*pix_x, (y+1)*pix_y);
		  g.drawImage(barrierImg, x-10, y-10, 20, 20, null);
		  g.drawImage(barrierImg, x-(bl_x/2), y-(bl_y/2), bl_x, bl_y, this);
		  }
		  }
		  if(map[x][y] == '0') {
		  g.setColor(new Color(200,200,200));
		  g.fillRect(x*pix_x, y*pix_y, pix_x, pix_y);
		  }*/
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

    public aStar(char[][] niimimap) {
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
	}
}

// Nodeクラス
class Node{
    public int[] pos = new int[2];
    public int Shs, Sfs; /////
    public int Ghs, Gfs; /////
    public Node Sparent_node, Gparent_node; /////
    public int michihaba;//new
    public int risk;
    int count1 = 1;//new
    int count2 = 1;//new
 
    // コンストラクタ
    public Node (int x, int y) {
        pos[0] = x;  
        pos[1] = y; 
        Shs = (int)(Math.pow(x-aStar.goal[0],2) + Math.pow(y-aStar.goal[1],2)); /////
        Sfs = 0; /////
        Ghs = (int)(Math.pow(x-aStar.start[0],2) + Math.pow(y-aStar.start[1],2));; /////
        Gfs = 0; /////
        Sparent_node = null; /////
        Gparent_node = null; /////

	if(aStar.aStarmap[y][x] == 'F'){
	    risk = 1;
	}else{
	    risk = 0;    
	}
     
	// !NEW! michihaba
	if (aStar.aStarmap[y][x] == '0' || aStar.aStarmap[y][x] == 'X') {
	    michihaba = 0;
	}else{
	    for (int i = x+1; i < aStar.map_width; i++) {
		if (aStar.aStarmap[y][i] == '0' || aStar.aStarmap[y][i] == 'X') {
		    break;
		}
		count1++;
	    }
	    for (int k = x-1; k > -1; k--) {
		if (aStar.aStarmap[y][k] == '0' || aStar.aStarmap[y][k] == 'X') {
		    break;
		}
		count1++;
	    }
	    for (int j = y+1; j < aStar.map_height; j++) {
		if (aStar.aStarmap[j][x] == '0' || aStar.aStarmap[j][x] == 'X') {
		    break;
		}
		count2++;
	    }
	    for (int m = y-1; m > -1; m--) {
		if (aStar.aStarmap[m][x] == '0' || aStar.aStarmap[m][x] == 'X') {
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
    
    // 現在地をゴールとするメソッド
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
	p_node = null;   //parent_node
    }
}


class NodeList{
    char[][] newMap = new char[aStar.map_height][aStar.map_width];
    ArrayList <Node> open_list1 = new ArrayList <Node> (); /////
    ArrayList <Node> close_list1 = new ArrayList <Node> (); /////
    ArrayList <Node> open_list2 = new ArrayList <Node> (); /////
    ArrayList <Node> close_list2 = new ArrayList <Node> (); /////
    Node start_node = new Node(aStar.start[0],aStar.start[1]);
    Node goal_node = new Node(aStar.goal[0],aStar.goal[1]); /////
    Node Send_node, Gend_node; /////
    ArrayList <Node> end_list = new ArrayList <Node> (); 
    ArrayList <Integer> sum_michihaba  = new ArrayList <Integer> ();  
    ArrayList <Node> equal_list1 = new ArrayList <Node> ();
    ArrayList <Node> equal_list2 = new ArrayList <Node> ();
    ArrayList <Integer> ngs_list = new ArrayList <Integer> (); 
    ArrayList <Node> path_list = new ArrayList <Node> ();
    Astar_Map a_map; 
    int cycle_num = 1;
	int min_path;
	long startTime;//タイムアップ


    NodeList(){
		startTime = System.currentTimeMillis();//タイムアップ
	start_node.Sfs = start_node.Shs; /////
	goal_node.Gfs = goal_node.Ghs; /////
	open_list1.add(start_node); /////
	open_list2.add(goal_node); /////
	search_path(open_list1, open_list2); /////
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
    public Node minS(ArrayList<Node> openM){
	Node minN = openM.get(0);
	Node n;
	int min = minN.Sfs;
	int ngs = 0;
	boolean flag2 = true;

	for(int i = 0;i<openM.size();i++){
	    n = openM.get(i);
	    if(n.Sfs < min){
		minN = n;
		min = n.Sfs;
	    }
	}
	for(int i = 0;i<openM.size();i++){
	    n = openM.get(i);
	    if(n.Sfs == min && minN != n && aStar.aStarmap[n.pos[1]][n.pos[0]] != 'S'){
	       	for(int j = 0;j<equal_list1.size();j++){
		    if(n.pos[0]==equal_list1.get(j).pos[0] &&
		       n.pos[1]==equal_list1.get(j).pos[1]){
			flag2 = false;
		    }
		}
		if(flag2==true){
		    ngs = n.Sfs - n.Shs;
		    equal_list1.add(n);
		    ngs_list.add(ngs);
		}
	    }
	}
	return minN;
    }
    
    public Node minG(ArrayList<Node> openM){
        Node minN = openM.get(0);
        Node n;
        int min = minN.Gfs;
        int ngs = 0;
        boolean flag2 = true;
    
        for(int i = 0;i<openM.size();i++){
            n = openM.get(i);
            if(n.Gfs < min){
                minN = n;
                min = n.Gfs;
            }
        }
        for(int i = 0;i<openM.size();i++){
            n = openM.get(i);
            if(n.Gfs == min && minN != n && aStar.aStarmap[n.pos[1]][n.pos[0]] != 'S'){
                   for(int j = 0;j<equal_list2.size();j++){
                   if(n.pos[0]==equal_list2.get(j).pos[0] &&
		      n.pos[1]==equal_list2.get(j).pos[1]){
		       flag2 = false;
		   }
		   }
		   if(flag2==true){
		       ngs = n.Gfs - n.Ghs;
		       equal_list2.add(n);
		       ngs_list.add(ngs);
		   }
            }
        }
        return minN;
    }

    //equal_list内に同じ経路になりうる候補をここで消してる
    public void equal_list_arrangement(ArrayList<Node> equal,int id){/////このへん変更したyo!
	Node n;
	Node parent;
	ArrayList<Node> delete_list = new ArrayList<Node>();
	
	if(id==0){
	    for(int i= 0;i<equal.size();i++){
	        n = equal.get(i);
	        parent = n.Sparent_node;
		while(true){
		    if(parent == null) break;
		    for(int j = 0;j<equal.size();j++){
			if(parent == equal.get(j)){
			    delete_list.add(equal.get(j));
    		        }
	    	    }
		    parent = parent.Sparent_node;
		}
	    }
	    for(int i =0;i<delete_list.size();i++){
	        delete(equal,delete_list.get(i));
	    }
	}else{
	    for(int i= 0;i<equal.size();i++){
		n = equal.get(i);
		parent = n.Gparent_node;
		while(true){
		    if(parent == null) break;
		    for(int j = 0;j<equal.size();j++){
			if(parent == equal.get(j)){
			    delete_list.add(equal.get(j));
			}
		    }   
		    parent = parent.Gparent_node;
		}
	    }
	    for(int i =0;i<delete_list.size();i++){
		delete(equal,delete_list.get(i));
	    }
	}
    }
    
    //2つ目以降の経路探索をする前にclose_listの中身を整理する関数  
    public void close_list_change(Node m,int id){/////このへん変更したyo!
	Node n = m;
	
	if(id==1){
	    close_list1.clear();
	    while(true){
	        n = n.Sparent_node;
	        if(n == null){
		    break;
	        }
	        close_list1.add(n);
	    }
	}else{
	    close_list2.clear();
	    while(true){
	        n = n.Gparent_node;
	        if(n == null){
		    break;
	        }
	        close_list2.add(n);
	    }
	}	
    }

    //経路探索
    public void search_path(ArrayList<Node> open1, ArrayList<Node> open2){ /////
	Node n1,n2;
	Node new_start_node1;
	Node new_start_node2;
	ArrayList <Node> new_open_list1 = new ArrayList <Node> (); //引数リスト
	ArrayList <Node> new_open_list2 = new ArrayList <Node> (); //引数リスト
	ArrayList <Node> parent_list = new ArrayList <Node> (); /////
	/*int dist,n_gs;
	  boolean flag;
	  int x = 0;
	  int y = 0;*/
	long currentTime;//タイムアップ
	long processingTime;//タイムアップ
	
	while(true){
		currentTime = System.currentTimeMillis();//タイムアップ↓
        processingTime = currentTime-startTime;
        if(processingTime>12000){//制限時間
            System.out.println("Time is up.");
            help_search_path2();
            return;
        }//タイムアップ↑
	    if(open1.size() == 0 || open2.size() == 0){ /////
		System.out.println("There is no route until reaching a goal.");
		equal_list_arrangement(equal_list1,1);/////このへん変更したyo!
		equal_list_arrangement(equal_list2,2);/////このへん変更したyo!
		if(equal_list2.size() == 0 && equal_list1.size() == 0){
		    System.out.println("探索終了!");
		    help_search_path2();
		    //System.exit(1);
		    return;
		}else if (equal_list1.size() == 0){
		    new_start_node2 = equal_list2.get(equal_list2.size()-1);
	    	    close_list_change(new_start_node2,1);/////このへん変更したyo!
		    delete(equal_list2,equal_list2.get(equal_list2.size()-1));
		    new_open_list2.add(new_start_node2);
		    search_path(open1, new_open_list2); /////このへん変更したyo!
		    return;
		}else{
		    new_start_node1 = equal_list1.get(equal_list1.size()-1);
	    	    close_list_change(new_start_node1,2);/////このへん変更したyo!
		    delete(equal_list1,equal_list1.get(equal_list1.size()-1));
		    new_open_list1.add(new_start_node1);        
		    search_path(new_open_list1, open2); /////このへん変更したyo!
		    return;
		}
	    }
	    
	    n1 = minS(open1); /////
	    delete(open1,n1);
	    close_list1.add(n1); 
	    
	    search(n1,open1,1);
	    if(open1.size() == 0 || open2.size() == 0){   /////このへん変更したyo!
		System.out.println("There is no route until reaching a goal.");
		equal_list_arrangement(equal_list1,1);/////このへん変更したyo!
		equal_list_arrangement(equal_list2,2);/////このへん変更したyo!
		if(equal_list2.size() == 0 && equal_list1.size() == 0){
		    System.out.println("探索終了!");
		    help_search_path2();
		    //System.exit(1);
		    return;
		}else if (equal_list1.size() == 0){
		    new_start_node2 = equal_list2.get(equal_list2.size()-1);
	    	    close_list_change(new_start_node2,1);/////このへん変更したyo!
		    delete(equal_list2,equal_list2.get(equal_list2.size()-1));
		    new_open_list2.add(new_start_node2);
		    search_path(open1, new_open_list2); /////このへん変更したyo!
		    return;
		}else{
		    new_start_node1 = equal_list1.get(equal_list1.size()-1);
	    	    close_list_change(new_start_node1,2);/////このへん変更したyo!
		    delete(equal_list1,equal_list1.get(equal_list1.size()-1));
		    new_open_list1.add(new_start_node1);        
		    search_path(new_open_list1, open2); /////このへん変更したyo!
		    return;
		}
	    }
	    
	    n2 = minG(open2);
	    delete(open2,n2);
	    close_list2.add(n2); 
	    
	    Node z1 = minS(open1);
	    while (z1 != null) {
		parent_list.add(z1);
		z1 = z1.Sparent_node;
	    }
	    
	    if (find(n2.pos[0],n2.pos[1],parent_list)) {
		Gend_node = n2; /////
		Send_node = findN(n2.pos[0],n2.pos[1],parent_list); /////
		help_search_path1(); 
		cycle_num++;
		equal_list_arrangement(equal_list1,1);/////このへん変更したyo!
		equal_list_arrangement(equal_list2,2);/////このへん変更したyo!
		if(equal_list2.size() == 0 && equal_list1.size() == 0){
		    System.out.println("探索終了!");
		    help_search_path2();
		    //System.exit(1);
		    return;
		}else if (equal_list1.size() == 0){
		    new_start_node2 = equal_list2.get(equal_list2.size()-1);
	    	    close_list_change(new_start_node2,1);/////このへん変更したyo!
		    delete(equal_list2,equal_list2.get(equal_list2.size()-1));
		    new_open_list2.add(new_start_node2);
		    search_path(open1, new_open_list2); /////このへん変更したyo!
		    return;
		}else{
		    new_start_node1 = equal_list1.get(equal_list1.size()-1);
	    	    close_list_change(new_start_node1,2);/////このへん変更したyo!
		    delete(equal_list1,equal_list1.get(equal_list1.size()-1));
		    new_open_list1.add(new_start_node1);        
		    search_path(new_open_list1, open2); /////このへん変更したyo!
		    return;
		}
	    }
	    parent_list.clear();
	    
	    search(n2,open2,0);
	}
    }/////↑ここまで
    
    public void search (Node n, ArrayList<Node> list, int a) { /////new関数
        Node v;
        int dist, n_gs;
	boolean flag;
	int x = 0;
        int y = 0;   
	
        for(int i=0;i<4;i++){
            if(i==0){
                x = n.pos[0] + 1;
                y = n.pos[1] + 0;
            }else if(i==1){
                x = n.pos[0] + -1;
                y = n.pos[1] + 0;
            }else if (i==2){
                x = n.pos[0] + 0;
                y = n.pos[1] + 1;
            }else if (i==3) {
                x = n.pos[0] + 0;
                y = n.pos[1] + -1;
            }else if (i==4){
                x = n.pos[0] + 1;
                y = n.pos[1] + 1;
            }else if (i==5){
                x = n.pos[0] + -1;
                y = n.pos[1] + 1;
            }else if (i==6){
                x = n.pos[0] + 1;
                y = n.pos[1] + -1;
            }else{
                x = n.pos[0] + -1;
                y = n.pos[1] + -1;
            }
            
            if (y <= 0 || y >= aStar.map_height ||
                x <= 0 || x >= aStar.map_width ||
                (aStar.aStarmap[y][x] == '0'||aStar.aStarmap[y][x] == 'X')) {
                continue;
            }

            if (a == 1) {
		n_gs = n.Sfs - n.Shs;
		flag = find(x,y,list);
		v = findN(x,y,list);
		dist = (int)(Math.pow((n.pos[0]-x),2) + Math.pow((n.pos[1]-y),2));
		
		if(flag){
		    if (v.Sfs > n_gs + v.Shs + dist){
			v.Sfs = n_gs + v.Shs + dist;
			v.Sparent_node = n;
		    }
		}else{
		    flag = find(x,y,close_list1);
		    v = findN(x,y,close_list1);
		    if(flag){
			if(v.Sfs > n_gs + v.Shs + dist){
			    v.Sfs = n_gs + v.Shs + dist;
			    v.Sparent_node = n;
			    list.add(v);
			    delete(close_list1,v);
			}
		    }else{
			v = new Node(x,y);
			v.Sfs = n_gs + v.Shs + dist;
			v.Sparent_node = n;
			list.add(v);
		    }
		}
            } else {
                n_gs = n.Gfs - n.Ghs;
		
                flag = find(x,y,list);
                v = findN(x,y,list);
                dist = (int)(Math.pow((n.pos[0]-x),2) + Math.pow((n.pos[1]-y),2));
                
                if(flag){
                    if (v.Gfs > n_gs + v.Ghs + dist){
			v.Gfs = n_gs + v.Ghs + dist;
			v.Gparent_node = n;
                    }
                }else{
                    flag = find(x,y,close_list2);
                    v = findN(x,y,close_list2);
                    if(flag){
			if(v.Gfs > n_gs + v.Ghs + dist){
			    v.Gfs = n_gs + v.Ghs + dist;
			    v.Gparent_node = n;
			    list.add(v);
			    delete(close_list2,v);
			}
                    }else{
			v = new Node(x,y);
			v.Gfs = n_gs + v.Ghs + dist;
			v.Gparent_node = n;
			list.add(v);
                    }
                }
            }
        }
	
    }
    
     //最短経路の候補の経路を保存する関数
    public void help_search_path1(){
    Node path1 = Send_node.Sparent_node; /////
    Node path2 = Gend_node.Gparent_node; /////
    Astar_Map m;

    // ルートとなるノードに印をつける
    while (true) {
        if(path1==null){/////このへん変更したyo!
            if (path2.Gparent_node == null) {
                break;
            }
            if (path2.Gparent_node != null) {
                path_list.add(path2);
                path2 = path2.Gparent_node;
            }
        }else if (path2==null){/////このへん変更したyo!
            if (path1.Sparent_node == null) {
                break; 
            }
            if (path1.Sparent_node != null) {
                path_list.add(path1);
                path1 = path1.Sparent_node;
            } 
        }else{
            if (path1.Sparent_node == null && path2.Gparent_node == null) {
                break; 
            }
            if (path1.Sparent_node != null) {
                path_list.add(path1);
                path1 = path1.Sparent_node;
            } 
            if (path2.Gparent_node != null) {
                path_list.add(path2);
                path2 = path2.Gparent_node;
            }
        }
    }
    
    //マップ情報を格納・保持
    if(cycle_num == 1){
	min_path = path_list.size();
	m = new Astar_Map(path_list);
	a_map = m;
    }else{
	if(min_path>=path_list.size()){
	    //min_pathより大きい時は格納しない
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
	//ArrayList<Astar_Map> arg = new ArrayList<Astar_Map>();
	int haba = 0;
	int max =0;
	Astar_Map m1 = a_map;
	Astar_Map m2 = a_map;
	int count =0;
	char[][] new_map = new char[aStar.map_height][aStar.map_width];

	while(true){
	    if(m1 ==null) break;
	    for(int i=0;i<m1.p_list.size();i++){
		haba += m1.p_list.get(i).michihaba;
	    }
	    michihaba_width.add(haba);
	    haba = 0;
	    m1 = m1.p_node;
	}

	
	max = michihaba_width.get(0);
	for(int i = 0;i<michihaba_width.size();i++){
	    if(max<michihaba_width.get(i)){
		max = michihaba_width.get(i);
        //count = michihaba_width.size()-i-2;
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
		new_map[i][j] = aStar.aStarmap[i][j];
	    }
	}	
	
	for(int i = 0;i<m2.p_list.size();i++){
	    new_map[m2.p_list.get(i).pos[1]][m2.p_list.get(i).pos[0]] = '+'; 
	}

	for (int i = 0; i < aStar.map_height; i++) {
	    for (int j = 0; j < aStar.map_width; j++) {
		 experiment2.map_result[i][j] = new_map[i][j];
	    }
	}

	/*ファイルへの書き出し
	try{
		File file = new File("./text5.txt");
  
		if (checkBeforeWritefile(file)){
		  FileWriter filewriter = new FileWriter(file);
		  for (int i = 0; i < aStar.map_height; i++) {
			for (int j = 0; j < aStar.map_width; j++) {
				String s = String.valueOf(new_map[i][j]);
				filewriter.write(s);
			}
			filewriter.write("\r\n");
		  }
		  filewriter.close();
		}else{
		  System.out.println("ファイルに書き込めません");
		}
	}catch(IOException e){
		System.out.println(e);
	}*/

	/*
	System.out.println("新しい道幅考慮したマップ:");
	for (int i = 0; i < aStar.map_height; i++) {
	String s = String.valueOf(new_map[i]);
	System.out.println(s);
	}*/
	}
	
	/*ファイルへの書き出し２
	private static boolean checkBeforeWritefile(File file){
		if (file.exists()){
		  if (file.isFile() && file.canWrite()){
			return true;
		  }
		}
	
		return false;
	}*/
    
    
}
