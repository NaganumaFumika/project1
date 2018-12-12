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


public class D_P extends JPanel{
    Map2 map = new Map2();
    Map2.human2 user = map.new human2();
    Map2.goal2 goal = map.new goal2();
    int xl = map.lx;
    int yl = map.ly;
    int xn = 900/xl;
    int yn = 900/yl;
    int n=1;
    ArrayList<Map2> m = new ArrayList<Map2>();
    ArrayList<Dijkstra2> di = new ArrayList <Dijkstra2>();
    ArrayList<Map2.human2> human2 = new ArrayList <Map2.human2>();
    public static char[][] map_result = new char[900][900];

    D_P() {
	human2.add(user);
	m.add(map);
	setOpaque(false);

	addMouseListener(new MouseAdapter() {
		int i=0;
		int s_x, s_y, e_x, e_y;

		public void mouseClicked(MouseEvent e) {
		    if(0<=e.getX() && e.getX()<=900 && 0<=e.getY() && e.getY()<=900) {
			if(map.road(e.getX(), e.getY())) {
			    if(i==0) {
				user.movehuman(e.getX()/xn*xn+xn/2, e.getY()/yn*yn+yn/2);
				//map.p.setxy(e.getX()/xn*xn+xn/2, e.getY()/yn*yn+yn/2);
				map.map[e.getY()/yn][e.getX()/xn] = 'S';
				di.get(0).start_x = e.getX();
				di.get(0).start_y = e.getY();
				s_x = e.getX()/xn;
				s_y = e.getY()/yn;
				repaint();
				i=1;
			    } else if(i==1) {
				user.x_end = e.getX();
				user.y_end = e.getY();
				goal.x = e.getX();
				goal.y = e.getY();
				m.get(0).map[e.getY()/yn][e.getX()/xn] = 'G';
				map.map[s_y][s_x] = 'S';
				di.get(0).end_x = e.getX();
				di.get(0).end_y = e.getY();
				e_x = e.getX()/xn;
				e_y = e.getY()/yn;

				Random rnd = new Random();
				int x_rnd = 0;
				int y_rnd = 0;
				for(int i=1; i<n; i++) {
				    m.add(new Map2());
				    m.get(i).map[e.getY()/yn][e.getX()/xn] = 'G';
				    while(true) {
					x_rnd = rnd.nextInt(xl);
					y_rnd = rnd.nextInt(yl);
					if(m.get(i).road(x_rnd*xn, y_rnd*yn) == true) {
					    m.get(i).map[y_rnd][x_rnd] =  'S';
					    di.get(i).start_x = x_rnd*xn;
					    di.get(i).start_y = y_rnd*yn;
					    break;
					}
				    } 
				    //change
				    //human2.add(m.get(i).new human2(x_rnd*xn+xn/2, y_rnd*yn+yn/2));
				}
				Dijkstra di;

				for(int i=0; i<n; i++) {
				    //di = new Dijkstra(m.get(i).map);
				    for(int a=0; a<xl; a++) {
					for(int b=0; b<yl; b++) {
					    m.get(i).map[b][a] = map_result[b][a];
					}
				    }
				    di = null;
				}
				i=2;
			    } else if(i==2) {
				//barrier
			    } else {
				//fire
			    }
			} else {
			    System.out.println("Please select road");
			}
		    } else {
			System.out.println ("Please click map");
		    }
		}
	    });
    }
    
    @Override
	public void paintComponent(Graphics g) {
	g.drawImage(map.mapImg2,0,0,900,900,this);
	map.draw(g);
	
	if((user.x_now!=0) && (user.y_now!=0)) {
	    goal.draw(g);
	}
    }

