package K_means;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import Structure.Centroid;
import Structure.Create_Doc2;
import Structure.SingleDoc;
import Structure.type.Relation;

public class K_Means {

	private static final double DISTance = 1.5;
	public static Semaphore semaphore = new Semaphore(1);
	public static int ITERATION = 100;
	public static double lambda = 0.5;
	public static int currentIteration = 0;
	
	//分的个数
	public static int K = 10;
	//所有<文档ID，对应的文档信息词汇等>
//	public static HashMap<String, SingleDoc> AllDocMap = new HashMap<String, SingleDoc>();

//	private static ArrayList<SingleDoc> singleList = new ArrayList<SingleDoc>();
	//所有文档名列表
	public static ArrayList<Integer> alldoclist = new ArrayList<Integer>();
	public static double distanceDEFAULT = Double.MIN_VALUE;
	//质心
	static ArrayList<Centroid>  centroidList = new ArrayList<Centroid>();; 
	//所有<分属的类别，文档ID-List>
//	private HashMap<Integer, ArrayList<Integer>> TypeDocList = new HashMap<Integer, ArrayList<Integer>>();
	
	public static Create_Doc2 create_Doc2 = null;
	
	
	
	public static Stack<Boolean> STACK_Run = new Stack<>();
	public static Stack<Boolean> STACK_Centroid = new Stack<>();
	public static ArrayList<ArrayList<Integer>> SomeArrayList = new ArrayList<>();
	public static int num_Run = 50;
	public static int num_Centroid = 10;
	public static void main(String[] args){
		long startTime = System.currentTimeMillis();
		System.out.println("START  !!");
		Relation.initRelation();
	//	Relation.run();
		K_Means k_Means = new K_Means();
		try {
			k_Means.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("总用时：：：：\t"+(System.currentTimeMillis() - startTime));
	}
	public K_Means(){
		System.out.println("!!!!!!!!");
		create_Doc2 = new Create_Doc2();
		initdocList();
		try {
			initCentroid();
			init_SomeArrayList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化静态变量
	 */
	public void initdocList(){
		System.out.println("@K_meams - initStatic()\tK_means初始化静态变量 singleList和docList");
		try {
			create_Doc2.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("@K_means - initStatic() - end");
	}
	public void init_SomeArrayList(){
		int skipIndex = K_Means.alldoclist.size() / K_Means.num_Run;
		int fromIndex = 0;
		int endIndex = 0;
		int allcount = 0;
		for (int ii = 0; ii <= K_Means.num_Run*2 ;ii++){
			fromIndex = endIndex;
			endIndex = fromIndex + skipIndex;
			if (fromIndex >= K_Means.alldoclist.size()){
				break;
			}
			if (endIndex >K_Means.alldoclist.size()){
				endIndex = K_Means.alldoclist.size();
			}
			//前闭后开[ , )
			ArrayList<Integer> aList = new ArrayList<>(K_Means.alldoclist.subList(fromIndex, endIndex));
			SomeArrayList.add(aList);
			allcount += aList.size();
		}
		System.out.println("list分快结束!总文件数量为:\t"+allcount);
	}
	/**
	 * 初始化质心
	 * @throws Exception 
	 */
	public void initCentroid(){
		System.out.println("@K_means - initCentroid()\t初始化质心");
		centroidList = new ArrayList<Centroid>();
		//随机选出K个文档作为中心，存放在centoridmap中
		ArrayList<Integer> indexlist = new ArrayList<Integer>();
		for (int i = 0; i<K; i++){
			while (true){
				int no = (int) ( K_Means.alldoclist.size()* Math.random());
				if (indexlist.contains(no)){
					continue;
				}else {
				//	SingleDoc singleDoc = AllDocMap.get(K_means.K_Means.docList.get(no));
					int docnumber = K_Means.alldoclist.get(no);
					SingleDoc singleDoc = create_Doc2.getDocItemList(docnumber);
					if (singleDoc.getWordCount() < 100){
						continue;
					}else {
						centroidList.add(new Centroid(singleDoc));
						indexlist.add(no);
						break;
					}
				}
			}
		}
		indexlist.clear();
		System.out.println("@K_means - initCentroid()\tend\t质心数量"+centroidList.size());
		System.err.println("初始化质心完成");
	}
	/**
	 * 整体流程
	 * @throws Exception 
	 */
	public void run() throws Exception{
		System.out.println("@K_meas - run()\t开始执行");
		ArrayList<Centroid>  tempCentroidList = null;
		System.err.println("@K_Means - run()\t 文档总数量为:\t" +  K_Means.alldoclist.size());
		
		for (int iteration = 0; iteration < K_Means.ITERATION; iteration++){
			System.out.println("@K_means - run()/t第"
					+ iteration +"次迭代\t");
			//是否满足终止条件
			if ( isEnd(tempCentroidList) ){
				centroidList = tempCentroidList;
				record();
				return;
			}
			K_Means.currentIteration = iteration;
			//每次迭代开始，清空记录表
			tempCentroidList = centroidList;
			if (iteration != 0){
				record();
			}
			int showNo = 20;
			int aa = K_Means.alldoclist.size() / showNo;
			/*	for (int ii = 0; ii < K_Means.alldoclist.size(); ii ++){
				if (ii > aa && ii %aa ==0){
					System.out.print((showNo--)+"\t");
				}
				int docNo = K_Means.alldoclist.get(ii);
				
				semaphore.acquire();
				
				//计算文档与各个中心之间的距离
				double maxDouble = K_Means.distanceDEFAULT;
				int cenIndex = 0;
				//选择合适的中心
				for (int cenNo = 0; cenNo < centroidList.size(); cenNo ++){
					double tempDistance = distance(K_Means.alldoclist.get(ii), centroidList.get(cenNo));
					if (maxDouble < tempDistance){
						maxDouble = tempDistance;
						cenIndex = cenNo;
					}
				}
				//将文档加入新类中
				Relation.relation.get(cenIndex).add(docNo);
				
				semaphore.release();
				}*/
			
			int checkNum = 0;
			long currentTime = System.currentTimeMillis();
			for (int ii = 0; ii < SomeArrayList.size(); ii ++){	
				ArrayList<Integer> tempaal = SomeArrayList.get(ii);
				MyThread_Run myThread_Run = new MyThread_Run(tempaal);
				myThread_Run.start();
				checkNum +=tempaal.size();
			}
			System.out.println("CHECK:\t"+checkNum);
			while(true){
				//每半个小时检测一遍，看看是否运行完了
				Thread.sleep(1000 * 1);
				if (STACK_Run.isEmpty()){
					break;
				}
			}
			System.out.println("@K_means - run()/t第"
					+ iteration +"次迭代的分类耗时：\t" +(System.currentTimeMillis() - currentTime));
			currentTime = System.currentTimeMillis();
			//更新质心
			refreshAllCentroid();
			System.out.println("@K_means - run()/t第"
					+ iteration +"次迭代计算质心耗时：\t" + (System.currentTimeMillis() - currentTime));
		}
	}
	//没用到
	private void run_SingleDoc(int ii) throws InterruptedException{
		
		int docNo = K_Means.alldoclist.get(ii);
		
		semaphore.acquire();
		
		//计算文档与各个中心之间的距离
		double maxDouble = K_Means.distanceDEFAULT;
		int cenIndex = 0;
		//选择合适的中心
		for (int cenNo = 0; cenNo < centroidList.size(); cenNo ++){
			double tempDistance = distance(K_Means.alldoclist.get(ii), centroidList.get(cenNo));
			if (maxDouble < tempDistance){
				maxDouble = tempDistance;
				cenIndex = cenNo;
			}
		}
		//将文档加入新类中
		Relation.relation.get(cenIndex).add(docNo);
		
		semaphore.release();
	}
	
	
	
	/*
	 * 应当结束就为真
	 */
	private boolean isEnd(ArrayList<Centroid>  tempCentroidList){
		//TODO
		if (tempCentroidList == null){
			return false;
		}
		for (int i = 0; i < tempCentroidList.size(); i ++){
			double dist = distance(K_Means.centroidList.get(i), tempCentroidList.get(i)) ;
			if (dist < K_Means.DISTance){
				return false;
			}
		}
		return true;
	}
	/**
	 * 计算质心的距离
	 * @param centroid
	 * @param centroid2
	 * @return
	 */
	private double distance(Centroid centroid, Centroid centroid2) {
		if (centroid.getTermMap() != null && centroid2.getTermMap()!=null){
			return distance(centroid.getTermMap(), centroid.getWordCount(), centroid2.getTermMap(), centroid2.getWordCount());
		}
		return 0;
	}
	//计算某个文档和质心的距离
	public static double distance(Integer string, Centroid centroid){
		SingleDoc singleDoc = create_Doc2.getDocItemList(string);
		/*if (centroid == null){
			System.out.println("!!!");
		}
		if (singleDoc == null){
			System.out.println("==" + string+"\t"+AllDocMap.containsKey(string));
		}*/
		return distance(singleDoc.getTermMap(), singleDoc.getWordCount(), centroid.getTermMap(), centroid.getWordCount());
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
	//更新全部质心
	private void refreshAllCentroid() throws InterruptedException {
		System.out.print("(");
		int skip = K / num_Centroid;
		int fromIndex = 0;
		int toIndex = -1;
		for (int ii = 0; ii <= num_Run + 1; ii ++){
			fromIndex = toIndex ++;
			toIndex = fromIndex +skip;
			if (fromIndex >= K){
				continue;
			}
			if (toIndex >= K){
				toIndex = K-1;
			}
		//	refreshSingleCentroid(cenNo);
			MyThread_centroid myThread_centroid = new MyThread_centroid(fromIndex, toIndex);
			myThread_centroid.start();
			
		}
		while (true){
			//每2分钟检查一次看看执行好了没
			Thread.sleep(1000 * 1);
			if (STACK_Centroid.isEmpty()){
				break;
			}
		}
		System.out.println(")");
	}
	/**
	 * 更新单个质心
	 * @param cenNo
	 */
	private void refreshSingleCentroid(int cenNo){
		Centroid centroid = null;
		if (Relation.relation.get(cenNo).size() == 0){
			int size = K_Means.alldoclist.size();
			size *= Math.random();
			centroid = new Centroid(create_Doc2.getDocItemList(size));
			System.err.print(","+cenNo+"="+size+"\t");
		}else{
			System.out.print(","+cenNo+"="+Relation.relation.get(cenNo).size()+"\t");
			
			File file = new File(AllPath.outputPath_Type +"/" + K_Means.currentIteration +"/type" + cenNo);
			ArrayList<Integer> docList = new ArrayList<>(10000);
			docList.addAll(Relation.relation.get(cenNo));
			centroid = new Centroid(docList);
		}
		centroidList.set(cenNo, centroid);
		return;
	}
	/**
	 * 列表中的内容输出出来，因为中间可能已经输出过了，然后呢将所有的东西清空掉，
	 * 然后，输出文件应当重新命名！命名中需要包含迭代次数
	 * 每次迭代完了，记录一些数据，保存一下断点
	 * 需要记录：1：质心集合列表，
	 * 					2：所有文档的类别和质心
	 */
	private void record(){
		Thread thread = new Thread(){
			@Override
			public void run(){
				ArrayList<ArrayList<Integer>> temp = null;
				synchronized (Relation.relation) {
					temp = (ArrayList<ArrayList<Integer>>) Relation.relation.clone();
					Relation.initRelation();
				}
				ToolsIO.write(K_Means.currentIteration, centroidList);	
				Relation.writeType(K_Means.currentIteration, true, temp);
			}
		};
		thread.start();
		System.err.println("RECORD！");
	}
	/**
	 * 有问题
	 * @param term
	 * @return
	 */
	/*private double computPBW(String term){
		//TODO
		double p_all = 0.0;
		int num = 0;
		for(int i = 0; i < centroidList.size(); i ++){
			if (!centroidList.get(i).getTermMap().containsKey(term)){
				p_all+= 0; 
			}else {
				p_all +=  centroidList.get(i).getTermMap().get(term)/ centroidList.get(i).getWordCount();
				num++;
			}
		}
		if (num == 0){
			return 0;
		}else {
			return p_all/num;
		}
	}*/
	private static double computPBW(Integer term){
		//TODO
		double p_all = 0.0;
		int num = 0;
		for(int i = 0; i < centroidList.size(); i ++){
			if (!centroidList.get(i).getTermMap().containsKey(term)){
				p_all+= 0; 
			}else {
				double temp=  centroidList.get(i).getTermMap().get(term)/ centroidList.get(i).getWordCount();
				p_all +=temp;
				if (temp >=1){
					System.out.println("err\t"+ temp+"\t"+centroidList.get(i).getTermMap().get(term)+"\t"+centroidList.get(i).getWordCount());
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
}

class MyThread_Run extends Thread{
	ArrayList<Integer> arrayList = new ArrayList<>();
	public MyThread_Run(ArrayList<Integer> tempAlist){
		arrayList = tempAlist;
	}
	@Override
	public void run(){
		K_Means.STACK_Run.add(false);
	//	System.out.print(" + ");
		try{
			for (int ii = 0; ii < arrayList.size(); ii++){
				int docNo = arrayList.get(ii);
		/*		System.out.println(docNo);
				if (!K_Means.alldoclist.contains(docNo)){
					System.out.println("!!!!!" + docNo);
					continue;
				}*/
				//计算文档与各个中心之间的距离
				double maxDouble = K_Means.distanceDEFAULT;
				int cenIndex = 0;
				//选择合适的中心
				for (int cenNo = 0; cenNo < K_Means.centroidList.size(); cenNo ++){
					double tempDistance = K_Means.distance(docNo, K_Means.centroidList.get(cenNo));
					if (maxDouble < tempDistance){
						maxDouble = tempDistance;
						cenIndex = cenNo;
					}
				}
				//将文档加入新类中
			//	Relation.relation.get(cenIndex).add(docNo);
				Relation.add(cenIndex, docNo);
			}
		}finally {
			K_Means.STACK_Run.pop();
	//		System.err.print("S("+ arrayList.size()+")   ");
		}
		Thread.currentThread().interrupt();
	}
}
class MyThread_centroid extends Thread{
	int fromIndex;
	int toIndex;
	public MyThread_centroid(int from, int to){
		fromIndex = from;
		toIndex = to;
	}
	@Override
	public void run(){
		K_Means.STACK_Centroid.add(false);
		for (int cenNo = fromIndex; cenNo <= toIndex; cenNo++){
			Centroid centroid = null;
			/*if (!Relation.relation.contains(cenNo)){
				continue;
			}*/
	//		System.out.println("真的运行了吗");
			if (cenNo >=Relation.relation.size() || cenNo <0){
				continue;
			}
			if (Relation.relation.get(cenNo).size() == 0){
				int size = K_Means.alldoclist.size();
				size *= Math.random();
				centroid = new Centroid(K_Means.create_Doc2.getDocItemList(size));
			}else{
				File file = new File(AllPath.outputPath_Type +"/" + K_Means.currentIteration +"/type" + cenNo);
				ArrayList<Integer> docList = new ArrayList<>(10000);
				docList.addAll(Relation.relation.get(cenNo));
				centroid = new Centroid(docList);
			}
			K_Means.centroidList.set(cenNo, centroid);
		}
		K_Means.STACK_Centroid.pop();
		Thread.currentThread().interrupt();
		return;
	}
}












