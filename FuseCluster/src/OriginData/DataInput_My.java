package OriginData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class DataInput_My {

	//原始数据：词项列表：qid - doc
	public static HashMap<Integer, ArrayList<String>> Input_QD= 
			new HashMap<Integer,ArrayList<String>>();
	//原始的得分列表：qid - doc - score
	public static HashMap<Integer, HashMap<String,Double>> Input_dS = 
			new HashMap<Integer, HashMap<String,Double>>();
	
	//所有文档的词项列表： doc - wordcount
	public static  HashMap<String, HashMap<String, Integer>> Input_D_W = 
			new HashMap<String, HashMap<String,Integer>>();
	
	
	//文档序号和文档id的对应
	public static HashMap<Integer, ArrayList<String>> Q_Ditem_Docid = 
			new HashMap<Integer, ArrayList<String>>();
	
	
	
	public static HashSet<String> AllDoc = new HashSet<>();
}
