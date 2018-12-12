/*入出力なし*/
/*アルゴリズム班のみ*/
/*stackoverflow問題を解決した最新版(12/12)*/

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
	"00000000000000",
	"0S           0",
	"0     0      0",	
	"0     0      0",
	"0     0      0",
	"0     0      0",
	"0     0      0",
	"0     0      0",	
	"0     0      0",
	"0     0G     0",
	"00000000000000",
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
    
    // ゴールかどうか確認するメソッド
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
    ArrayList <Node> p_list = new ArrayList <Node> (); 
    Astar_Map p_map;
    
    Astar_Map(ArrayList<Node> close){
		for(int i=0;i<close.size();i++){
		    p_list.add(close.get(i));
		}
		//p_list = close;  //+となる全てのNodeを格納するArrayList
		p_map = null;   //parent_node
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
	int cycle_num = 0;
	int min_path;


    NodeList(){
	start_node.fs = start_node.hs;
	open_list.add(start_node);
	search_path(open_list);
    }

	public void action(ArrayList<Node> open) {
		Node new_start_node;
		ArrayList <Node> new_open_list = new ArrayList <Node> (); //引数リスト
		
		search_path(open_list);

		while(true) {
			if (equal_list.size() != 0) {
			new_start_node = equal_list.get(equal_list.size()-1);
			close_list_change(new_start_node);
			delete(equal_list,equal_list.get(equal_list.size()-1));
			new_open_list.add(new_start_node);  		       
			search_path(new_open_list);
			}else{
				return;
			}
		}
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

    //経路探索
    public void search_path(ArrayList<Node> open){
	Node n;

	while(true){
	    if(open.size() == 0){
			System.out.println("There is no route until reaching a goal.");
			equal_list_arrangement(equal_list); 
			if(equal_list.size() == 0){
            	System.out.println("探索終了!");
		    	help_search_path2();
		    	return;
			}else{
		    	return;		    
			}
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
            System.out.println("探索終了!");
		    help_search_path2(); 
		    return;
		}else{
		    return;
		}
	    }
	    
	    search(n, open);
	}
	
	}
	
	public void search(Node n, ArrayList<Node> open) {
		Node v;
        int dist, n_gs;
		boolean flag;
		int x = 0;
		int y = 0; 
		
		n_gs = n.fs - n.hs; 
	    
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
		}else if(i==3){
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
		
		flag = find(x,y,open);
		v = findN(x,y,open);
		dist = (int)(Math.pow((n.pos[0]-x),2) + Math.pow((n.pos[1]-y),2));		
		
		if(flag){
		    if (v.fs > n_gs + v.hs + dist){
			v.fs = n_gs + v.hs + dist;
			v.parent_node = n;
		    }
		}else{
		    flag = find(x,y,close_list);
		    v = findN(x,y,close_list);
		    if(flag){
			if(v.fs > n_gs + v.hs + dist){
			    v.fs = n_gs + v.hs + dist;
			    v.parent_node = n;
			    open.add(v);
			    delete(close_list,v);
			}
		    }else{
			v = new Node(x,y);
			v.fs = n_gs + v.hs + dist;
			v.parent_node = n;
			open.add(v);
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

		//候補マップの表示
		char[][] new_map = new char[aStar.map_height][aStar.map_width];
		for (int i = 0; i < aStar.map_height; i++) {
		    for (int j = 0; j < aStar.map_width; j++) {
			new_map[i][j] = aStar.map[i][j];
	    	}
		}			
		for(int i = 0;i<path_list.size();i++){
		    new_map[path_list.get(i).pos[1]][path_list.get(i).pos[0]] = '+';
		}
		System.out.println("候補マップ:");
		for (int i = 0; i < aStar.map_height; i++) {
	    	String s = String.valueOf(new_map[i]);
	    	System.out.println(s);
		}

		if(cycle_num == 0){
			min_path = path_list.size();
			m = new Astar_Map(path_list);
			a_map = m;
		}else{
			if(min_path<path_list.size()){
				//min_pathより大きい時は格納しない
				return;
			}else{
				//マップ情報を格納・保持
				m = new Astar_Map(path_list);
				m.p_map = a_map;
				a_map = m;
			}
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
	char[][] new_map = new char[aStar.map_height][aStar.map_width];

	//object_dist();
	System.out.println("help searchpath2きた");

	while(true){
	    if(m1 ==null){
			break;
		}
	    //for(int i=0;i<a_map.p_list.size();i++){
		for(int i=0;i<m1.p_list.size();i++){
			haba += m1.p_list.get(i).michihaba;
	    }
		michihaba_width.add(haba);
		haba = 0;
	    m1 = m1.p_map;
	}

	max = michihaba_width.get(0);
	for(int i = 0;i<michihaba_width.size();i++){
	    if(max<michihaba_width.get(i)){
			max = michihaba_width.get(i);
			count = i;
	    }
	}

	while(count >0){
	    m2 = m2.p_map;
	    count--;
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
	System.out.println(cycle_num);
	System.out.println("新しい道幅考慮したマップ:");
	for (int i = 0; i < aStar.map_height; i++) {
	    String s = String.valueOf(new_map[i]);
	    System.out.println(s);
	}
	
	}
	
/*
	//障害物までの距離を計算するメソッド
	public void object_dist(){
		Astar_Map m = a_map;

		for(int i = 0;i<aStar.map_height;i++){
			for(int j = 0;j<aStar.map_width;j++){
				if(aStar.map[i][j] = 'X'){
					node = aStar.map[i+1][j];
					node.dist = 1;
					node = aStar.map[i][j+1];
					node = aStar.map[i+1][j+1];

				}
			}
		}	*/	





    
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
