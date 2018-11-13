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



public class P_astar_obs2 extends JPanel{

    Map map = new Map();

    human user = new human();

    barrier obs = new barrier();

    goal goal = new goal();

    int xl = map.xl;//maisuu  panel

    int yl = map.yl;//maisuu  panel

    int xn = 900/xl;//x no panel no nagasa

    int yn = 900/yl;//y no panel no nagasa

    int n = 3;//user komi

    ArrayList<human> human = new ArrayList <human>();

    ArrayList<Map> m = new ArrayList<Map>();

    ArrayList<aStar> as = new ArrayList <aStar>();



    

    //String map2[][] = new String[30][30];	



    

    P_astar_obs2 (){

	human.add(user);

	m.add(map);

	

	setOpaque(false);

	addMouseListener(new MouseAdapter() {

		int i = 0;

		int s_x, s_y, e_x, e_y;

		public void mouseClicked(MouseEvent e) {

		    System.out.println("click");

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

					human.add(new human(x_rnd*xn+xn/2 , y_rnd*xn+xn/2));

				 }
		    		
		    	aStar as ;
				 for (int i = 0;i<n ;i++){

					as = new aStar(m.get(i).map); 
				 	
				    	//astarが呼ばれるとmapが変わる
				    	//ここがおかしそう
				 	for(int a = 0;a < xl;a++){
			    			for(int b = 0;b < yl;b++){
			    				String s = String.valueOf(m.get(0).map[b][a]);//
			    				System.out.print(s);
			    			}
			    			 System.out.println();
			    		}
				    	
					m.get(i).map = as.map_result;
					 	
					as = null;
			    }
		    		/*for(int k = 0;k < n;k++){//
			    		for(int a = 0;a < xl;a++){
			    			for(int b = 0;b < yl;b++){
			    				String s = String.valueOf(m.get(k).map[b][a]);//
			    				System.out.print(s);
			    			}
			    			 System.out.println();
			    		}
		    			System.out.println();System.out.println();
		    		}//*/

			    i = 3;

		    	}else if(i == 2){

				    obs.x = e.getX();

				    obs.y = e.getY();
					
					for(int k = 0;k<n;k++){

						m.get(k).map[e.getX()/xn][e.getY()/yn] = 'X';

						m.get(k).map[e_x][e_y] = 'G';

					}
				    repaint();


					for(int k = 0; k<n;k++){
					
						for(int i=0; i<xl; i++) {

							for(int j=0; j<yl; j++) {

							    if(m.get(k).map[i][j] == '+' || m.get(k).map[i][j] == 'S') {

								map.map[i][j] = ' ';

							    }

							}

					    }
					}
				
				


					for (int k = 0;k<n;k++){

						m.get(k).map[human.get(k).x_now/xn][human.get(k).y_now/yn] = 'S';

					    

						as.set(k,  new aStar(m.get(k).map)) ;

						m.get(k).map = as.get(k).map_result;
					}

			    /*シミュレーション停止もう一度mapの送受信必要*/

			//

		    }else if(i == 3){
		    	for (int k = 0; k < human.size();k++){
		    		System.out.println(human.get(k).x_now + " ,"+ human.get(k).y_now);
		    		
		    	}
		    }else{}	

		    }else{System.out.println("Please select road");}

		}

	    });

	

	new javax.swing.Timer(10000, new ActionListener(){

		public void actionPerformed(ActionEvent evt){	

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

					   			 }	 //  

							}	

				   		 }

					}

			    	}

				}else{

				    human.get(t).movehuman(human.get(t).x_now + human.get(t).vx ,human.get(t).y_now + human.get(t).vy);
					if(t == 0){

				    m.get(t).p.setxy(human.get(t).x_now+human.get(t).vx ,human.get(t).y_now + human.get(t).vy);
					}

				    repaint();

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

    }

    

    public static void main(String[] args) {

    	JFrame fr = new JFrame("map");

	fr.setSize(900, 900);

    	fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	fr.getContentPane().setBackground(new Color(255, 255, 255));

        P_astar_obs2 pr =new P_astar_obs2();

        pr.setOpaque(false);

	fr.add(pr);

	fr.setVisible(true);	

    }

}



