package capstone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class ElevationReader {
	
	static String baseURL = "http://www.marine-geo.org/services/GridServer?";
	
	static String tail = "&layer=topo&format=esriascii";
	
	static String elevation_file = "elevation.asc";
	
	public static String makeBounds(double n, double s, double e, double w) {
		
		return "north=" + n + "&west=" + w + "&east=" + e + "&south=" + s;
	
	}
	
	/*For a 1 lat by 1 lon square, there are 456 columns and 454 rows, 
	 * we will retrieve at rows/columns that are multiples of 45*/
	
	public static float[][] getElevationMatrix(double n, double s, double e, double w){
		
		float[][] matrix = new float[10][10];
		
		File file = new File(elevation_file);
		
		URL url;
		try {
			url = new URL(baseURL + makeBounds(n,s,e,w) + tail);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
		try {
			FileUtils.copyURLToFile(url, file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line;
			
			int line_num = 0;
			
			int rStep=45, cStep=45;
			
		    while ((line = br.readLine()) != null) {
		    	
		    	if(line.startsWith("ncols")){
		    		int nCol = Integer.parseInt(line.split("\\s+")[1]);
		    		System.out.println(nCol + " columns of data");
		    		cStep = nCol/10;
		    	}else if(line.startsWith("nrows")){
		    		int nRow = Integer.parseInt(line.split("\\s+")[1]);
		    		System.out.println(nRow + " rows of data");
		    		rStep = nRow/10;
		    	}
		    	
		    	line_num++;
		    	
		    	if(line_num%rStep == 0){
		    	
		    		String[] cells = line.split("\\s+");
		    		
		    		for(int i = 1; i <= 10; i++){
		    			matrix[line_num/rStep - 1][i-1] = Float.parseFloat(cells[i*cStep]);
		    		}
		    		
		    	}
		    }
		    
		    br.close();
		    
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
		return matrix;
		
	}

}
