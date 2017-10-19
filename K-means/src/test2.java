import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import K_means.K_Means;
import Structure.Centroid;
import Structure.Create_Doc2;


public class test2 {

	public static void run(){
		try {
			System.out.println("2 acquire");
			test.semaphore1.acquire();
			System.out.println("2 run");
			
			for (int i = 0; i<10; i++){
				System.err.println("\tT222\t"+i);
				Thread.sleep(700);
			}
			
			test.semaphore1.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String args[]) throws Exception{
	/*	ArrayList<Integer> x = new ArrayList<Integer>();
		x.add(1);
		x.add(1);
		x.add(1);
		x.add(1);*/
		K_Means.create_Doc2 = new Create_Doc2();
		Centroid centroid = new Centroid(new ArrayList<Integer>(){{add(13839);}});
		String outputPath = "F:/123";
		FileOutputStream fos;
		ObjectOutputStream  oos;
			fos = new FileOutputStream(outputPath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(centroid);
			oos.close();
			
			
			
		FileInputStream fis = new FileInputStream(outputPath);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Centroid y = (Centroid) ois.readObject();
		System.out.println(centroid.termMap.size()+"\t"+y.termMap.size());
	}
}
