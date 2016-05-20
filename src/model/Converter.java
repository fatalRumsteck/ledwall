package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import javafx.scene.paint.Color;

public class Converter {
	
	private LinkedList<WallPanel> panels;
	private Animation animation;
	private String filePath;
	private String folderPath;
	
	public Converter(LinkedList<WallPanel> wallConf, Animation animation){
		this.panels = wallConf;
		this.animation = animation;
		this.filePath = "";
		this.folderPath = "";
	}
	
	public boolean startConvertion(){
		// Parcours de wallpanel
			
			// Récu nb motifs
			
			// Parcours des Motifs
				
				// Récu du temps
		
				// Parcours des LED
					
					// Récu couleur
					
				// Fin Parcours LED
				
			// Fin Parcours Motfs
			
			// Création du fichier texte
			
		// Fin Parcours WallPanels
		
		
		String  chaine = "";
		String animationName = animation.getName();
		boolean folderCreated = true;
		folderPath = "D:\\POLYTECH\\DII4A\\PROJETS\\LED Wall\\INFO\\" + animationName;
		File folder;
		folder = new File(folderPath);
		if(!folder.exists()){
			if(!folder.mkdir()){
				folderCreated = false;
			}
		}
		
		for(WallPanel currPanel : panels){
			int nbMotif = animation.size();
			int idPanel = currPanel.getId();			

			filePath = "D:\\POLYTECH\\DII4A\\PROJETS\\LED Wall\\INFO\\" + animationName + "\\" + "panel" + idPanel + ".txt";
			FileWriter writer;
			chaine += (char)nbMotif;
			for(int i = 1; i <= nbMotif; i++){
				chaine += (char)animation.getMotif(i).getTime();
				LinkedList<Color> leds;
				leds = animation.getMotif(i).getLeds();
				for(int j = 0; j < leds.size(); ++j){
					chaine += leds.get(j);
				}
			}
			try {
				writer = new FileWriter(filePath);
				writer.write(chaine);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			chaine = "";
		}
		return true;
	}
	
	
}