    public static void main(String[] args) {
	JFrame fr = new JFrame("map");
	fr.setSize(900,900);
	fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	fr.getContentPane().setBackground(new Color(255,255,255));
	D_P dp = new D_P();
	dp.setOpaque(false);
	fr.add(dp);
	fr.setVisible(true);
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
    Dijkstra2 dik = new Dijkstra2();
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

    /*class human2 {
        int x_now = 0;
        int y_now = 0;
        int x_end = 0;
        int y_end = 0;
        int vx = 0;
        int vy = 0;

        human2(){}

        human2(int x_n,int y_n){
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
	}*/


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


    public void DK_syumi() {
	ArrayList<Integer> x = new ArrayList<Integer>();
	ArrayList<Integer> y = new ArrayList<Integer>();
	//human2 user = new human2();

	/*x.add(0);
	  x.add(600);
	  x.add(200);
	  y.add(300);
	  y.add(500);
	  y.add(800);*/
	
	
	user.x_now = dik.xzahyou.get(0);
	user.y_now = dik.yzahyou.get(0);
	//user.y_now = y.get(0);
	
	new javax.swing.Timer(10, new ActionListener(){
		public void actionPerformed(ActionEvent evt){
		    
		    move(x,y,user);//引数大きいほど、人が動くのが早い                                
		    repaint();
		}
	    }).start();
    }

    public void  move(ArrayList<Integer> a,ArrayList<Integer> b, human2 h){
	int x_p = dik.xzahyou.get(0);
	int y_p = dik.yzahyou.get(0);
	
	int x_s,y_s ;
	if(dik.xzahyou.size()>0){
	    if(h.x_now == x_p && h.y_now == y_p){
		dik.xzahyou.remove(0);
		dik.yzahyou.remove(0);
		x_p = dik.xzahyou.get(0);
		y_p = dik.yzahyou.get(0);
		
	    }else{
		x_s = x_p-h.x_now;
		y_s = y_p-h.y_now;
		if(x_s!=0){
		    x_s = x_s/Math.abs(x_s);
		}
		if(y_s != 0){
		    y_s = y_s/Math.abs(y_s);
		    
		}
		
		h.movehuman(h.x_now + x_s,h.y_now + y_s);
	    }
	}
	
	
    }
    @Override
    public void paintComponent(Graphics g){
	user.draw(g);
	g.setColor(Color.blue);
	for(int i = 0;i < dik.xzahyou.size();i++){
	    
	    g.fillOval(dik.xzahyou.get(i),dik.yzahyou.get(i),10,10);
	}
	g.drawLine(user.x_now, user.y_now,dik.xzahyou.get(0), dik.yzahyou.get(0));
	for(int i = 0;i < dik.xzahyou.size()-1;i++){
	    g.drawLine(dik.xzahyou.get(i), dik.yzahyou.get(i),dik.xzahyou.get(i+1), dik.yzahyou.get(i+1));
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
}



class Dijkstra2 {
    public static ArrayList<Integer> xzahyou = new ArrayList<Integer>();
    public static ArrayList<Integer> yzahyou = new ArrayList<Integer>();
    public static int[] list_x = new int[150];
    public static int[] list_y = new int[150];
    public int start_x, start_y, end_x, end_y;
    
    //public static void main(String[] args) {
    public static void dijkstra(int[][] nodeMap2,int start2,int[] distance2, int[] via2) {

	
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
		    System.out.println("no." + no);
		    i=1;
		} else if(i==1) {
		    xx = data;
		    xxx = Integer.valueOf(xx);
		    list_x[j] = xxx;
		    xzahyou.add(xxx);
		    System.out.println("x: " + list_x[j]);
		    j++;
		    i=2;
		} else if(i==2) {
		    yy = data;
		    yyy = Integer.valueOf(yy);
		    list_y[k] = yyy;
		    yzahyou.add(yyy);
		    System.out.println("y: " + list_y[k]);
		    k++;
		    i=0;
		}
	    }

	    bufferedReader.close();

	} catch(IOException e) {
            System.out.println("Failed to read file");
	}
	

	
	Scanner sc = new Scanner(System.in); // 標準入力から読む
		
	int nTown = sc.nextInt(); // ノードの数 = 105
	int nRoute = sc.nextInt(); // 道路の数 = 292
	int[][] nodeMap = new int[nTown][nTown]; // 都市の接続関係のマップ
	
	
	//隣接関係のnodeMapを初期化
	for (int i=0; i<nTown; i++) {
	    for (int j=0; j<nTown; j++) {
		if(i == j) {
		    nodeMap[i][j] = 0;
		}else{
		    nodeMap[i][j] = -1;
		}
	    }
	}
	
	
	//自分、隣接ノード、重み（距離）をnodeMapに格納
	for (int i=0; i<nRoute; i++) {
	    int from = sc.nextInt(); //自分
	    int to = sc.nextInt(); //行き先（隣接ノード）
	    int len = length(from, to); //距離

	    nodeMap[from][to] = nodeMap[to][from] = len; //逆向きも格納
	}
	
	
	int start = sc.nextInt(); // 出発地点 = 0←ノードの番号
	int goal = sc.nextInt();	// 到着地点 = 29
	//ここまで.dataから読み込み＝シミュレーション側のmapからnodeMapを作る作業
	
	// 各ノードまでの最短距離
	int[] distance = new int[nTown];
	//経由したノード
	int[] via = new int[nTown];
	
	dijkstra(nodeMap, start, distance, via);
	
	if (distance[goal] == Integer.MAX_VALUE) {
	    System.out.println("There is no route");
	} else {
	    System.out.println("distance="+distance[goal]); //距離
	    for(int i=goal; i!=start; i=via[i]) {
		System.out.print(i + " "); //経由地
	    }
	    System.out.println(start); //スタート
	} 
    
    
    //nodeMapにはnodeMap[自分][隣接ノード]=距離(重み)が格納されている
    //srcはスタート地点
    //各ノードまでのdistanceは最短距離
    
    //public static void dijkstra(int[][] nodeMap,int start,int[] distance, int[] via) {
	int nTown2 = distance.length; //ノードの数
	boolean[] fixed = new boolean[nTown2]; // 最短距離が確定→true、未確定→false、各ノードに対して確認
	
	
	//各ノードの最短距離を初期化
	for (int i=0; i<nTown2; i++) {
	    distance[i] = Integer.MAX_VALUE; // 初期値は無限
	    fixed[i] = false;	// 最短距離はまだ確定していない
	    via[i] = -1;
	}
	
	distance[start] = 0;	// 出発地点までの距離を0とする
	
	//無限に繰り返す
	while (true) {
	
	    // 未確定の中で最も近い都市を求める
	    int marked = minIndex(distance,fixed);
	    
	    if (marked < 0) return; // 全都市が確定した場合
	    if (distance[marked]==Integer.MAX_VALUE) return; // 非連結グラフ
	    
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