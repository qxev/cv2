package test;

/**
 * @author Administrator
 * 
 */
public class Test {

	public static void main(String[] args) {
		int end = 101;
		int start = end;

		while (start > 50) {
			start = end - 1;
			if(start < 50){
				start = 50;
			}
			System.out.print(start);
			System.out.print("-");
			System.out.println(end);
			end = start;
		}
	}

}
