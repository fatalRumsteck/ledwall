package application;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class Motif {
	
	private int id;
	private int time;
	private ArrayList<ArrayList<Color>> leds;
	
	
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
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
		leds = new ArrayList<ArrayList<Color>>();
		
		for(int i = 0 ; i < width ; i ++){
			leds.add(new ArrayList<Color>());
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
