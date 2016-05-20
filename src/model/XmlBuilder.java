package model;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.*;
import javafx.scene.paint.Color;

public class XmlBuilder {
	
	static public org.jdom2.Document buildXML(Animation animation){
		
		Element racine = new Element("Animation");
		org.jdom2.Document document = new Document(racine);
		
		
		Attribute name = new Attribute("Name", animation.getName());
		Attribute heigh = new Attribute("Heigh", String.valueOf(animation.getHeigh()));
		Attribute width = new Attribute("Width", String.valueOf(animation.getWidth()));
		
		racine.setAttribute(name);
		racine.setAttribute(heigh);
		racine.setAttribute(width);

		
		for( Motif m : animation.getMotifs()){
			Element motif = new Element("Motif");
			
			Attribute idMotif = new Attribute("Id", String.valueOf(m.getId()));
			Attribute time = new Attribute("Time", String.valueOf(m.getTime()));
			
			motif.setAttribute(idMotif);
			motif.setAttribute(time);
			
			racine.addContent(motif);
			
			int i = 1;
			
			for( Color c : m.getLeds()){
				
				Element led = new Element("Led");
				
				Attribute idLed = new Attribute("Id", String.valueOf(i));
				Attribute R = new Attribute("R", String.valueOf((int)(c.getRed()*255)));
				Attribute G = new Attribute("G", String.valueOf((int)(c.getGreen()*255)));
				Attribute B = new Attribute("B", String.valueOf((int)(c.getBlue()*255)));
				
				led.setAttribute(idLed);
				led.setAttribute(R);
				led.setAttribute(G);
				led.setAttribute(B);
				
				motif.addContent(led);
				
				i++;
			}
			
		}
		
		
		return document;
	}
	
	static public void saveXML(String fichier, Document document)
	{
	   try
	   {
	      //On utilise ici un affichage classique avec getPrettyFormat()
	      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	      sortie.output(document, new FileOutputStream(fichier));
	   }
	   catch (java.io.IOException e){}
	} 
	
	static public Animation openAnimation(String file){
		SAXBuilder sxb = new SAXBuilder();
	    Document document = null;
	    Element racine = null;
	    Animation animation = null;
		try
	    {
	       //On crée un nouveau document JDOM avec en argument le fichier XML
	       //Le parsing est terminé ;)
	       document = sxb.build(new File(file));
	       racine = document.getRootElement();
	    }
	    catch(Exception e){}
	    
		if(racine != null){
		
			animation = new Animation(racine.getAttributeValue("Name"), Integer.valueOf(racine.getAttributeValue("Width")), Integer.valueOf(racine.getAttributeValue("Heigh")));
			
			List<Element> listMotif = racine.getChildren("Motif");
			
			for(Element elementMotif : listMotif){
				Motif newMotif = new Motif(animation.getWidth(), animation.getHeigh());
				
				newMotif.setId((Integer.valueOf(elementMotif.getAttributeValue("Id")))-1);
				newMotif.setTime(Integer.valueOf(elementMotif.getAttributeValue("Time")));
				
				List<Element> listLed = elementMotif.getChildren("Led");
				
				for(Element elementLed : listLed){
					int id = Integer.valueOf(elementLed.getAttributeValue("Id")); 
					Color color = new Color(Float.valueOf(elementLed.getAttributeValue("R"))/255, Float.valueOf(elementLed.getAttributeValue("G"))/255, Float.valueOf(elementLed.getAttributeValue("B"))/255, 1);
					newMotif.setColor(color, id);
				}
				animation.add(newMotif);
			}
		}
		return animation;
	}
}
