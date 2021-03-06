package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
		String  chaine = "";
		String animationName = animation.getName();
		
		boolean status = true;
		folderPath = "../" + animationName;
		File folder;
		folder = new File(folderPath);
		if(!folder.exists()){
			if(!folder.mkdir()){
				status = false;
			}
		}
		
		for(WallPanel currPanel : panels){
			int nbMotif = animation.size();
			int idPanel = currPanel.getId();
			int xmin = currPanel.getCorner1().x;
			int xmax = currPanel.getCorner2().x + 1;
			int ymin = currPanel.getCorner1().y;
			int ymax = currPanel.getCorner2().y + 1;
			int red, green, blue;

			filePath = "../" + animationName + "/" + "panel" + idPanel;
			BufferedWriter writer;
			chaine += (char)nbMotif;
			for(int i = 1; i <= nbMotif; i++){
				chaine += (char)((int)(animation.getMotif(i).getTime()/255));
				chaine += (char)(animation.getMotif(i).getTime()%255);
				
				for(int x = xmin; x < xmax; x++){
					for(int y = ymin; y < ymax; y++){
						Color led = animation.getMotif(i).getColor(x, y);
						red 	= (int) (led.getRed() 	* 255);
						green 	= (int) (led.getGreen() * 255);
						blue	= (int) (led.getBlue() 	* 255);
//						chaine += " R:" + red + " G:" + green + " B:" + blue;
						chaine += (char)red; 
						chaine += (char)green; 
						chaine += (char)blue;
					}
				}
			}
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
				writer.write(chaine);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				status = false;
			}
			chaine = "";
		}
		return status;
	}
	
	
}
