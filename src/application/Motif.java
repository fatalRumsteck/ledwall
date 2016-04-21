package application;

import java.util.LinkedList;

import javafx.scene.paint.Color;

public class Motif {
	
	private int id;
	private int time;
	private int width;
	private LinkedList<Color> leds;
	
	
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id+1;
	}
	public void setTime(int time){
		this.time = time;
	}
	public int getTime(){
		return this.time;
	}
	
	public Color getColor(int column, int row){
		return leds.get(column + row*width);
	}
	
	public LinkedList<Color> getLeds(){
		return leds;
	}
	
	public Motif(int width, int heigh){
		
		this.time = 1;		
		this.leds = new LinkedList<Color>();
		this.width = width;
		
		for(int i = 0 ; i < heigh*width ; i ++){
			Color white = Color.WHITE;
			leds.add(white);
		}
	}
	
	public void setColor(Color color, int column, int row){
		leds.set(column + row*width, color);
	}
	
	public void setColor(Color color, int id){
		leds.set(id-1, color);
	}
	
	
}
