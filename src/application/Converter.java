package application;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Converter {
	
	private LinkedList<WallPanel> panels;
	private Animation animation;
	private String fullPath;
	
	public Converter(LinkedList<WallPanel> wallConf, Animation animation){
		this.panels = wallConf;
		this.animation = animation;
		this.fullPath = "D:\\POLYTECH\\DII4A\\PROJETS\\LED Wall\\INFO";
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
		
		PrintWriter writer = new PrintWriter("panel.txt","UTF-8");
		String  chaine = "";
		for(WallPanel currPanel : panels){
			int nb_motif = animation.size();
			for(int i = 0; i < animation.size(); i++){
				
			}
			chaine += (char)nb_motif + "\n";
		}
		writer.write(chaine);
		writer.close();
		
		return true;
	}
	
	
}
