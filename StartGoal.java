/*入出力なし*/
/*アルゴリズムのみ*/
/*スタートとゴール両方から探索 */ 

import java.io.*;
import java.awt.*;
import java.util.*;

public class StartGoal{
    public static int map_width,map_height;
    public static char[][] map;
    public static int[] start = new int[2];
    public static int[] goal = new int[2];

    public static void main (String[] args) {
	
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
    public int Shs, Sfs; /////
    public int Ghs, Gfs; /////
	public Node Sparent_node, Gparent_node; /////
	public int michihaba;//new
	int count1 = 1;//new
	int count2 = 1;//new
 
    // コンストラクタ
    public Node (int x, int y) {
        pos[0] = x;  
        pos[1] = y; 
        Shs = (int)(Math.pow(x-StartGoal.goal[0],2) + Math.pow(y-StartGoal.goal[1],2)); /////
        Sfs = 0; /////
        Ghs = (int)(Math.pow(x-StartGoal.start[0],2) + Math.pow(y-StartGoal.start[1],2));; /////
        Gfs = 0; /////
        Sparent_node = null; /////
        Gparent_node = null; /////
     
	// !NEW! michihaba
	if (StartGoal.map[y][x] == '0' || StartGoal.map[y][x] == 'X') {
	    michihaba = 0;
	}else{
	    for (int i = x+1; i < StartGoal.map_width; i++) {
		if (StartGoal.map[y][i] == '0' || StartGoal.map[y][i] == 'X') {
		    break;
		}
		count1++;
	    }
	    for (int k = x-1; k > -1; k--) {
		if (StartGoal.map[y][k] == '0' || StartGoal.map[y][k] == 'X') {
		    break;
		}
		count1++;
	    }
	    for (int j = y+1; j < StartGoal.map_height; j++) {
		if (StartGoal.map[j][x] == '0' || StartGoal.map[j][x] == 'X') {
		    break;
		}
		count2++;
	    }
	    for (int m = y-1; m > -1; m--) {
		if (StartGoal.map[m][x] == '0' || StartGoal.map[m][x] == 'X') {
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
    
    // 現在地をゴールとするメソッド
    public static boolean isGoal (Node n) {
	    if(n.pos[0] == StartGoal.goal[0] && n.pos[1] == StartGoal.goal[1]){	
	        return true;
	    }else{
	        return false;
	    }
    }

}

//new!
class Astar_Map{
    ArrayList <Node> p_list = new ArrayList <Node> (); 
    Astar_Map p_node;
    
    Astar_Map(ArrayList<Node> close){
	for(int i=0;i<close.size();i++){
	    p_list.add(close.get(i));
	}
	p_node = null;   //parent_node
    }

}


class NodeList{
    char[][] newMap = new char[StartGoal.map_height][StartGoal.map_width];
    ArrayList <Node> open_list1 = new ArrayList <Node> (); /////
    ArrayList <Node> close_list1 = new ArrayList <Node> (); /////
    ArrayList <Node> open_list2 = new ArrayList <Node> (); /////
    ArrayList <Node> close_list2 = new ArrayList <Node> (); /////
    Node start_node = new Node(StartGoal.start[0],StartGoal.start[1]);
    Node goal_node = new Node(StartGoal.goal[0],StartGoal.goal[1]); /////
    Node Send_node, Gend_node; /////
    ArrayList <Node> end_list = new ArrayList <Node> (); 
    ArrayList <Integer> sum_michihaba  = new ArrayList <Integer> ();  
    ArrayList <Node> equal_list1 = new ArrayList <Node> ();
    ArrayList <Node> equal_list2 = new ArrayList <Node> ();
    ArrayList <Integer> ngs_list = new ArrayList <Integer> (); 
    ArrayList <Node> path_list = new ArrayList <Node> ();
    Astar_Map a_map; 
    int cycle_num = 1;
    int min_path;


    NodeList(){
    start_node.Sfs = start_node.Shs; /////
    goal_node.Gfs = goal_node.Ghs; /////
    open_list1.add(start_node); /////
    open_list2.add(goal_node); /////
	action(open_list1, open_list2); /////
    }

    public void action(ArrayList<Node> open1, ArrayList<Node> open2) { //new!再帰防止関数
		Node new_start_node1;
		Node new_start_node2;
		ArrayList <Node> new_open_list1 = new ArrayList <Node> (); 
		ArrayList <Node> new_open_list2 = new ArrayList <Node> (); 
	
		search_path(open_list1, open_list2); 

		while(true){
			if (equal_list1.size() != 0) {
				new_start_node1 = equal_list1.get(equal_list1.size()-1);
				close_list_change(new_start_node1,2);
				delete(equal_list1,equal_list1.get(equal_list1.size()-1));
				new_open_list1.add(new_start_node1);        
				search_path(new_open_list1, open2);
			}else if (equal_list2.size() != 0) {
				new_start_node2 = equal_list2.get(equal_list2.size()-1);
				close_list_change(new_start_node2,1);
				delete(equal_list2,equal_list2.get(equal_list2.size()-1));
				new_open_list2.add(new_start_node2);
				search_path(open1, new_open_list2);
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
    public void search_path(ArrayList<Node> open1, ArrayList<Node> open2){ 
	Node n1,n2;
	ArrayList <Node> parent_list = new ArrayList <Node> (); 

	long currentTime;//タイムアップ
	long processingTime;//タイムアップ
	
	while(true){
	    currentTime = System.currentTimeMillis();//タイムアップ↓
	    processingTime = currentTime-startTime;
	    if(processingTime>12000){//制限時間
		System.out.println("Time is up.");
		help_search_path2();
		return;
	    }//タイムアップ↑
	    if(open1.size() == 0 || open2.size() == 0){ 
		System.out.println("There is no route until reaching a goal.");
		equal_list_arrangement(equal_list1,1);
		equal_list_arrangement(equal_list2,2);
		if(equal_list2.size() == 0 && equal_list1.size() == 0){
		    System.out.println("探索終了!");
		    help_search_path2();
		    return;
		}else{
			return;
		}
	    }
	    
	    n1 = minS(open1); 
	    delete(open1,n1);
	    close_list1.add(n1); 
	    
	    search(n1,open1,1);
	    
	    if(open1.size() == 0 || open2.size() == 0){   
		System.out.println("There is no route until reaching a goal.");
		equal_list_arrangement(equal_list1,1);
		equal_list_arrangement(equal_list2,2);
		if(equal_list2.size() == 0 && equal_list1.size() == 0){
		    System.out.println("探索終了!");
		    help_search_path2();
		    return;
		}else{
		    return;
		}
	    }
	    
	    n2 = minG(open2);
	    delete(open2,n2);
	    close_list2.add(n2); 
	    
	    Node z1 = minS(open1);
	    while (z1 != null) {
		parent_list.add(z1);
		z1 = z1.Sparent_node;
	    }
	    
	    if (find(n2.pos[0],n2.pos[1],parent_list)) {
		Gend_node = n2; 
		Send_node = findN(n2.pos[0],n2.pos[1],parent_list);
		help_search_path1(); 
		cycle_num++;
		equal_list_arrangement(equal_list1,1);
		equal_list_arrangement(equal_list2,2);
		if(equal_list2.size() == 0 && equal_list1.size() == 0){
		    System.out.println("探索終了!");
		    help_search_path2();
		    return;
		}else{
		    return;
		}
	    }
	    parent_list.clear();
	    
	    search(n2,open2,0);
	}

    }

    public void search (Node n, ArrayList<Node> list, int a) { /////new関数
        Node v;
        int dist, n_gs;
	    boolean flag;
	    int x = 0;
        int y = 0;   

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
            }else if (i==3) {
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
            
            if (y <= 0 || y >= StartGoal.map_height ||
                x <= 0 || x >= StartGoal.map_width ||
                (StartGoal.map[y][x] == '0')) {
                continue;
            }

            if (a == 1) {
            n_gs = n.Sfs - n.Shs;
            
            flag = find(x,y,list);
            v = findN(x,y,list);
            dist = (int)(Math.pow((n.pos[0]-x),2) + Math.pow((n.pos[1]-y),2));
            
            if(flag){
                if (v.Sfs > n_gs + v.Shs + dist){
                v.Sfs = n_gs + v.Shs + dist;
                v.Sparent_node = n;
                }
            }else{
                flag = find(x,y,close_list1);
                v = findN(x,y,close_list1);
                if(flag){
                if(v.Sfs > n_gs + v.Shs + dist){
                    v.Sfs = n_gs + v.Shs + dist;
                    v.Sparent_node = n;
                    list.add(v);
                    delete(close_list1,v);
                }
                }else{
                v = new Node(x,y);
                v.Sfs = n_gs + v.Shs + dist;
                v.Sparent_node = n;
                list.add(v);
                }
            }
            } else {
                n_gs = n.Gfs - n.Ghs;
            
                flag = find(x,y,list);
                v = findN(x,y,list);
                dist = (int)(Math.pow((n.pos[0]-x),2) + Math.pow((n.pos[1]-y),2));
                
                if(flag){
                    if (v.Gfs > n_gs + v.Ghs + dist){
                    v.Gfs = n_gs + v.Ghs + dist;
                    v.Gparent_node = n;
                    }
                }else{
                    flag = find(x,y,close_list2);
                    v = findN(x,y,close_list2);
                    if(flag){
                    if(v.Gfs > n_gs + v.Ghs + dist){
                        v.Gfs = n_gs + v.Ghs + dist;
                        v.Gparent_node = n;
                        list.add(v);
                        delete(close_list2,v);
                    }
                    }else{
                    v = new Node(x,y);
                    v.Gfs = n_gs + v.Ghs + dist;
                    v.Gparent_node = n;
                    list.add(v);
                    }
                }
            }
        }

    }
    
    //new!
    public void help_search_path1(){
    Node path1 = Send_node.Sparent_node; /////
    Node path2 = Gend_node.Gparent_node; /////
    Astar_Map m;

	// ルートとなるノードに印をつける
	while (true) {
        if(path1==null){/////このへん変更したyo!
            if (path2.Gparent_node == null) {
                break;
            }
            if (path2.Gparent_node != null) {
                path_list.add(path2);
                path2 = path2.Gparent_node;
            }
        }else if (path2==null){/////このへん変更したyo!
            if (path1.Sparent_node == null) {
                break; 
            }
            if (path1.Sparent_node != null) {
                path_list.add(path1);
                path1 = path1.Sparent_node;
            } 
        }else{
            if (path1.Sparent_node == null && path2.Gparent_node == null) {
                break; 
            }
            if (path1.Sparent_node != null) {
                path_list.add(path1);
                path1 = path1.Sparent_node;
            } 
            if (path2.Gparent_node != null) {
                path_list.add(path2);
                path2 = path2.Gparent_node;
            }
        }
    }
    System.out.println();
    
    // 複数経路の表示（候補マップの表示）
    char[][] new_map = new char [StartGoal.map_height][StartGoal.map_width];
    for (int i = 0; i < StartGoal.map_height; i++) {
	    for (int j = 0; j < StartGoal.map_width; j++) {
		new_map[i][j] = StartGoal.map[i][j];
	    }
	}	
	
	for(int i = 0;i<path_list.size();i++){
        new_map[path_list.get(i).pos[1]][path_list.get(i).pos[0]] = '+';    
    }
    
    //描画
	for (int i = 0; i < StartGoal.map_height; i++) {
	    String s = String.valueOf(new_map[i]);
	    System.out.println(s);
    }

	//マップ情報を格納・保持
	if(cycle_num == 1){
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
            m.p_node = a_map;
            a_map = m;
        }
    }
	path_list.clear();
    }

    public void equal_list_arrangement(ArrayList<Node> equal,int id){/////このへん変更したyo!
	Node n;
	Node parent;
	ArrayList<Node> delete_list = new ArrayList<Node>();
	
    if(id==0){
    	for(int i= 0;i<equal.size();i++){
	        n = equal.get(i);
	        parent = n.Sparent_node;
    	    while(true){
        		if(parent == null) break;
	        	for(int j = 0;j<equal.size();j++){
		            if(parent == equal.get(j)){
			            delete_list.add(equal.get(j));
    		        }
	    	    }
		        parent = parent.Sparent_node;
    	    }
	    }
    	for(int i =0;i<delete_list.size();i++){
	        delete(equal,delete_list.get(i));
        }
    }else{
        for(int i= 0;i<equal.size();i++){
            n = equal.get(i);
            parent = n.Gparent_node;
            while(true){
                if(parent == null) break;
                for(int j = 0;j<equal.size();j++){
                    if(parent == equal.get(j)){
                        delete_list.add(equal.get(j));
                    }
                }   
                parent = parent.Gparent_node;
            }
        }
        for(int i =0;i<delete_list.size();i++){
            delete(equal,delete_list.get(i));
        }
    }
    }

    //new!
    public void close_list_change(Node m,int id){/////このへん変更したyo!
	Node n = m;
    
    if(id==1){
    	close_list1.clear();
    	while(true){
	        n = n.Sparent_node;
	        if(n == null){
    		break;
	        }
	        close_list1.add(n);
        }
    }else{
        close_list2.clear();
    	while(true){
	        n = n.Gparent_node;
	        if(n == null){
    		break;
	        }
	        close_list2.add(n);
        }
    }
    
    }
    
    //new!
    public void help_search_path2(){
	ArrayList<Integer> michihaba_width = new ArrayList<Integer>();
	//ArrayList<Astar_Map> arg = new ArrayList<Astar_Map>();
	int haba = 0;
	int max =0;
	Astar_Map m1 = a_map;
	Astar_Map m2 = a_map;
	int count =0;
	char[][] new_map = new char[StartGoal.map_height][StartGoal.map_width];

	while(true){
	    if(m1 ==null) break;
	    for(int i=0;i<m1.p_list.size();i++){
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
        //count = michihaba_width.size()-i-2;
        count = i;
	    }
	}

	while(count >0){
	    m2 = m2.p_node;
	    count--;
	}

	//map informatin making
	for (int i = 0; i < StartGoal.map_height; i++) {
	    for (int j = 0; j < StartGoal.map_width; j++) {
		new_map[i][j] = StartGoal.map[i][j];
	    }
	}	
	
	for(int i = 0;i<m2.p_list.size();i++){
        new_map[m2.p_list.get(i).pos[1]][m2.p_list.get(i).pos[0]] = '+'; 
    }

    
	System.out.println("新しい道幅考慮したマップ:");
	for (int i = 0; i < StartGoal.map_height; i++) {
	    String s = String.valueOf(new_map[i]);
	    System.out.println(s);
	}

    }
    
    //pythonにあってjavaにない関数の実装
    public Node minS(ArrayList<Node> openM){
	Node minN = openM.get(0);
	Node n;
	int min = minN.Sfs;
	int ngs = 0;
	boolean flag2 = true;

	for(int i = 0;i<openM.size();i++){
	    n = openM.get(i);
	    if(n.Sfs < min){
			minN = n;
			min = n.Sfs;
	    }
	}
	for(int i = 0;i<openM.size();i++){
	    n = openM.get(i);
	    if(n.Sfs == min && minN != n && StartGoal.map[n.pos[1]][n.pos[0]] != 'S'){
	       	for(int j = 0;j<equal_list1.size();j++){
       		if(n.pos[0]==equal_list1.get(j).pos[0] &&
		   n.pos[1]==equal_list1.get(j).pos[1]){
		    flag2 = false;
		}
		}
		if(flag2==true){
		    ngs = n.Sfs - n.Shs;
		    equal_list1.add(n);
		    ngs_list.add(ngs);
		}
	    }
	}
	return minN;
    }
    
    public Node minG(ArrayList<Node> openM){
        Node minN = openM.get(0);
        Node n;
        int min = minN.Gfs;
        int ngs = 0;
        boolean flag2 = true;
    
        for(int i = 0;i<openM.size();i++){
            n = openM.get(i);
            if(n.Gfs < min){
                minN = n;
                min = n.Gfs;
            }
        }
        for(int i = 0;i<openM.size();i++){
            n = openM.get(i);
            if(n.Gfs == min && minN != n && StartGoal.map[n.pos[1]][n.pos[0]] != 'S'){
                   for(int j = 0;j<equal_list2.size();j++){
                   if(n.pos[0]==equal_list2.get(j).pos[0] &&
               n.pos[1]==equal_list2.get(j).pos[1]){
                flag2 = false;
            }
            }
            if(flag2==true){
                ngs = n.Gfs - n.Ghs;
                equal_list2.add(n);
                ngs_list.add(ngs);
            }
            }
        }
        return minN;
        }
}
