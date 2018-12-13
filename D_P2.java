import java.util.Scanner;
import java.io.*;
import java.lang.Math;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Random;
import java.util.ArrayList;


public class D_P2 extends JPanel{
    Map2 map = new Map2();
    human2 user = new human2();
    Map2.goal2 goal = map.new goal2();
    int xl = map.lx;
    int yl = map.ly;
    int xn = 900/xl;
    int yn = 900/yl;
    int n=1;
    ArrayList<Map2> m = new ArrayList<Map2>();
    //    ArrayList<Dijkstra2> di = new ArrayList <Dijkstra2>();
    Dijkstra2 di;
    ArrayList<human2> human2 = new ArrayList <human2>();
    public static char[][] map_result = new char[900][900];
    ArrayList<Integer> x = new ArrayList<Integer>();
    ArrayList<Integer> y = new ArrayList<Integer>();

    D_P2() {
	
	human2.add(user);
	//	di.add(new Dijkstra2());
	m.add(map);
 	di = new Dijkstra2();

	setOpaque(false);
	int x_s = 272;
	int y_s= 142;
	map.map[y_s/yn][x_s/xn] = 'S';
	di.start_x = x_s;
	di.start_y = y_s;
	int x_g = 346;
	int y_g = 535;
	goal.x = x_g;
	goal.y = y_g;
	di.end_x = x_g;
	di.end_y = y_g;

	//  	di = new Dijkstra2();
	x = di.getList_x();
	y = di.getList_y();
	//ここで呼び出し
	//di.get(0).関数(xのリストを返す)
	//xっていうarraylistに代入
	//di.get(0).関数(yのリストを返す)
	////xっていうarraylistに代入
	/*x.add(0);
	x.add(600);
	x.add(200);
	y.add(300);
	y.add(500);
	y.add(800);*/
	
	
	
	user.x_now = x.get(0);
	user.y_now = y.get(0);
	
	new javax.swing.Timer(1, new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		    
		    move(x,y,user);
		    repaint();
		}
	    }).start(); 
    }
    
    


    public void move(ArrayList<Integer> a,ArrayList<Integer> b, human2 h){
	int x_p = x.get(0);
	int y_p = y.get(0);
	
	int x_st,y_st ;
	if(x.size()>0){
	    if(h.x_now == x_p && h.y_now == y_p){
		x.remove(0);
		y.remove(0);
		x_p = x.get(0);
		y_p = y.get(0);
		
	    }else{
		x_st = x_p-h.x_now;
		y_st = y_p-h.y_now;
		if(x_st!=0){
		    x_st = x_st/Math.abs(x_st);
		}
		if(y_st != 0){
		    y_st = y_st/Math.abs(y_st);
		    
		}
		
		h.movehuman(h.x_now + x_st,h.y_now + y_st);
	    }
	}
    }
	
    
    @Override
	public void paintComponent(Graphics g) {
	g.drawImage(map.mapImg2,0,0,900,900,this);
	map.draw(g);
	
	if((user.x_now!=0) && (user.y_now!=0)) {
	    goal.draw(g);
	}
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
	fr.setSize(900,900);
	fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	fr.getContentPane().setBackground(new Color(255,255,255));
	D_P2 dp = new D_P2();
	dp.setOpaque(false);
	fr.add(dp);
	fr.setVisible(true);
    }
}

class human2 {
    
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

class Map2 extends JPanel {
    public static int lx = 900, ly = 900;
    public char map[][];

    public static int pix_x = 900/lx;
    public static int pix_y = 900/ly;
    private BufferedImage barrierImg;
    public BufferedImage mapImg2;
    public int mapColor[][];
    human2 user = new human2();

    public boolean road(int x, int y) {
	int xm = x/pix_x;
	int ym = y/pix_y;
	if(map[ym][xm] == ' ' || map[ym][xm] == '+') {
	    return true;
	} else {
	    return false;
	}
    }

    public boolean build(int x, int y) {
	int xm = x/pix_x;
	int ym = y/pix_y;
	if(map[ym][xm] == '0') {
	    return true;
	} else {
	    return false;
	}
    }