class Map {

    public char map[][];

    int xl = 30, yl = 30;

    int xn = 900/xl;

    int yn = 900/yl;

    path p = new path();

    

    public  boolean road(int x, int y){

    	int xm = x/xn;

    	int ym = y/yn;

    	if(map[xm][ym] == ' ' || map[xm][ym] == '+'){return true;}else{return false;}

    }

    

    public Map(){

        this.map = new char[xl][yl];

        

	for (int x=0; x<xl; x++) {

	    for (int y=0; y<yl; y++) {

		map[x][y] = '0';

	    }

	}

	

    	for(int x=0; x<xl; x++) {

	    for(int y=0; y<yl; y++) { 

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

    }

    

    

    public void draw(Graphics g) {

	for (int x=0; x<xl; x++) {

	    for (int y=0; y<yl; y++) {

		if(map[x][y] == 'S' || map[x][y] == 'G' || map[x][y] == ' ' || map[x][y] == '+' ||map[x][y] == 'X' ) {

	           g.setColor(Color.white);

		    g.fillRect(x*xn, yn*30, xn, yn);

		    g.setColor(Color.black);

		    if(map[x][y] =='X'){g.drawLine(x*xn, y*yn, (x+1)*xn, (y+1)*yn);}

		}

		if(map[x][y] == '0') {

		    g.setColor(new Color(200,200,200));

		    g.fillRect(x*xn, y*yn, xn, yn);

		}

	    }

	}

	p.draw(g);

    }

    

    class path {

	int xm,ym,x1,y1;

	int xn = 900/xl;

	int yn = 900/xn;



	public void setxy(int x,int y){x1 = x; y1 = y;xm =x/xn;ym = y/yn;}

	

	public void draw (Graphics g){

	    g.setColor(Color.blue);

	    if(map[xm][ym]=='+'||map[xm][ym] == 'G'){ g.drawLine(x1,y1,xn/2+xn*(xm),yn/2+yn*(ym));}

	    int xbf = 2; int ybf = 2;

	    int n = 0;

	    while(true){

		int i = 0;

				

		for(int x= -1; x < 2 ; x++ ){

		    if (xm + x > -1&& xm + x < xl){

			for(int y = -1; y < 2; y++){

			    if(x ==0||y == 0){ //

				if (ym + y > -1 &&  ym + y < yl){

                    

				    if((map[xm+x][ym+y] == '+'||map[xm+x][ym+y] == 'G') &&!(x==0 && y == 0) && !(x==xbf && y==ybf)){

					if(n == 0){

					    if(map[xm][ym]=='+'){ 

						g.drawLine(xn/2+xm*xn,yn/2+ym*yn,xn/2+xn*(xm+x),yn/2+yn*(ym+y));

					    }else{

						g.drawLine(x1,y1,xn/2+xn*(xm+x),yn/2+yn*(ym+y));

					    }

					}else{

					    g.drawLine(xn/2+xm*xn,yn/2+ym*yn,xn/2+xn*(xm+x),yn/2+yn*(ym+y));

					}

					xm = xm+x; ym = ym+y; xbf = -x; ybf = -y;

					i = 1;

					n++;

					break;

				    }

				    	

				}

				if(i == 1){break;}

				if(x == 1){i = 2; break;}

			    }//

			}

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





class barrier {

    int x,y,xm,ym;

    public void draw(Graphics g) {

	g.setColor(Color.black);

	xm = x/30;

	ym = y/30;

	g.drawLine(xm*30, ym*30, (xm+1)*30, (ym+1)*30);

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

                    (aStar.aStarmap[y][x] == '0' || aStar.aStarmap[y][x] == 'X')) {

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



        for (int i = 0; i < 30; i++) {

            for (int j = 0; j < 30; j++){

                String s = String.valueOf(newMap[j][i]);//

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