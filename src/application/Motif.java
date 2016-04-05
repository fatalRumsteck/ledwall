package application;

import java.util.LinkedList;

import javafx.scene.paint.Color;

public class Motif {
	
	private int id;
	private int time;
	private LinkedList<LinkedList<Color>> leds;
	
	
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
		return leds.get(column).get(row);
	}
	
	public Motif(int width, int heigh){
		
		time = 1;		
		leds = new LinkedList<LinkedList<Color>>();
		
		for(int i = 0 ; i < width ; i ++){
			leds.add(new LinkedList<Color>());
			for(int j = 0 ; j < heigh ; j ++){
				Color white = Color.WHITE;
				leds.get(i).add(white);
			}
		}
	}
	
	public void setColor(Color color, int column, int row){
		leds.get(column).set(row, color);
	}
	
	
	
}
