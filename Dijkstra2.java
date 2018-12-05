//route03.dataをダウンロードしてjava Dijkstra<route03.data 実行。最短距離と経由地が出力される

import java.util.Scanner;
import java.io.*;

class Dijkstra {
    public static int[] xzahyou = new int[150];
    public static int[] yzahyou = new int[150];
    
    public static void main(String[] args) {
	
	//Scanner sc = new Scanner(System.in); // 標準入力から読む

	try {
	    File route = new File("./route03.data");
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
		    xzahyou[j] = xxx;
		    System.out.println("x: " + xzahyou[j]);
		    j++;
		    i=2;
		} else if(i==2) {
		    yy = data;
		    yyy = Integer.valueOf(yy);
		    yzahyou[k] = yyy;
		    System.out.println("y: " + yzahyou[k]);
		    k++;
		    i=0;
		}
	    }

	    bufferedReader.close();

	} catch(IOException e) {
            System.out.println("Failed to read file");
	}
	/*	
	int nTown = sc.nextInt(); // ノードの数 = 30
	int nRoute = sc.nextInt(); // 道路の数 = 132
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
	
	
	//自分、隣接ノード、重み（距離）をnodeMapに格納 !!新美くんのつくったものに置き換える
	for (int i=0; i<nRoute; i++) {
	    int from = sc.nextInt(); //自分
	    int to = sc.nextInt(); //行き先（隣接ノード）
	    int len = sc.nextInt(); //←~重みを計算する関数に書き換える
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
    }
    
    
    
    //nodeMapにはnodeMap[自分][隣接ノード]=距離(重み)が格納されている
    //srcはスタート地点
    //各ノードまでのdistanceは最短距離
    
    public static void dijkstra(int[][] nodeMap,int start,int[] distance, int[] via) {

	int nTown = distance.length; //ノードの数
	boolean[] fixed = new boolean[nTown]; // 最短距離が確定→true、未確定→false、各ノードに対して確認
	
	
	//各ノードの最短距離を初期化
	for (int i=0; i<nTown; i++) {
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
	    
	    for (int j=0; j<nTown; j++) { // 隣の都市(i)について
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
	*/
    }

    public void length() {
	int x;
	int y;
	for(int i=0; i<xzhyou.length; i++) {
	    x = xzahyou[i];
	    y = yzahyou[i];
	}
    }
}
