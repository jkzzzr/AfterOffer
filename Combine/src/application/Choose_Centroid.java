package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * 我的工作！！！
 * 为每个查询，选择几个数据集啊
 * @author Lee
 *
 */
public class Choose_Centroid {
	public int qid ;
	public HashMap<Integer, Double> SCORE = new HashMap<Integer, Double>();
	
	
	/**
	 * 样本文档检索结果
	 * @param sample_path
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Integer> init_result(String sample_path) throws IOException{
		ArrayList<Integer> result = new ArrayList<Integer>();
		BufferedReader br = new BufferedReader(new FileReader(sample_path));
		String line = "";
		while ((line = br.readLine())!=null){
			if (!line.startsWith(this.qid+"")){
				continue;
			}
			StringTokenizer st = new StringTokenizer(line);
			st.nextToken();st.nextToken();
			result.add(Integer.parseInt(st.nextToken()));
		}
		br.close();
		return result;
	}
	
	/**
	 * 所属类别信息
	 * @param typePath
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public HashMap<Integer, Integer> init_type(String typePath) throws IOException, Exception{
		FileInputStream fis = new FileInputStream(new File(typePath));
		ObjectInputStream ois = new ObjectInputStream(fis);
		HashMap<Integer, Integer> hm = (HashMap<Integer, Integer>) ois.readObject();
		ois.close();
		fis.close();
		return hm;
	}
	
	/**
	 * 根据样本文档检索结果，对样本文档所属类别进行打分
	 * @param sample_Result	样本文档检索结果
	 * @param typeSet	文档所属类别的信息
	 * @param outputPath	
	 */
	public void count( ArrayList<Integer> sample_Result , HashMap<Integer, Integer> typeSet){
		if (sample_Result == null || sample_Result.size() == 0){
			System.err.println("myMethod.Choose_Centroid.count(ArrayList<Integer>, HashMap<Integer, Integer>, String)\n\t" + this.qid);
			return ;
		}
		for (int i = 0; i < sample_Result.size(); i++){
			int docid = sample_Result.get(i);
			if (typeSet.containsKey(docid)){
				int type = typeSet.get(docid);
				double score = computeScore(i, type);
				double default_Score = 0.0;
				if (this.SCORE.containsKey(type)){
					default_Score = this.SCORE.get(type);
				}
				this.SCORE.put(type, default_Score + score);
			}else {
				System.err.println("!!!");
			}
		}
	}
	
	/**
	 * 最终要挑选出几个类
	 * @param number
	 */
	public int[] choose(int number){
		if (this.SCORE.size() == 0){
			System.err.println("myMethod.Choose_Centroid.choose(int)\n\t" + this.qid);
			return null;
		}
		if (this.SCORE.size() < number){
			int []result = new int[number];
			int i = 0;
			for (Integer integer : this.SCORE.keySet()){
				result[i++] = integer;
			}
			return result;
		}
		TreeSet<Entry<Integer, Double>> ts = 
				new TreeSet<Entry<Integer, Double>>(new Comparator<Entry<Integer, Double>>() {

					@Override
					public int compare(Entry<Integer, Double> o1,
							Entry<Integer, Double> o2) {
						
						return -(o1.getValue().compareTo(o2.getValue()));
					}
				});
		for(Entry<Integer, Double> entry : this.SCORE.entrySet()){
			ts.add(entry);
		}
		Iterator<Entry<Integer, Double>> it = ts.iterator();
		
		int [] result = new int[number];
		int i = 0;
		while (it.hasNext()){
			if (i == number){
				break;
			}
			result[i++] = it.next().getKey();
		}
		return result;
	}
	/**
	 * 排名在i位的文档，它的对应的类别应该有什么得分
	 * @param i
	 */
	public double computeScore(int r, int type){
		double resutl = 1 - 0.25 * Math.log(r + 1);//（0.3前27名）
	//	double resutl = 1/(r + 60.0);
		if (resutl < 0){
			resutl = 0.0;
		}
		return resutl;
	}
	public void clear(int qi){
		this.qid = qi;
		this.SCORE.clear();
	}
	
	public static void main(String[] args) throws Exception {
		
		Choose_Centroid choose_Centroid = new Choose_Centroid();
		choose_Centroid.qid = 151;
		ArrayList<Integer> sample_Result = choose_Centroid.init_result("E:\\实验数据\\样本文档检索结果");
		HashMap<Integer, Integer> typeSet = choose_Centroid.init_type("E:\\实验数据\\FR-数据\\分类信息-样本文档\\sample-序列化结果");
		choose_Centroid.count(sample_Result, typeSet);
		int [] a = choose_Centroid.choose(10);
		/*System.out.println("结果输出\t" + a.length);
		for (int i = 0; i < a.length; i++){
			System.out.print(a[i]+"\t");
		}
		System.out.println("!!!!\t");
		for(Entry<Integer, Double> entry : choose_Centroid.SCORE.entrySet()){
			System.out.println("("+entry.getKey()+"," + entry.getValue()+")");
		}*/
	}

}
