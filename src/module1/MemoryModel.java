package module1;

public class MemoryModel {
	public static void main(String[] args){
		int var1 = 17;
		int var2 = var1 + 1;
		var1 = var2 + 1;
		System.out.println("Var1:" + var1 + " Var2:" + var2);
	}
}
