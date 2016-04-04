package application;

import java.util.ArrayList;

public class Animation {
	
	private String name;
	private ArrayList<Motif> Motifs;
	private int width;
	private int heigh;
	
	public Animation(String name, int width, int heigh){
		this.name = name;
		this.width = width;
		this.heigh = heigh;
		Motifs = new ArrayList<Motif>();
	}
	

	public int size(){
		return Motifs.size();
	} 
	public void add(Motif m){
		Motifs.add(m);
		m.setId(Motifs.size());
	}
	public void remove(int index){
		Motifs.remove(index);
	}

	
	
	public Motif getMotif(int id){
		return Motifs.get(id-1);
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeigh() {
		return heigh;
	}
	public void setHeigh(int heigh) {
		this.heigh = heigh;
	}
}
