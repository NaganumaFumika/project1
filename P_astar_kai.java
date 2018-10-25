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

public class P_astar extends JPanel{
    Map map = new Map();
    human user = new human();
    //String map2[][] = new String[30][30];	
    
    P_astar(){
	setOpaque(false);
	addMouseListener(new MouseAdapter() {
		int i = 0;
		int s_x, s_y;
		public void mouseClicked(MouseEvent e) {
		    if(map.road(e.getX(),e.getY())){
		    	if(i == 0){			    
    			    user.movehuman(e.getX()/30*30+15,e.getY()/30*30+15);
	    		    map.p.setxy(e.getX()/30*30+15,e.getY()/30*30+15);
		    	    map.map[e.getX()/30][e.getY()/30] = 'S';
			        s_x = e.getX()/30;
			        s_y = e.getY()/30;
    			    repaint();
	    		    i = 1;
		    	}else{
		    	    user.x_end = e.getX();
			        user.y_end = e.getY();
			        map.map[e.getX()/30][e.getY()/30] = 'G';
			        map.map[s_x][s_y] = 'S';
                    aStar astar = new aStar(map.map); 
                    /*simyu watasu*/ 
                    /*simyu uketori*/
			        map.map = astar.map_result;
                    i = 0;
                }
		    }else{System.out.println("Plese select road");}
		}
	    });
	
	new javax.swing.Timer(100, new ActionListener(){
		public void actionPerformed(ActionEvent evt){		    
		    int xm = user.x_now/30;int ym = user.y_now/30;
		    if((user.x_now-15)%30 == 0 && (user.y_now-15)%30 == 0){
	    		int i = 0;
		    	map.map[xm][ym] = ' ';
    			for(int x= -1; x < 2 ; x++ ){
	    		    if (xm + x > -1&& xm + x < 30){
		    	    	for(int y = -1; y < 2; y++){
			    	        if (ym + y > -1 &&  ym + y < 30){
    	        				if(map.map[xm+x][ym+y] == '+'||map.map[xm+x][ym+y] == 'G' ){
	    		        		    user.vx = x; user.vy = y; i = 1;
					                user.movehuman(user.x_now+user.vx ,user.y_now + user.vy);
					                map.p.setxy(user.x_now+user.vx ,user.y_now + user.vy);
            					    repaint();
			            		    break;
					            }
                                if(i == 0&&y==1&&x==1){
                                    user.vx = 0;user.vy = 0; break;
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
    public char map[][];
    private int xx = 30, yy = 30;
    path p = new path();
    
    public  boolean road(int x, int y){
    	int xm = x/30;
    	int ym = y/30;
    	if(map[xm][ym] == ' '){return true;}else{return false;}
    }
    
    public Map(){
        this.map = new char[xx][yy];
        
	    for (int x=0; x<xx; x++) {
	        for (int y=0; y<yy; y++) {
		        map[x][y] = '0';
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
		if(map[x][y] == 'S' || map[x][y] == 'G' || map[x][y] == ' ' || map[x][y] == '+') {
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
        Node node;
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
        result = search_path();
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

        public char[][] search_path(){
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
            for(int i=0;i<8;i++){
                if(i==0){
                    x = n.pos[0] + 1;
                    y = n.pos[1] + 0;
                }else if(i==1){
                    x = n.pos[0] + -1;
                    y = n.pos[1] + 0;
                }else if (i==2){
                    x = n.pos[0] + 0;
                    y = n.pos[1] + 1;
                }else if(i==3){
                    x = n.pos[0] + 0;
                    y = n.pos[1] + -1;
                }else if(i==4){
                    x = n.pos[0] + 1;
                    y = n.pos[1] + 1;
                }else if(i==5){
                    x = n.pos[0] + 1;
                    y = n.pos[1] + -1;
                }else if(i==6){
                    x = n.pos[0] + -1;
                    y = n.pos[1] + 1;
                }else{
                    x = n.pos[0] + -1;
                    y = n.pos[1] + -1;
                }

                /*マップが範囲外または壁(O)の場合はcontinue*/
                if (y <= 0 || y >= aStar.map_height ||
                    x <= 0 || x >= aStar.map_width ||
                    (aStar.aStarmap[y][x] == 'O')) {
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

        // ルートとなるノードに印をつける                                                                                                                                                                                                                                     
        while (true) {
            if (path.parent_node == null) break;
            newMap[path.pos[1]][path.pos[0]] = '+';
            path = path.parent_node;
        }

        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++){
                String s = String.valueOf(newMap[i][j]);
                System.out.print(s);
            }
            System.out.println();
        }
        return newMap;
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


