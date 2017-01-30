package demos;
import java.util.LinkedList;

public class TestClass {
	static String temp = "thisisapuperlongasfdaownrcdshalksdfalskfdlkashdkfjhsakjcnbksnlkajdfkjsancksalkcjnkjs"
+ "hoahwbreocahwobcraiecbhkfdhbcoiuhbpwuhreoichw";
	
	public static void print(){
	LinkedList<String> imageList = new LinkedList<String>();
	while(temp.length() > 127 * 1024 / 2){
        imageList.add(temp.substring(0, 127 * 1024 / 2));
        temp = temp.substring(127 * 1024 / 2);
    }
	
	imageList.add(temp);
	
	String data = "";
	for (String s : imageList){
        data += s;
    }
	System.out.println(temp.length());
	System.out.println(data);
	System.out.println(data.length());
	}
}
