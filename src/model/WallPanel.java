package model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class WallPanel {
	private int id;
	private Point corner1, corner2;
	
	public WallPanel(){
		id = -1;
		corner1 = new Point(0, 0);
		corner2 = new Point(0, 0);
	 }
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public Point getCorner1(){
		return corner1;
	}
	public Point getCorner2(){
		return corner2;
	}
	
	public void setCorner1(int x, int y){
		corner1.setLocation(x, y);
	}
	
	public void setCorner2(int x, int y){
		corner2.setLocation(x, y);
	}
	
	public LinkedList<WallPanel> checkPanel(LinkedList<WallPanel> list){
		return list.stream()
				   .filter(wp -> testSuperposition(this, wp))
				   .collect(Collectors.toCollection(LinkedList::new));
	}
	
	private boolean testSuperposition(WallPanel panel1, WallPanel panel2){
		return (((panel2.corner1.x >= panel1.corner1.x) && (panel2.corner1.x <= panel1.corner2.x)) || ((panel2.corner2.x >= panel1.corner1.x) && (panel2.corner2.x <= panel1.corner2.x)))
				&& (((panel2.corner1.y >= panel1.corner1.y) && (panel2.corner1.y <= panel1.corner2.y)) || ((panel2.corner2.y >= panel1.corner1.y) && (panel2.corner2.y <= panel1.corner2.y)));
	}
	
	public boolean IsCorrect(){
		return corner1.x <= corner2.x || corner1.y <= corner2.y;
	}
	
	public String toString(){
		return "Panel n°" + id + " : " + corner1.x + "x" + corner1.y + " - " + corner2.x + "x" + corner2.y; 
	}
}
