package Entry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;

import Input.c1;
import Input.c2;
import fuse.compute.ClusterFuse;

public class FuseEntry {

	public static int year = 0;
	public static void main(String[] args) {
		c1.main("/home/lee/DATA/data/new-"+ year, "/home/lee/音乐/WordList_perDoc_Path");
		c2.run1("/home/lee/音乐/output-2");
		int first = (year-2009) * 50 +1;
		int end = (year - 2009) * 50 +50;
		for (int qid = first; qid <=end; qid ++){

			c2.run2("/home/lee/音乐/output-2", qid);
			System.out.println("!!!!!" + qid);
			ClusterFuse clusterFuse = new ClusterFuse(qid);
			clusterFuse.run();
			HashMap<String, Double> result = clusterFuse.result;
			write(result, qid, "/home/lee/DATA/result/" + year + "-" + ClusterFuse.lambda);
		}
		System.out.println("END");
		
	}

	public static void write(HashMap<String, Double> result,int qid, String path){
		TreeSet<String> tSet = new TreeSet<>(new Comparator<String>() {

			@Override
			public int compare(String string1, String string2) {
				String str[] = string1.split("[+]");
				String str2[] = string2.split("[+]");
				double d1 = Double.parseDouble(str[0]);
				double d2 = Double.parseDouble(str2[0]);
				if (d1 > d2){
					return -1;
				}else if (d1 < d2){
					return 1;
				}else{
					return str[1].compareTo(str2[1]);
				}
			}
		});
		for (Entry<String, Double> entry: result.entrySet()){
			String value = entry.getValue() +"+" + entry.getKey();
			tSet.add(value);
		}

		try {
		BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
		Iterator<String> iterator = tSet.iterator();
		int count = 1;
		while (iterator.hasNext()){
			String templine = iterator.next();
			String []tempStr = templine.split("[+]");
			double score = Double.parseDouble(tempStr[0]);
			String docid = tempStr[1];
			String outputLine = qid +"\tQ0\t"+docid+"\t"+(count++)+"\t"+score+"\t"+"ClusterFuse";
				bw.write(outputLine+"\n");
		}
		bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
