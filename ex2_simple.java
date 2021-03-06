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





public class ex2_simple extends JPanel{

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

   int x_s =154;   //ここ変えると座標変えられる！ただダイクストラは注意！

    int y_s = 144;

    int x_g =555;

    int y_g =508;




    ex2_simple (){

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

	research.name = "ナビ";

	//char button_flag;

	

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

	ex2_simple pr =new ex2_simple ();

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

    //Map chizu = new Map();

    public static char[][] map_result = new char[30][30];

				   

    

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

        //Node node;

        NodeList nodelist = new NodeList();

        map_result = nodelist.result;

    }

    

}



class Node{

    public int[] pos = new int[2];

    public int hs, fs;

    public Node parent_node;



    // コンストラクタ                                                                                                                                                                                                                                                         

    public Node (int x, int y) {

        pos[0] = x;

        pos[1] = y;

        hs = (int)(Math.pow(x-aStar.goal[0],2) + Math.pow(y-aStar.goal[1],2));

        fs = 0;

        parent_node = null;

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





class NodeList{

        static char[][] newMap = new char[aStar.map_height][aStar.map_width];

        //openリストとcloseリストを設定前半                                                                                                                                                                                                                                   

        ArrayList <Node> open_list = new ArrayList <Node> ();

        ArrayList <Node> close_list = new ArrayList <Node> ();

        Node start_node = new Node(aStar.start[0],aStar.start[1]);

        Node end_node;

        char[][] result = new char[aStar.map_height][aStar.map_width];



    NodeList(){

        System.out.println("search_path method is called");

        start_node.fs = start_node.hs;

        open_list.add(start_node);

        search_path();

        System.out.println("search_path method finished");

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

        num = list.indexOf(n);

        list.remove(num);

    }



    public void search_path(){

        Node n;

        Node v;//(2,1)                                                                                                                                                                                                                                                        

        int dist,n_gs;

        boolean flag;

        int x = 0 ;

        int y = 0;



        while(true){

            if(open_list ==null){

                System.out.println("There is no route until reaching a goal.");

                System.exit(1);

            }



            n = min(open_list);

            delete(open_list,n);

            close_list.add(n);



            if(Node.isGoal(n)){

                end_node = n;

                break;

            }

            n_gs = n.fs - n.hs;



            /*ノードnの移動可能方向のノードを調べる                                                                                                                                                                                                                           

              for v in ((1,0),(-1,0),(0,1),(0,-1)):*/

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

                }else{

                    x = n.pos[0] + 0;

                    y = n.pos[1] + -1;

                }



                /*マップが範囲外または壁(O)の場合はcontinue*/

                if (y <= 0 || y >= aStar.map_height ||

                    x <= 0 || x >= aStar.map_width ||

                    (aStar.aStarmap[y][x] == '0')) {

                    continue;

                }



				/*移動先のノードがOpen,Closeのどちらのリストに

		        格納されているか、または新規ノードなのかを調べる*/

                flag = find(x,y,open_list);

                v = findN(x,y,open_list);

                dist = (int)(Math.pow((n.pos[0]-x),2) + Math.pow((n.pos[1]-y),2));

		

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

                            open_list.add(v);

                            delete(close_list,v);

                        }

                    }else{

                		/*新規ノードならばOpenリストにノードに追加*/

		            	v = new Node(x,y);

                        v.fs = n_gs + v.hs + dist;

                        v.parent_node = n;

                        open_list.add(v);

                    }

                }

            }

        }

        Node path = end_node.parent_node;



        // endノードから親を辿っていくと、最短ルートを示す.                                                                                                                                                                                                                   

        for (int i = 0; i < aStar.map_height; i++) {

            for (int j = 0; j < aStar.map_width; j++) {

                newMap[i][j] = aStar.aStarmap[i][j];

            }

        }

        /*for (int i = 0; i < 30; i++) {

            for (int j = 0; j < 30; j++){

                String s = String.valueOf(newMap[i][j]);

                System.out.print(s);

            }

            System.out.println();

        }*/

        // ルートとなるノードに印をつける                                                                                                                                                                                                                                     

        while (true) {

            if (path.parent_node == null) break;

            newMap[path.pos[1]][path.pos[0]] = '+';

            path = path.parent_node;

        }



        for (int i = 0; i < aStar.map_height; i++) {

            for (int j = 0; j < aStar.map_width; j++){

                ex2_simple.map_result[i][j] = newMap[i][j];

		

            }

        }

    }

    

    public Node min(ArrayList<Node> open){

        Node minN = open.get(0);

        Node n;

        int min = minN.fs;



        for(int i = 0;i<open.size();i++){

            n = open.get(i);

            if(n.fs < min){

                minN = n;

                min = n.fs;

            }

        }

        return minN;

        }

}