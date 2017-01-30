package GUImodule;
import processing.core.*;

public class MyDisplay extends PApplet{
	public String URL = "http://knightsinntoronto-com.factorepreview.ca/system/images/images/2/original/toronto-012222.jpg?1358279327";
	PImage img;
	
	public void setup(){
		size(800, 500);
		img = loadImage(URL, "jpg");
		img.resize(0, height);
		image(img, 0, 0);
	}
	public void draw(){
		noStroke();
		int[] color = getColor(second());
		fill(color[0], color[1], color[2]);
		ellipse(width/5, height/4, 150, 150);
		
		fill(0, 0, 0);
		ellipse(width/5 - 40, height/4 - 10, 25, 30);
		ellipse(width/5 + 40, height/4 - 10, 25, 30);
	
		fill(250, 100, 30);
		arc(width/5, height/4 + 30, 30, 15, PI/6, PI*5/6);
		
	}
	
	public int[] getColor(int sec){
		
		int dif = Math.abs(30 - sec);
		int[] color = new int[3];
		color[0] = (int)(255*dif/30);
		color[1] = (int)(248*dif/30);
		color[2] = (int)(173*dif/30);
		
		return color;
	}
}
