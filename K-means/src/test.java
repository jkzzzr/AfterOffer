import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class test {

	public static Semaphore semaphore1 = new Semaphore(1, false);
	public static ArrayList<ArrayList<Integer>> al = new ArrayList<>();
//	public static Semaphore semaphore2 = new Semaphore(-1);
	public static int count = 0;
	public static void main(String args[]){
		ArrayList<String> al = new ArrayList<>();
		al.add("11");
		al.add("22");
		al.add("33");
		System.out.println("sss"+al.contains("11"));
		/*
		
		al.add(new ArrayList<>());
		Stack<Integer> stack  = new Stack<>();
		for (int i = 0; i < 10; i++){
			new Thread(){
				@Override
				public void run(){
					stack.push(1);
					ArrayList<Integer> temp = al.get(0);
			//		synchronized (temp) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						temp.add(count++);
		//			}
					stack.pop();
					Thread.currentThread().interrupt();
				}
			}.start();
		}
		
		while(true){
			if (stack.isEmpty()){
				System.out.println("result\t"+al.get(0).size());
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		*/
		
		
		
		
		
		
		
		
		
		
		
		
//		run();
	}
	public static void run(){
		try {			
			System.out.println("START");
			long timeStart = System.currentTimeMillis();
			for (int i = 0; i<100000000; i++){
				test.semaphore1.acquire();
				test.semaphore1.release();
			}
			System.out.println(System.currentTimeMillis() - timeStart);
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void run1(int a, int b){
		ArrayList<Integer> temp = al.get(a);
		synchronized (temp) {
			Thread.sleep(100);
			temp.add(b);
		}
	}
}