    public Map2() {
	this.map = new char[ly][lx];
	for(int x=0; x<lx; x++) {
	    for(int y=0; y<ly; y++) {
		map[y][x] = '0';
	    }
	}
	
	try {
	    File mapFile = new File("./map5.png");
	    BufferedImage mapImg = ImageIO.read(new File("./map5.png"));
	    mapImg2 = ImageIO.read(mapFile);
	    
	    for(int i=0; i<lx; i++) {
		for(int j=0; j<ly; j++) {
		    Color color = new Color(mapImg.getRGB(i, j));
		    if(color.getRed() >= 253 && color.getGreen() >= 253 && color.getBlue() >= 253) {
			map[j][i] = ' ';
		    }
		}
	    }
	} catch (IOException e) {
	    System.out.println("Failed to read file");
	}
    }

    public void draw(Graphics g) {
	for(int x=0; x<lx; x++) {
	    for(int y=0; y<ly; y++) {
		//draw fire
	    }
	}
    }

  

    class goal2 {
	int x=0;
	int y=0;
	int xm = 0;
	int ym = 0;
	public void draw(Graphics g) {
	    g.setColor(Color.green);
	    xm = x/30;
	    ym = y/30;
	    g.fillOval(xm*30, ym*30, 30, 30);
	}
    }   
}



class Dijkstra2 {
    public static ArrayList<Integer> xzahyou = new ArrayList<Integer>();
    public static ArrayList<Integer> yzahyou = new ArrayList<Integer>();
    public static ArrayList<Integer> keiyu = new ArrayList<Integer>();
    public static ArrayList<Integer> keiyu_x = new ArrayList<Integer>(); //経由地のx座標         
    public static ArrayList<Integer> keiyu_y = new ArrayList<Integer>();
    public static int[] list_x = new int[150];
    public static int[] list_y = new int[150];
    public int start_x, start_y, end_x, end_y;
    
    //public static void main(String[] args) {    
    
    public Dijkstra2() {	
	try {
	    File route = new File("./route.data");
	    File node = new File("./node.data");
	    
	    if(!route.exists() || !node.exists()) {
		System.out.println("file is not exit");
		return;
	    }
	    
	    FileReader fileReader = new FileReader(node);
	    BufferedReader bufferedReader = new BufferedReader(fileReader);
	    
	    String data;
	    String no;
	    String xx;
	    String yy;
	    int xxx;
	    int yyy;
	    int i=0;
	    int j=0;
	    int k=0;
	    
	    while((data = bufferedReader.readLine()) != null) {
		if(i==0) {
		    no = data;
		    //System.out.println("no." + no);
		    i=1;
		} else if(i==1) {
		    xx = data;
		    xxx = Integer.valueOf(xx);
		    list_x[j] = xxx;
		    xzahyou.add(xxx);
		    //System.out.println("x: " + list_x[j]);
		    j++;
		    i=2;
		} else if(i==2) {
		    yy = data;
		    yyy = Integer.valueOf(yy);
		    list_y[k] = yyy;
		    yzahyou.add(yyy);
		    //System.out.println("y: " + list_y[k]);
		    k++;
		    i=0;
		}
	    }

	    bufferedReader.close();

	} catch(IOException e) {
            System.out.println("Failed to read file");
	}
	

	
	Scanner sc = new Scanner(System.in); // 標準入力から読む
		
	int nTown = 0; // ノードの数 = 105
	int nRoute = 0; // 道路の数 = 292
	int[][] nodeMap = new int[0][0]; // 都市の接続関係のマップ
	while(true) {
	    nTown = sc.nextInt(); // ノードの数 = 105
	    nRoute = sc.nextInt(); // 道路の数 = 292
	    nodeMap = new int[nTown][nTown]; // 都市の接続関係のマップ
	    if(nRoute == 292) {
                break;
	    }
	}
	
	
	//隣接関係のnodeMapを初期化
	//change
	for (int i=0; i<104; i++) {
	    for (int j=0; j<104; j++) {
		if(i == j) {
		    nodeMap[i][j] = 0;
		}else{
		    nodeMap[i][j] = -1;
		}
	    }
	}

		
	//自分、隣接ノード、重み（距離）をnodeMapに格納
	for (int i=0; i<nRoute; i++) {
	    int from = sc.nextInt();
	    int to = sc.nextInt();
	    int len = length(from, to);

	    nodeMap[from][to] = nodeMap[to][from] = len;
	}
	

	//chenge	
	int start = 0; // 出発地点
	int goal = 103;	// 到着地点


	//ここまで.dataから読み込み＝シミュレーション側のmapからnodeMapを作る作業
	

	int[] distance = new int[nTown];	// 各ノードまでの最短距離
	int[] via = new int[nTown]; //経由ノードの番号
	/*public static ArrayList<Integer> keiyu = new ArrayList<Integer>();
	public static ArrayList<Integer> keiyu_x = new ArrayList<Integer>(); //経由地のx座標
	public static ArrayList<Integer> keiyu_y = new ArrayList<Integer>();*/


	//	System.out.println("ここ");	
	dijkstra(nodeMap, start, distance, via);	

	if (distance[goal] == Integer.MAX_VALUE) {
	    System.out.println("There is no route");
	} else {
	    System.out.println("distance="+distance[goal]); //距離
	    for(int i=0; i<keiyu.size()-1; i++) {
		System.out.print(keiyu.get(i) + " "); //経由地
	    }
	    System.out.println(start); //スタート
	}
    }

       
	//nodeMapにはnodeMap[自分][隣接ノード]=距離(重み)が格納されている
	//srcはスタート地点
	//各ノードまでのdistanceは最短距離
    
