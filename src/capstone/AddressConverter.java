package capstone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressConverter {
	
	private static String key = "AIzaSyDKyzGifsIAxe0O9kxjrXfkqKtDFano608"; 
	
	public static float[] getLatLonFromAddr(String addr){
			
		float[] res = {0f, 0f};
		
		try {
	        URL url = new URL(
	                "https://maps.googleapis.com/maps/api/geocode/json?address="
	                        + addr.replaceAll("\\s+", "+") + "&key=" + key);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");

	        if (conn.getResponseCode() != 200) {
	            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	        }
	        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

	        String output = "", full = "";
	        while ((output = br.readLine()) != null) {
	            //System.out.println(output);
	            full += output;
	        }
	        
	        String loc = "";
	        
	        Pattern pattern0 = Pattern.compile("\"location\"\\s*:\\s*.*");
	        Matcher matcher0 = pattern0.matcher(full);
	        if (matcher0.find())
	        {
	        	loc = matcher0.group();
	        }else{
	        	return null;
	        }
	        
	        Pattern pattern = Pattern.compile("\"lat\"\\s*:\\s*([0-9.-]+)");
	        Matcher matcher = pattern.matcher(loc);
	        if (matcher.find())
	        {
	        	res[0] = Float.parseFloat(matcher.group(1));
	        	System.out.println("lat: " + res[0]);
	        }else{
	        	return null;
	        }
	        
	        Pattern pattern2 = Pattern.compile("\"lng\"\\s*:\\s*([0-9.-]+)");
	        Matcher matcher2 = pattern2.matcher(loc);
	        if (matcher2.find())
	        {
	        	res[1] = Float.parseFloat(matcher2.group(1));
	        	System.out.println("lng: " + res[1]);
	        }else{
	        	return null;
	        }
	        
	        conn.disconnect();
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	        return null;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	    
	    return res;
		
	} 

}
