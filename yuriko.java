/*入出力なし*/
/*アルゴリズム班のみ*/
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
	    "000000000000000000000000000000000000",
	    "0         0000S0000        0    0  0",		
	    "0         0000 0000  0000000  000000",
	    "0         0000 0000  0000000  000000",
	    "0                    0000000  000000",
	    "0         000000000  00000000000   0",
	    "0         000000000  00000000000   0",
	    "0         000000000  00000000000   0",
	    "0             G            0    0  0",
	    "0  00000     0000      000000  0   0",
	    "0  00000     0000       00000  0   0",
	    "0  00000                  000      0",
	    "000000000000000000000000000000000000",
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
	public int michihaba;//new
	int count1 = 1;//new
	int count2 = 1;//new
 
    // コンストラクタ
    public Node (int x, int y) {
        pos[0] = x;  
	pos[1] = y; 
        hs = (int)(Math.pow(x-aStar.goal[0],2) + Math.pow(y-aStar.goal[1],2));
        fs = 0;
	parent_node = null;
	
	// !NEW! michihaba
	if (aStar.map[y][x] == '0' || aStar.map[y][x] == 'X') {
	    michihaba = 0;
	}else{
	    for (int i = x+1; i < aStar.map_width; i++) {
		if (aStar.map[y][i] == '0' || aStar.map[y][i] == 'X') {
		    break;
		}
		count1++;
	    }
	    for (int k = x-1; k > -1; k--) {
		if (aStar.map[y][k] == '0' || aStar.map[y][k] == 'X') {
		    break;
		}
		count1++;
	    }
	    for (int j = y+1; j < aStar.map_height; j++) {
		if (aStar.map[j][x] == '0' || aStar.map[j][x] == 'X') {
		    break;
		}
		count2++;
	    }
	    for (int m = y-1; m > -1; m--) {
		if (aStar.map[m][x] == '0' || aStar.map[m][x] == 'X') {
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

//new!
class Astar_Map{
    ArrayList <Node> p_list = new ArrayList <Node> (); //path_list の list
    Astar_Map p_node;
    
    Astar_Map(ArrayList<Node> close){
	p_list = close;
	p_node = null;
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
    ArrayList <Integer> sum_michihaba  = new ArrayList <Integer> ();  
    ArrayList <Node> equal_list = new ArrayList <Node> ();
    ArrayList <Integer> ngs_list = new ArrayList <Integer> (); //引数リス
    ArrayList <Node> path_list = new ArrayList <Node> ();
	Astar_Map a_map; 


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
	boolean judge = find(n.pos[0],n.pos[1],list);
	if(judge){
	    num = list.indexOf(n);
	    list.remove(num);
	}
    }	

    //while True:以降の処理？
    public void help_search_path1(ArrayList<Node> open){//hikisuu
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
		help_search_path2();
		
		equal_list_arrangement(equal_list);
		/*
		// 描画
		System.out.println("arranged equal_list is ");
		Node noooode;
		for (int i = 0; i < equal_list.size(); i++) {
		    noooode = equal_list.get(i);
		    System.out.print(noooode.pos[0]);
		    System.out.print(",");
		    System.out.println(noooode.pos[1]);
		    }*/
		
		if(equal_list.size() == 0){
		    break;
		}else{
		    new_start_node = equal_list.get(0);
		    close_list_change(new_start_node);
		    delete(equal_list,equal_list.get(0));
		    //equal_list.remove(0);
		    // new_start_node.fs = ngs_list.get(0);
		    ngs_list.remove(0);
		    new_open_list.add(new_start_node);  
		       
		    help_search_path1(new_open_list);		    
		}
	    }
	    
	    n_gs = n.fs - n.hs; //火種になるかもよ！
	    
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
		    (aStar.map[y][x] == '0')) {
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
    
    //new!
    public void help_search_path2(){
	Node path = end_node.parent_node; 
	path_list.add(end_node);	
	
	// endノードから親を辿っていくと、最短ルートを示す.
	for (int i = 0; i < aStar.map_height; i++) {
	    for (int j = 0; j < aStar.map_width; j++) {
		newMap[i][j] = aStar.map[i][j];
	    }	
	}
	
	// ルートとなるノードに印をつける
	while (true) {
	    if (path.parent_node == null) break;
	    path_list.add(path);
	    newMap[path.pos[1]][path.pos[0]] = '+';
	    path = path.parent_node;
	}
	
	//new! マップ情報を格納・保持
	/*
	  a_map = new Astar_Map(path_list);
	  a_map.p_node = a_map;
	  a_map = a_map.p_node;
	  path_list.clear();*/
	
	// 描画
	for (int i = 0; i < aStar.map_height; i++) {
	    for (int j = 0; j < aStar.map_width; j++) {
		String s = String.valueOf(newMap[i][j]);
		System.out.print(s);
	    }
	    System.out.println();
	    }
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
	/*
	// 描画
	System.out.println("close list is ");
	Node nodde;
	for (int i = 0; i < close_list.size(); i++) {
	    nodde = close_list.get(i);
	    System.out.print(nodde.pos[0]);
	    System.out.print(",");
	    System.out.println(nodde.pos[1]);
	    }*/
    }
    
    //new!
    public void search_path(){
	ArrayList<Integer> michihaba_width = new ArrayList<Integer>();
	int haba = 0;
	int max =0;
	Astar_Map m1 = a_map,m2 = a_map;
	int count =0;
	char[][] new_map = new char[aStar.map_height][aStar.map_width];

	help_search_path1(open_list);

/*	while(true){
	    if(a_map ==null){
			break;
	    }
	    for(int i=0;i<a_map.p_list.size();i++){
			haba += m1.p_list.get(i).michihaba;
	    }
	    michihaba_width.add(haba);
	    m1 = m1.p_node;
	}
	
	max = michihaba_width.get(0);
	for(int i = 0;i<michihaba_width.size();i++){
	    if(max<michihaba_width.get(i)){
		max = michihaba_width.get(i);
	    }
	}
	for(int i = 0;i<michihaba_width.size();i++){
	    if(max == michihaba_width.get(i)){
		count = i;
		while(count >=0){
		    m2 = m2.p_node;
		    count--;
		}
	    }
	}
	//map informatin making
	for (int i = 0; i < aStar.map_height; i++) {
	    for (int j = 0; j < aStar.map_width; j++) {
		new_map[i][j] = aStar.map[i][j];
	    }	
	}
	for(int i = 0;i<m2.p_list.size();i++){
	    new_map[m2.p_list.get(i).pos[1]][m2.p_list.get(i).pos[0]] = '+';
	}
	System.out.println("新しい道幅考慮したマップ:");
	for (int i = 0; i < aStar.map_height; i++) {
	    String s = String.valueOf(new_map[i]);
	    System.out.println(s);
	}
	System.out.println();
*/
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
	    if(n.fs == min && minN != n &&aStar.map[n.pos[1]][n.pos[0]] != 'S'){
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
