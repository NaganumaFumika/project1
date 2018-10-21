import java.io.*;
import java.awt.*;
import java.util.*;

public class aStar{
    public static int map_width,map_height;
    public static char[][] map;
    public static int[] start = new int[2];
    public static int[] goal = new int[2];

    public static void main (String[] args) {
	Node node;
	
	String[] map_data = {
	    "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO",
	    "OS  O     O     O         O          O",
	    "O   O  O  O  O  O         O    OOOO GO",
	    "O      O     O  O   OOOO  O    O  OOOO",
	    "OO OOOOOOOOOOOOOOO  O     O    O     O",
	    "O                O  O     O          O",
	    "O        OOO     O  O     OOOOOOOOO  O",
	    "O  OO    O    OOOO  O     O      OO  O",
	    "O   O    O          O     O  O   O   O",
	    "O   OOO  O          O        O   O   O",
	    "O        O          O        O       O",
	    "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO",
	};
	
	map_width = 0;
	for (int i = 0; i < map_data.length; i++) {
	    map_width = Math.max (map_width, map_data[i].length());
	}
	map_height = map_data.length;
	
	map = new char[map_height][map_width];
	for (int i = 0; i < map_height; i++) {
	    for (int j = 0; j < map_width; j++) {
		map[i][j] = map_data[i].charAt(j);
		if ( map[i][j] == 'S' ) {
		    start[0] = j; 
		    start[1] = i;
		}
		if( map[i][j] == 'G'){
		    goal[0] = j;
		    goal[1] = i;
		}
	    }
	}
	new NodeList();
    }
}

// Nodeクラス
class Node{
    public int[] pos = new int[2];
    public int hs, fs;
    public Node parent_node;
    /* public int[] owner_list;  // ???*/
 
    // コンストラクタ
    public Node (int x, int y) {
        pos[0] = x;  
	pos[1] = y; 
        hs = (int)(Math.pow(x-aStar.goal[0],2) + Math.pow(y-aStar.goal[1],2));
        fs = 0;
        /*this.owner_list = null;*/
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
	char[][] newMap = new char[aStar.map_height][aStar.map_width];
	//openリストとcloseリストを設定前半
	ArrayList <Node> open_list = new ArrayList <Node> (); 
	ArrayList <Node> close_list = new ArrayList <Node> (); 
	Node start_node = new Node(aStar.start[0],aStar.start[1]); 
	Node end_node;   	

    NodeList(){
	start_node.fs = start_node.hs;
	open_list.add(start_node);
	search_path();

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

    //while True:以降の処理？
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
		    (aStar.map[y][x] == 'O')) {
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
		newMap[i][j] = aStar.map[i][j];
	    }	
	}
	
	// ルートとなるノードに印をつける
	while (true) {
	    if (path.parent_node == null) break;
	    newMap[path.pos[1]][path.pos[0]] = '+';
	    path = path.parent_node;
	}
	
	// 描画
	for (int i = 0; i < aStar.map_height; i++) {
	    String s = String.valueOf(newMap[i]);
	    System.out.println(s);
	}
    }
    
    //pythonにあってjavaにない関数の実装
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