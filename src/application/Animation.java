package application;

import java.util.LinkedList;

public class Animation {
	
	private String name;
	private LinkedList<Motif> Motifs;
	private int width;
	private int heigh;
	
	public Animation(String name, int width, int heigh){
		this.name = name;
		this.width = width;
		this.heigh = heigh;
		Motifs = new LinkedList<Motif>();
	}
	

	public int size(){
		return Motifs.size();
	} 
	public void add(int id, Motif m){
		Motifs.add(id, m);
		refreshID();
		
	}
	public void remove(int index){
		Motifs.remove(index-1);
		refreshID();
	}

	public void refreshID(){
		for(int i = 0 ; i < Motifs.size() ; i++){
			Motifs.get(i).setId(i);
		}
	}
	
	public LinkedList<Motif> getMotifs(){
		return Motifs;
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
