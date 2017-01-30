package capstone;

import java.io.PrintWriter;
import java.util.LinkedList;

import com.fazecast.jSerialComm.SerialPort;

public class DataSerialWriter {
	
	//We always should connect to this port, left side lower
	static String portName = "COM5";
	
	static SerialPort chosenPort;
	
	static public LinkedList<Integer> populateStepsList(float[][] matrix){
		LinkedList<Integer> stepsList = new LinkedList<Integer>();
		
		float min=matrix[0][0], max=matrix[0][0];
		
		for(int i = 0; i < matrix.length; i++){
			for(int j=0; j < matrix[i].length; j++){
				if(matrix[i][j] > max){
					max = matrix[i][j];
				}
				if(matrix[i][j] < min){
					min = matrix[i][j];
				}
			}
		}
		
		float diff = max - min;
		//this height difference maps to 10cm movable range
		//1000 steps = 0.4cm
		for(int i = 0; i < matrix.length; i++){
			for(int j=0; j < matrix[i].length; j++){
				stepsList.add((int) ((matrix[i][j] - min)/diff * 10f/0.4f * 1000f));
			}
		}
		
		return stepsList;
		
	}
	
	static public void sendData(final LinkedList<Integer> stepsList){
		// Can use this line to pull all possible ports on the PC
				//SerialPort[] portNames = SerialPort.getCommPorts();
				chosenPort = SerialPort.getCommPort(portName);
				chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
				
				if(chosenPort.openPort()) {
					
					// create a new thread for sending data to the arduino
					Thread thread = new Thread(){
						@Override public void run() {
							/* No need to wait, 
							since user won't click to send data immediately after boot*/
							//try {Thread.sleep(100); } catch(Exception e) {}
							// enter an infinite loop that sends text to the arduino
							PrintWriter output = new PrintWriter(chosenPort.getOutputStream());
							//TODO: replace 10 with stepsList.size() later
							String toSend = "";
							for(int i = 0; i < stepsList.size(); i++) {
								System.out.println(" " + stepsList.get(i));
								
								/*Build the string to send*/
								toSend = toSend + stepsList.get(i) + " ";
								//try {Thread.sleep(100); } catch(Exception e) {}
							}
							output.print(toSend);
							output.flush();
						}
					};
					thread.start();
				}else{
					System.out.println("Could not connect to port!");
				}
	}
	
	public static void main (String[] args){
		System.out.println("Initiate testing");
		
		float[][] testData = new float[10][10];
		
		testData[0][0] = 100f;
		testData[0][1] = 50f;
		
		sendData(populateStepsList(testData));
		
	}
	
	static public void printStepsList(LinkedList<Integer> list){
		
		for(int i = 0; i < list.size()/10; i++){
			
			for(int j = 0; j < 10; j++){
				System.out.print( list.get(i*10 + j) + " ");
			}
			
			System.out.print("\n");
		}
		
	}

}

