package capstone;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PImage;
import org.gicentre.utils.gui.TextInput;
import processing.core.PFont;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setup and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	//special data units
	
	private Location selectedLocation;
	
	PImage zoonIn, zoonOut, reset, sicon;
	
	TextInput search;
	
	float pinX, pinY;
	
	boolean drawPin = false;
	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(1400, 1400, OPENGL);
		
		loadMap();
		
		zoonIn = loadImage("ic_add_circle_white_24dp_2x.png");
		
		zoonOut = loadImage("ic_remove_circle_white_24dp_2x.png");
		
		reset = loadImage("ic_autorenew_white_24dp_2x.png");
		
		sicon = loadImage("ic_search_black_24dp_1x.png");
		
		search = new TextInput(this, createFont("Arial", 16), 16);
	    
	}  // End setup
	
	public void loadMap(){
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		}
		else {
			//map = new UnfoldingMap(this, 200, 00, 1000, 800, new Google.GoogleMapProvider());
			map = new UnfoldingMap(this, 0, 0, 1400, 1400, new Microsoft.AerialProvider());
		}
		
		MapUtils.createDefaultEventDispatcher(this, map);
	}
	
	public void draw() {
		background(90, 90, 90);
		map.draw();
		
		Location location = map.getLocation(mouseX, mouseY);
		fill(255, 0, 0);
		textSize(24);
		textAlign(LEFT);
	    text(location.getLat() + ", " + location.getLon(), mouseX, mouseY);
		
		addButton();
		addSelection();
		
		addSearch();
		
		if(drawPin){
			drawCircleAt();
		}
	}
	
	public void addSearch(){
		fill(255, 255, 255);
		rect(28, 358, 230, 30);
		
		image(sicon, 228, 360, 30, 30);
		
		fill(0);
		search.draw(32, 360);
		
	}
	
	public void addButton(){
		
		image(zoonIn, 70, 440, 60, 60);
		
		image(zoonOut, 70, 520, 60, 60);
		
		image(reset, 70, 600, 60, 60);
	}
	
	public void addSelection(){
		
		if(selectedLocation == null) return;
		
		Location location = selectedLocation;
		
		float lat = location.getLat();
		float lon = location.getLon();
		
		ScreenPosition topLeft = map.getScreenPosition(new Location(Math.floor(lat), Math.floor(lon)));
		
		ScreenPosition bottomRight = map.getScreenPosition(new Location(Math.ceil(lat), Math.ceil(lon)));
		
		fill(142, 181, 242, 100f);
		
		rect(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
		
	}
	
	// If there is a marker under the cursor, and lastSelected is null 
	// set the lastSelected to be the first marker found under the cursor
	// Make sure you do not select two markers.
	//
	
	@Override
	public void keyPressed(){
		
		if(key == ENTER | key == RETURN){
			closingIn();
		}else{
			search.keyPressed();
		}
	}
	
	
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked()
	{	
		if(mouseX < 130 && mouseX > 70 && mouseY < 500 && mouseY > 440){
			map.zoomIn();
			map.zoomIn();
			map.zoomIn();
		}else if(mouseX < 130 && mouseX > 70 && mouseY < 580 && mouseY > 520){
			map.zoomOut();
			map.zoomOut();
			map.zoomOut();
		}else if(mouseX < 130 && mouseX > 70 && mouseY < 660 && mouseY > 600){	//reset to initial state	
			loadMap();
			drawPin = false;
			selectedLocation = null;
		}else if(mouseX < 258 && mouseX > 228 && mouseY < 390 && mouseY > 360){
			closingIn();
		}
		else{
			//timing for test
			long start = System.currentTimeMillis();
			
			drawPin = false;
			
			selectedLocation = map.getLocation(mouseX, mouseY);
			map.zoomAndPanTo(9, selectedLocation);
			
			float lat = selectedLocation.getLat();
			float lon = selectedLocation.getLon();
			
			float[][] matrix = ElevationReader.getElevationMatrix(Math.ceil(lat), Math.floor(lat), Math.ceil(lon), Math.floor(lon));
		
			long end = System.currentTimeMillis();
			
			System.out.println("Time elapse: " + (end - start)/1000 + " sec");
			
			System.out.println("Bounds: " + Math.ceil(lat)+ " " + Math.floor(lat) + " " + Math.ceil(lon)+ " " + Math.floor(lon));
			
			//System.out.println("Elevation Matrix");
			
			//printMatrix(matrix);
			
			//System.out.println("Matrix of Steps");
			
			//DataSerialWriter.printStepsList(DataSerialWriter.populateStepsList(matrix));
		}
	}
	
	public void printMatrix(float[][] m){
		
		for(int i = 0; i < m.length; i++){
			for(int j = 0; j < m[i].length; j++){
				System.out.print(m[i][j] + " ");
			}
			System.out.print("\n");
		}
		
	}
	
	public void closingIn(){
		//API Key needed
		
		float[] target = AddressConverter.getLatLonFromAddr(search.getText());
		
		if(target == null){
			
			search.setText("Cannot recognize address!");
			
		}else{
			System.out.println("Located at: " + target[0] + " " + target[1]);
			
			pinX = target[0];
			pinY = target[1];
			
			drawPin = true;
			
			drawCircleAt();
			
			map.zoomAndPanTo(9, new Location(target[0], target[1]));
		}
	}
	
	public void drawCircleAt(){
		ScreenPosition pin = map.getScreenPosition(new Location(pinX, pinY));
		fill(255, 255, 0);
		stroke(255, 0, 0);
		ellipse(pin.x, pin.y, 25, 25);
		noStroke();
	}
	
}
