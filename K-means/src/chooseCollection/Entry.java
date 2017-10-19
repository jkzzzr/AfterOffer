package chooseCollection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import K_means.K_Means;

public class Entry {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static double distance (HashMap<Integer, Double> map1, double tokenier1, HashMap<Integer, Double>map2, double tokenier2){
		Set<Integer> termSet1 = map1.keySet();
		Iterator<Integer> iterator = termSet1.iterator();
		double result = 0.0;
		while (iterator.hasNext()){
			Integer term = iterator.next();
			if (!map2.containsKey(term)){
				continue;
			}
			double count1 = map1.get(term);
			double count2 = map2.get(term);
			//计算变量
			double pcw = count2 / tokenier2;
			double pbw = computPBW(term);
			double pdw = (1 - K_Means.lambda) * count1/tokenier1 + K_Means.lambda * pbw;
			//第一部分
			double part1 = pcw * Math.log(pdw / (K_Means.lambda * pbw));
			double part2 = pdw * Math.log(pcw / (K_Means.lambda * pbw));
			result += part1 + part2;
		}
		return result;
	}
	private static double computPBW(Integer term){
		double p_all = 0.0;
		int num = 0;
		for(int i = 0; i < K_Means.centroidList.size(); i ++){
			if (!K_Means.centroidList.get(i).getTermMap().containsKey(term)){
				p_all+= 0; 
			}else {
				double temp=  K_Means.centroidList.get(i).getTermMap().get(term)/ K_Means.centroidList.get(i).getWordCount();
				p_all +=temp;
				if (temp >=1){
					System.out.println("err\t"+ temp+"\t"+K_Means.centroidList.get(i).getTermMap().get(term)+"\t"+K_Means.centroidList.get(i).getWordCount());
				}
				num++;
			}
		}
		if (num == 0){
			return 0;
		}else {
			return p_all/num;
		}
	}
	public static void init_Centroid(){
		
	}
}

class MyThread_InitCentroid extends Thread{
	
	@Override
	public void run(){
		
	}
}