    public static void dijkstra(int[][] nodeMap,int start,int[] distance, int[] via) {
	int nTown2 = distance.length; //ノードの数
	boolean[] fixed = new boolean[nTown2]; // 最短距離が確定→true、未確定→false、各ノードに対して確認
	
	
	//各ノードの最短距離を初期化
	for (int i=0; i<nTown2; i++) {
	    distance[i] = Integer.MAX_VALUE; // 初期値は無限
	    fixed[i] = false;	// 最短距離はまだ確定していない
	    via[i] = -1;
	}
	
	distance[start] = 0;	// 出発地点までの距離を0とする
	
	
	
	while (true) {
	    // 未確定の中で最も近い都市を求める
	    int marked = minIndex(distance,fixed);
	    
	    if (marked < 0) break; // 全都市が確定した場合
	    if (distance[marked]==Integer.MAX_VALUE) break; // 非連結グラフ
	    
	    fixed[marked] = true; // その都市までの最短距離は確定となる
	    
	    for (int j=0; j<nTown2; j++) { // 隣の都市(i)について
		if (nodeMap[marked][j]>0 && fixed[j]==false) { // 未確定ならば
		    // 旗の都市を経由した距離を求める
		    int newDistance = distance[marked]+nodeMap[marked][j];
		    if (newDistance < distance[j]) {
			// 今までの距離よりも小さければ、それを覚える
			distance[j] = newDistance;
			//経由地書き換え
			via[j] = marked;
		    }
		}
	    }
	}

	//chenge	


	for(int i = nTown2-1; i>=0; i--) {
	    keiyu.add((int)via[i]);
	}

	for(int i=0; i<keiyu.size()-1; i++) {
	    keiyu_x.add( list_x[keiyu.get(i)] );
	    keiyu_y.add( list_y[keiyu.get(i)] );
	}
    }

    public ArrayList<Integer> getList_x() {
	return keiyu_x;
    }

    public ArrayList<Integer> getList_y() {
	return keiyu_y;
    }
    
    
    // まだ距離が確定していないノードの中で、もっとも近いノードを探す
    static int minIndex(int[] distance,boolean[] fixed) {
	int idx = 0;
	
	//距離が未確定のノードの一つを探す
	for (; idx<fixed.length; idx++) {
	    if (fixed[idx] == false) {
		break;
	    }
	}
	
	//すべてのノードを見きって距離が未確定の都市が存在しなければ-1を返す	
	if (idx == fixed.length) {
	    return -1;
	}
	
	//距離が一番小さい隣接ノードを探す
	for (int i=idx+1; i<fixed.length; i++) {// 距離が小さいものを探す
	    if (fixed[i]==false && distance[i]<distance[idx]) {
		idx=i;
	    }
	}
	
	return idx; //距離が一番小さい隣接ノードの番号を返す
	
    }

    
    public static int length(int i, int j) {
	int from_X = list_x[i];
	int from_Y = list_y[i];
	int to_X = list_x[j];
	int to_Y = list_y[j];
	int len;
	
	len = (int)Math.sqrt((from_X - to_X)*(from_X - to_X) + (from_Y - to_Y)*(from_Y - to_Y));
	
	return len;
    }

}