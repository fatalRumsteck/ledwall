package application;

import javafx.scene.shape.Rectangle;

public class PlayThread extends Thread{

	
	private Controller controller;
	private int indexFisrtMotif;
	private boolean running = true;;
	
	public PlayThread(Controller controller , int indexfirstMotif){
		this.controller = controller;
		this.indexFisrtMotif = indexfirstMotif;
	}
	
	public void run (){
		for(Motif m : controller.getAnimation().getMotifs()){
			
			if(m.getId() >= indexFisrtMotif){
				
				// Mise à jour de l'IHM
				for (int i = 0; i < controller.getAnimation().getWidth(); i++) {
					for (int j = 0; j < controller.getAnimation().getHeigh(); j++) {
						Rectangle currentRect = (Rectangle) Controller.getNodeFromGridPane(controller.getWall(), i, j);
						currentRect.setFill(m.getColor(i, j));
					}
				}
				
				try {
					// Temporisation correspondant à la durée du motif
					int i = 0;
					while(running && i<m.getTime()){
						sleep(100);
						i++;
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(running == false){
				break;
			}
		}
		
		controller.setDisableIHMforPlay(false);
	}
	
	public void stopThread(){
		running = false;
	}
	
}
