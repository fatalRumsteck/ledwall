package application;

import java.awt.Point;

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
}
