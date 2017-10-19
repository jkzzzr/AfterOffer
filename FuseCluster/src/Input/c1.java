package Input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeSet;

import OriginData.DataInput_My;
import OriginData.IDF;
import filter.structure.Data;

public class c1 {

	public static HashMap<Integer, ArrayList<Integer>> Input_QD= 
			new HashMap<Integer,ArrayList<Integer>>();
	/**
	 * 
	 * @param path_CombSum
	 * @param filepath
	 */
	public static void main(String path_CombSum, String filepath){
		try {
			init_2(path_CombSum);
			init_D_W(filepath);
			init_IDF();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	/**
	 * 同时初始化了 Input_QD和Input_dS
	 * @param path CombSum地址
	 * @throws Exception
	 */
	public static void init_2(String path) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(path));
		String templine = "";
		ArrayList<String> docidList = null;
		HashMap<String, Double> scoreList = null;
		int tempQid = 0;
		while ((templine = br.readLine())!=null){
			StringTokenizer st = new StringTokenizer(templine);
			int qid = Integer.parseInt(st.nextToken());
			if (tempQid != qid){
				if (docidList!=null){
					DataInput_My.Input_QD.put(tempQid, docidList);
					DataInput_My.Input_dS.put(tempQid, scoreList);
				}
				tempQid = qid;
				docidList = new ArrayList<String>();
				scoreList = new HashMap<String, Double>();
			}
			st.nextToken();
			String docid = st.nextToken();
			docidList.add(docid);
			st.nextToken();
			double score = Double.parseDouble(st.nextToken());
			scoreList.put(docid, score);
			DataInput_My.AllDoc.add(docid);
		}
		if (docidList!=null && !DataInput_My.Input_QD.containsKey(tempQid)){
			DataInput_My.Input_QD.put(tempQid, docidList);
			DataInput_My.Input_dS.put(tempQid, scoreList);
		}
		br.close();
	}
	
	
	
	//将所有文档的词项信息录入DataInput_My.Input_D_W中
	//path指定的是存储文档与词频信息的路径
	public static void init_D_W(String path) throws Exception{
		
		File file = new File(path);
		String filename[] = file.list();
		for (String string:filename){
			File file2 = new File(path + "/" + string);
			String filename2[] = file2.list();
			for (String string2: filename2){
				if (DataInput_My.AllDoc.contains(string2)){
					DataInput_My.Input_D_W.put(string2, init_D_W_2(path+"/"+string+"/"+string2));
				}
			}
		}
	}
	
	private static HashMap<String, Integer> init_D_W_2(String path) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(path));
		String templine = "";
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		while ((templine = br.readLine() )!=null){
			try{
				String arr[] = templine.split("\t");
				String term = arr[0];
				if (term.equals("nbsp")){
					continue;
				}
				int count = Integer.parseInt(arr[1]);
				result.put(term, count);
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}
		br.close();
		return result;
	}
	
	/**
	 * 基于前两个已经初始化完成的基础上
	 */
	public static void init_IDF(){
		for (Entry<Integer, ArrayList<String>>  entry: DataInput_My.Input_QD.entrySet()){
			int allWordCount = 0;
			int qid = entry.getKey();
			ArrayList<String> doclist = entry.getValue();
			HashMap<String, Integer> tempResult = new HashMap<>();
			for (String docid : doclist){
				HashMap<String, Integer> temp = DataInput_My.Input_D_W.get(docid);
				if (temp != null && temp.size() !=0){
			//		tempResult.putAll(temp);
					for (Entry<String, Integer> entry2: temp.entrySet()){
						
						allWordCount +=entry2.getValue();
						
						
						String term = entry2.getKey();
						Integer wordcount = entry2.getValue();
						if (tempResult.containsKey(term)){
							Integer tempWordCount = tempResult.get(term);
							wordcount  += tempWordCount;
						}
						tempResult.put(term, wordcount);/*
						if (wordcount == 0){
							int x = 1/0;
						}
						*/
					}
				}
			}
			HashMap<String, Double> doubleResult = new HashMap<>();
			for (String string: tempResult.keySet()){
				int value = tempResult.get(string);
				double pro = (value * 1.0) / allWordCount;
				
				doubleResult.put(string, pro);
			}
			

			
			IDF.wordFrequencyOnCollection.put(qid, doubleResult);
	//		IDF.wordCountOnCollection.put(qid, tempResult);
			
		}
	}
}
