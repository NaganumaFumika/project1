import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.RenderingHints;

public class Project {
    
    public static void main(String[] args) {
        JFrame fr = new JFrame("map");
	
	fr.setSize(900, 900);
	fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.getContentPane().setBackground(new Color(255, 255, 255));
	
	Map map = new Map();
        map.setOpaque(false);
	fr.add(map);
	
        fr.setVisible(true);
    }
}
class Map extends JPanel {
    
    public char map[][];
    private int xx = 30, yy = 30;
    
    Map() {
	map = new char[xx][yy];
	
        for (int x=0; x<xx; x++) {
            for (int y=0; y<yy; y++) {
		map[x][y] = 0;
            }
	}
	
        for(int x=0; x<xx; x++) {
            for(int y=0; y<yy; y++) {
                map[3][y] = 1;
	        map[9][y] = 1;
		map[15][y] = 1;
	        map[x][4] = 1;
		map[x][10] = 1;
                map[x][19] = 1;
                map[x][23] = 1;
                map[x][25] = 1;
                /*if(x == y) {                                                  
		  map[x][y] = 1;                                              
		  }*/
		if(10 <= y) {
                    map[5][y] = 1;
                }
                if(10 <= y) {
	            map[3][y] = 1;
                }
                if(9 <= x && x <= 15) {
                    map[x][13] = 1;
                }
    		if(4 <= y) {
                    map[22][y] = 1;
	        }
                if(x <= 22) {
	            map[x][7] = 1;
                }
		if(19 <= y && y <= 25) {
                    map[18][y] = 1;
                }
	    }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
	
        for (int x=0; x<xx; x++) {
            for (int y=0; y<yy; y++) {
                if(map[x][y] == 1) {
                    g.setColor(Color.gray);
                    g.fillRect(x*30, y*30, 30, 30);
                }
                System.out.println(map[x][y]);
            }
    	}
	
    }
}
