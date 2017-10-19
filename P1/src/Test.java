import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class Test {

	public static void main(String asg[]) throws Exception{
/*		HashMap<Integer, HashMap<Integer, Double>> hm = null;
		FileInputStream fis = new FileInputStream(new File("E:\\实验数据\\result/155"));
		ObjectInputStream ois = new ObjectInputStream(fis);
		hm = (HashMap<Integer, HashMap<Integer, Double>>) ois.readObject();*/
		
		
		HashMap<Integer, Integer> aaHashMap = new HashMap<Integer, Integer>();
		aaHashMap.put(1, 2);
		aaHashMap.put(12, 12);
		aaHashMap.put(13, 32);
		aaHashMap.put(14, 52);
		aaHashMap.put(15, 52);
		aaHashMap.put(16, 62);
		ArrayList<Integer> alArrayList = new ArrayList<Integer>(aaHashMap.values());
		alArrayList.add(1);
	}
}
