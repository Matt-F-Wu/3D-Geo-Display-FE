package funProj;
import processing.core.*;

public class funPic extends PApplet{
	
	public void setup(){
		size(800, 500);
	}
	public void draw(){
		size(800, 500);
		background(0);
		noStroke();
		directionalLight(51, 102, 126, -1, 0, 0);
		translate(20, 50, 0);
		sphere(30);
	}
}
