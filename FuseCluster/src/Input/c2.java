package Input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import filter.structure.Centroid;
import fuse.compute.PreResult;

public class c2 {

	/**
	 * 初始化：	PreResult.Q_CentrMap
	 * @param args
	 */
	public static void run1(String path) {
		try {
			//******************
			String tempPath = path +"/cenlist";
			File file = new File(tempPath);
			String []strings = file.list();
			for (String string: strings){
					init_Centroid(tempPath+"/"+string, Integer.parseInt(string));
			}

			/*System.out.println("+++");
			//**********************
			String tempPath2 = path +"/simmap";
			File file2 = new File(tempPath2);
			String []strings2 = file2.list();
			for (String string: strings2){
				System.out.println(string);
				init_Simap(tempPath2+"/"+string, Integer.parseInt(string));
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void run2(String path, int qid ) {
		try {
			String tempPath2 = path +"/simmap";
			File file2 = new File(tempPath2);
			String []strings2 = file2.list();
			for (String string: strings2){
				if (Integer.parseInt(string) == qid){
					init_Simap(tempPath2+"/"+string, Integer.parseInt(string));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void init_Centroid(String path,int qid) throws IOException{
		BufferedReader bReader = new BufferedReader(new FileReader(path));
		String temp = "";
		ArrayList<Centroid> result = new ArrayList<>();
		while ((temp = bReader.readLine())!=null){
			StringTokenizer stringTokenizer = new StringTokenizer(temp);
			ArrayList<String > tempList = new ArrayList<>();
			while (stringTokenizer.hasMoreTokens()){
				String string = stringTokenizer.nextToken();
				if (!string.startsWith("clueweb")){
					continue;
				}
				tempList.add(string);
			}
			Centroid centroid = new Centroid();
			centroid.doclist = tempList;
			centroid.sort();
			result.add(centroid);
		}
		bReader.close();
		PreResult.Q_CentrMap.put(qid, result);
	}
	
	public static void init_Simap(String path,int qid) throws Exception{
		System.err.println("@c2.init_Simap() 初始化相似度表的时候，先将表晴空!!!!");
		PreResult.Q_SimMap.clear();
		BufferedReader bReader = new BufferedReader(new FileReader(path));
		String temp = "";
		HashMap<String, HashMap<String, Double>> result = new HashMap<>();
		while ((temp = bReader.readLine())!=null){
			StringTokenizer stringTokenizer = new StringTokenizer(temp);
			String key = stringTokenizer.nextToken();
			double value = Double.parseDouble(stringTokenizer.nextToken());
			String keys[] = key.split("[+]");
			if (result.containsKey(keys[0])){
				HashMap<String, Double> tempMap = result.get(keys[0]);
				tempMap.put(keys[1], value);
			}else {
				result.put(keys[0], new HashMap<String, Double>(){{put(keys[1], value);}});
			}
		}
		PreResult.Q_SimMap.put(qid, result);
		bReader.close();
	}
}
