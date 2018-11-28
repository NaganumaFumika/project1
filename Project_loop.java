// アルゴリズム班とシミュ班合体済み
// 無限ループあり
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
    Map.barrier obs = map.new barrier();
    Map.goal goal = map.new goal();
    int xl = map.lx;//maisuu  panel
    int yl = map.ly;//maisuu  panel
    int xn = 900/xl;//x no panel no nagasa
    int yn = 900/yl;//y no panel no nagasa
    int n = 3;//user komi
	ArrayList<Map.human> human = new ArrayList <Map.human>();
    ArrayList<Map> m = new ArrayList<Map>();
    ArrayList<aStar> as = new ArrayList <aStar>();
    //String map2[][] = new String[30][30];

    Project2 (){
	human.add(user);
	m.add(map);
	setOpaque(false);

	addMouseListener(new MouseAdapter() {
		int i = 0;
		int s_x, s_y, e_x, e_y;
		boolean click_map = true; 

		public void mouseClicked(MouseEvent e) {
		    System.out.println("click");
		    if(click_map == true){
			if(map.road(e.getX(),e.getY())){
			    if(i == 0){    
					user.movehuman(e.getX()/xn*xn+xn/2,e.getY()/yn*yn+yn/2);
					map.p.setxy(e.getX()/xn*xn+xn/2,e.getY()/yn*yn+yn/2);
					map.map[e.getX()/xn][e.getY()/yn] = 'S';
					s_x = e.getX()/xn;
					s_y = e.getY()/yn;
					repaint();
					i = 1;
			    }else if( i == 1 ){
					user.x_end = e.getX();
					user.y_end = e.getY();
					goal.x = e.getX();
					goal.y = e.getY();
					m.get(0).map[e.getX()/xn][e.getY()/yn] = 'G';
					map.map[s_x][s_y] = 'S';
					e_x = e.getX()/xn;
					e_y = e.getY()/yn;

					//make human & Map
					Random rnd = new Random();
					int x_rnd = 0;int y_rnd = 0;;
					for(int i = 1;i < n ;i++){
					    m.add(new Map());
					    m.get(i).map[e.getX()/xn][e.getY()/yn] = 'G';
				    	while(true){
							x_rnd = rnd.nextInt(xl);
							y_rnd = rnd.nextInt(yl);
							if(m.get(i).road(x_rnd*xn , y_rnd*yn) == true ){
							    m.get(i).map[x_rnd][y_rnd] ='S';
							    break;
							}
				    	}
				    	human.add(m.get(i).new human(x_rnd*xn+xn/2 , y_rnd*xn+xn/2));
					}
				
					aStar as ;
					for (int i = 0;i<n ;i++){
					    as = new aStar(m.get(i).map); 
					    for(int a = 0;a < xl;a++){
							for(int b = 0;b < yl;b++){
							    m.get(i).map[a][b] = as.map_result[a][b];
							}
					    }
					    as = null;
					}
					
					click_map = false;
					i = 2;//changeF
					
			    }else if(i == 2){
					obs.x = e.getX();
					obs.y = e.getY();
					/*changeF*/
					for(int a=0; a<900; a++) {
					for(int b=0; b<900; b++) {
						if(a >= e.getX()-(obs.bl_x/2) && a <= e.getX()+(obs.bl_x/2) && b >= e.getY()-(obs.bl_y/2) && b <= e.getY()+(obs.bl_y/2)) {
					    	// m.get(0).map[a/xn][b/yn] = 'X';
					   	for(int k = 0;k<n;k++){
							m.get(k).map[a/xn][b/yn] = 'X';
							m.get(k).map[e_x][e_y] = 'G';
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
					    if(m.get(k).map[i][j] == '+' || m.get(k).map[i][j] == 'S') {
						m.get(k).map[i][j] = ' ';
					    }
					}
				    }
				}
				aStar as;
				for (int k = 0;k<n;k++){
				    m.get(k).map[human.get(k).x_now/xn][human.get(k).y_now/yn] = 'S';
				    as =  new aStar(m.get(k).map) ;
				    for(int a = 0;a < xl;a++){
					for(int b = 0;b < yl;b++){
					    m.get(k).map[a][b] = as.map_result[a][b];
					}
				    }
				}
				i = 2; //changeF
				/*シミュレーション停止もう一度mapの送受信必要*/
				//
				click_map = false;
			    }else if(i == 3){
					for (int k = 0; k < human.size();k++){
					    System.out.println(human.get(k).x_now + " ,"+ human.get(k).y_now);
					}
			    }else if(i == 4){
					click_map = false;
			    }else{}
			}else{System.out.println("Please select road");
			    //change niimi
			    for(int k = 0;k<n;k++){
					if(m.get(k).build(e.getX(),e.getY())){
					    m.get(k).map[e.getX()/xn][e.getY()/yn] = 'F';
					}
			    }
			    click_map = false;
			}
			
		    }
		    else{//フォームの処理
			click_map = true;
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
			    m.get(t).map[xm][ym] = ' ';
			    for(int x= -1; x < 2 ; x++ ){
				if (xm + x > -1&& xm + x < xl){
				    for(int y = -1; y < 2; y++){
					if (ym + y > -1 &&  ym + y < yl){
					    if(x == 0 || y == 0){ //
						if(m.get(t).map[xm+x][ym+y] == '+'||m.get(t).map[xm+x][ym+y] == 'G' ){
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
					    } //  
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

    

    public void paintComponent(Graphics g){
	map.draw(g);
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
	if((obs.x != 0) && (obs.y != 0)) {
	    obs.draw(g);
	}
	/*changeF*/
    }

    public static void main(String[] args) {
	JFrame fr = new JFrame("map");
	fr.setSize(900, 900);
	fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	fr.getContentPane().setBackground(new Color(255, 255, 255));
        Project2 pr =new Project2();
        pr.setOpaque(false);
	fr.add(pr);
	fr.setVisible(true);
    }
}


class Map {
    public static int lx = 900, ly = 900;
    public char map[][];
    path p = new path();

    //追加部分 
    public static int pix_x = 900/lx;
    public static int pix_y = 900/ly;

    int x_clixked,y_clicked;

    /*changeF*/
    private BufferedImage barrierImg;
    /*changeF*/

    public boolean road(int x, int y){
        int xm = x/pix_x;
        int ym = y/pix_y;
        if(map[xm][ym] == ' '|| map[xm][ym] == '+'){return true;}else{return false;}
    }
    
    //change niimi
    public boolean build(int x, int y){
        int xm = x/pix_x;
        int ym = y/pix_y;
        if(map[xm][ym] == '0' ){return true;}else{return false;}
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
		   if(map[x][y] == 'F'){ f_x.add(x);f_y.add(y);}
	       }
	   }
	       
	   for(int i =0;i < f_x.size();i++){
	       for(int x = -1*yabakyori;x < yabakyori; x++){
		   for(int y = -1*yabakyori; y < yabakyori; y++){
		       if (f_x.get(i) + x > -1&& f_x.get(i) + x < lx){
			   if (f_y.get(i) + y > -1 &&  f_y.get(i) + y < ly){
			       if(map[f_x.get(i)+x][f_y.get(i)+y]==' '){
				   map[f_x.get(i)+x][f_y.get(i)+y]='P';
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
			       if(map[f_x.get(i)+x][f_y.get(i)+y]=='0'){
				   map[f_x.get(i)+x][f_y.get(i)+y]='F';
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
		this.map = new char[lx][ly];
		for (int x=0; x<lx; x++) {
	    	for (int y=0; y<ly; y++) {
			map[x][y] = '0';
		    }
		}		
	
	
	for(int x=0; x<lx; x++) {
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
	}
    }
    public void draw(Graphics g) {
	
        for (int x=0; x<lx; x++) {
            for (int y=0; y<ly; y++) {
                if(map[x][y] == 'S' || map[x][y] == 'G' || map[x][y] == ' ' || map[x][y] == '+' ||map[x][y] == 'X' ) {
                    g.setColor(Color.white);
                    g.fillRect(x*pix_x, y*pix_y, pix_x, pix_y);
		    /*changeF*/
                    //g.setColor(Color.black);
                    if(map[x][y] =='X'){
                        //g.drawLine(x*pix_x, y*pix_y, (x+1)*pix_x, (y+1)*pix_y);
                        //g.drawLine((x+1)*pix_x, y*pix_y, x*pix_x, (y+1)*pix_y);
			g.drawImage(barrierImg, x-10, y-10, 20, 20, null);
		    }
		    /*changeF*/
                }
                if(map[x][y] == '0') {
                    g.setColor(new Color(200,200,200));
                    g.fillRect(x*pix_x, y*pix_y, pix_x, pix_y);
                }
		//change niimi
		if(map[x][y] == 'F'){
		    g.setColor(Color.red);
		    g.fillRect(x*pix_x, y*pix_y, pix_x, pix_y);
		}
		    if(map[x][y] == 'P'){
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
                    if(map[i][j] == '+' || map[i][j] == 'G') {
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
    
    /*class barrier {
	int x,y,xm,ym;
	public void draw(Graphics g) {
	    g.setColor(Color.black);
	    xm = x/30;
	    ym = y/30;
	    g.drawLine(xm*30, ym*30, (xm+1)*30, (ym+1)*30);
	}
	}*/
    /*changeF*/
}

class aStar{
    public  static int map_width,map_height;
    public  static char[][] aStarmap;
    public  static int[] start = new int[2];
    public  static int[] goal = new int[2];
    public  static char[][] map_result = new char[30][30];

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
        map_result = nodelist.result;
    }
}

class Node{
    public int[] pos = new int[2];
    public int hs, fs;
	public Node parent_node;
	public int michihaba;
	int count1 = 1;
	int count2 = 1;
    
    // コンストラクタ
    public Node (int x, int y) {
	pos[0] = x;
	pos[1] = y;
	hs = (int)(Math.pow(x-aStar.goal[0],2) + Math.pow(y-aStar.goal[1],2));
	fs = 0;
	parent_node = null;

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

	// ゴールかどうか確認するメソッド
    public static boolean isGoal (Node n) {
	if(n.pos[0] == aStar.goal[0] && n.pos[1] == aStar.goal[1]){
	    return true;
	}else{
		return false;
	}
    }
}

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
    static char[][] newMap = new char[aStar.map_height][aStar.map_width];
    //openリストとcloseリストを設定前半
    ArrayList <Node> open_list = new ArrayList <Node> ();
    ArrayList <Node> close_list = new ArrayList <Node> ();
    Node start_node = new Node(aStar.start[0],aStar.start[1]);
	Node end_node;
	ArrayList <Node> end_list = new ArrayList <Node> (); 
    ArrayList <Integer> sum_michihaba  = new ArrayList <Integer> ();  
    ArrayList <Node> equal_list = new ArrayList <Node> ();
    ArrayList <Integer> ngs_list = new ArrayList <Integer> (); //引数リス
    ArrayList <Node> path_list = new ArrayList <Node> ();
    Astar_Map a_map; 
    int cycle_num = 1;
    char[][] result = new char[aStar.map_height][aStar.map_width];

    NodeList(){
        start_node.fs = start_node.hs;
		open_list.add(start_node);
		search_path(open_list);
		//result = result_map;
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
    
    public void search_path(ArrayList<Node> open){
		Node n;
		Node v;//(2,1)
		Node new_start_node;
		ArrayList <Node> new_open_list = new ArrayList <Node> (); //引数リスト
		int dist,n_gs;
		boolean flag;
		int x = 0 ;
		int y = 0;
		//boolean naname = false;
		int sum_width = 0;
				
	
		while(true){
			if(open.size() == 0){
			System.out.println("There is no route until reaching a goal.");
				  System.exit(1);
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
					//System.out.println("探索終了!");
					help_search_path2();
					System.exit(1);
				}else{
					new_start_node = equal_list.get(equal_list.size()-1);
					close_list_change(new_start_node);
					delete(equal_list,equal_list.get(equal_list.size()-1));
					new_open_list.add(new_start_node);  		       
					search_path(new_open_list);		    
				}
			}
			
			n_gs = n.fs - n.hs; 
			
			/*ノードnの移動可能方向のノードを調べる
			  for v in ((1,0),(-1,0),(0,1),(0,-1)):*/
			for(int i=0;i<4;i++){
			if(i==0){
				x = n.pos[0] + 1;
				y = n.pos[1] + 0;
				// naname = false;
			}else if(i==1){
				x = n.pos[0] + -1;
				y = n.pos[1] + 0;
				// naname = false;
			}else if (i==2){
				x = n.pos[0] + 0;
				y = n.pos[1] + 1;
				// naname = false;
			}else{
				x = n.pos[0] + 0;
				y = n.pos[1] + -1;
			   // naname = false;
			}
			
			/*マップが範囲外または壁(0)の場合はcontinue*/
			if (y <= 0 || y >= aStar.map_height ||
				x <= 0 || x >= aStar.map_width ||
				(aStar.aStarmap[y][x] == '0')) {
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
		
	 
	public void help_search_path1(){
		Node path = end_node.parent_node;
		Astar_Map m;
	
		// ルートとなるノードに印をつける
		while (true) {
			if (path.parent_node == null) break;
				path_list.add(path);
				path = path.parent_node;
			}
	
		/*候補マップの表示
		char[][] new_map = new char[aStar.map_height][aStar.map_width];
		for (int i = 0; i < aStar.map_height; i++) {
			for (int j = 0; j < aStar.map_width; j++) {
			new_map[i][j] = aStar.aStarmap[i][j];
			}
		}			
		for(int i = 0;i<path_list.size();i++){
			new_map[path_list.get(i).pos[1]][path_list.get(i).pos[0]] = '+';
		}
		System.out.println("候補マップ:");
		for (int i = 0; i < aStar.map_height; i++) {
			String s = String.valueOf(new_map[i]);
			System.out.println(s);
		}*/

		//マップ情報を格納・保持
		m = new Astar_Map(path_list);
		if(cycle_num == 1){
			a_map = m;
		}else{
			m.p_node = a_map;
			a_map = m;
		}
		path_list.clear();  
	}
	
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

	//new!
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
	
	//new!
	public void help_search_path2(){
		ArrayList<Integer> michihaba_width = new ArrayList<Integer>();
		ArrayList<Astar_Map> arg = new ArrayList<Astar_Map>();
		int haba = 0;
		int max =0;
		Astar_Map m1 = a_map;
		Astar_Map m2 = a_map;
		int count =0;
		//char[][] result_map = new char[aStar.map_height][aStar.map_width];

		while(true){
			if(m1 ==null) break;
			for(int i=0;i<a_map.p_list.size();i++){
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
		
		for(int i = 0;i<m2.p_list.size();i++){
			result[m2.p_list.get(i).pos[1]][m2.p_list.get(i).pos[0]] = '+';
		}
		/*
		System.out.println("新しい道幅考慮したマップ:");
		for (int i = 0; i < aStar.map_height; i++) {
			String s = String.valueOf(new_map[i]);
			System.out.println(s);
		}	*/
		//return new_map;
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
				ngs_list.add(ngs);
			}
			}
		}
		return minN;
	}
}