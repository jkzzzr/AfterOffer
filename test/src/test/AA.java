package test;

public class AA {

	public static void main(String[] args) {
		int a[][]= new int[12345][12345];
		System.out.println(a[0].length);
		RRR r = new RRR() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		};
	}

}
abstract class RRR implements Runnable{
	
}