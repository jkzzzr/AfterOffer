package K_means;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import Structure.Centroid;
import Structure.Create_Doc2;
import Structure.SingleDoc;
import Structure.type.Relation;
/**
 * 将所有文档全部归入分类的代码
 * @author Administrator
 *
 */
public class Classify {

	//分的个数
	public static int K = 10;
	//所有文档名列表
	
	
	public static Create_Doc2 create_Doc2 = null;
	
	

	public static Stack<Boolean> STACK_Run = new Stack<>();
	public static Stack<Boolean> STACK_Record = new Stack<>();
	
	public static ArrayList<ArrayList<Integer>> SomeArrayList = new ArrayList<>();
	
	public static int num_Run = 250;
	public static int num_Centroid = 5;
	
	public static void main(String[] args){
		long startTime = System.currentTimeMillis();
		System.out.println("START  !!");
		Relation.initRelation();
		Classify classify = new Classify();
		try {
			classify.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("总用时：：：：\t"+(System.currentTimeMillis() - startTime));
	}
	public Classify(){
		System.out.println("!!!!!!!!");
		create_Doc2 = new Create_Doc2();
		Create_Doc2.percent = 1.0;
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
		System.out.println("@K_meams - initStatic()\tClassify初始化静态变量 singleList和docList");
		try {
			create_Doc2.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("@Classify - initStatic() - end");
	}
	public void init_SomeArrayList(){
		int skipIndex = K_Means.alldoclist.size() / Classify.num_Run;
		int fromIndex = 0;
		int endIndex = 0;
		int allcount = 0;
		for (int ii = 0; ii <= Classify.num_Run +1;ii++){
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
	 * @throws Exception 
	 */
	public void initCentroid() throws Exception{
		System.out.println("@Classify - initCentroid()\t初始化质心");
		K_Means.centroidList = new ArrayList<Centroid>();
		//随机选出K个文档作为中心，存放在centoridmap中
		for (int i = 0; i<K; i++){
			FileInputStream fis = new FileInputStream(AllPath.inputPath_Centroid +"/" + i);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Centroid centroid  = (Centroid)ois.readObject();
			K_Means.centroidList.add(centroid);
		}
		System.out.println("@Classify - initCentroid()\tend\t质心数量"+K_Means.centroidList.size());
		System.err.println("初始化质心完成");
	}
	/**
	 * 整体流程
	 * @throws Exception 
	 */
	public void run() throws Exception{
		System.out.println("@Classify - run()\t开始执行");
		System.err.println("@Classify - run()\t 文档总数量为:\t" +  K_Means.alldoclist.size());
		System.out.println("11\t" + K_Means.centroidList.get(5).getWordCount());
		long currentTime = System.currentTimeMillis();
		for (int ii = 0; ii < SomeArrayList.size(); ii ++){	
			ArrayList<Integer> tempaal = SomeArrayList.get(ii);
			MyThread_1 myThread_1 = new MyThread_1(tempaal);
			myThread_1.start();
		}
	/*	while(true){
			//每半个小时检测一遍，看看是否运行完了
			Thread.sleep(1000 * 10);
			if (STACK_Run.isEmpty()){
				break;
			}
		}*/
		System.out.println("@Classify - run()/t"
				+ "分类耗时：\t" +(System.currentTimeMillis() - currentTime));
		currentTime = System.currentTimeMillis();
		//更新质心
//		write();
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				try {
					write();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		Timer timer = new Timer();
		timer.schedule(timerTask, 1000 * 2);
		while (true){
			//每2分钟检查一次看看执行好了没
			Thread.sleep(1000 * 10);
			if (STACK_Record.isEmpty()){
				timer.cancel();
			//	write();
				break;
			}
		}
		System.out.println("@Classify - run()/t"
				+ "写出耗时：\t" + (System.currentTimeMillis() - currentTime));
	}
	public void write() throws Exception{
		int skip = K / Classify.num_Centroid;
		int fromIndex = 0;
		int toIndex = -1;
		for (int ii = 0; ii <= K + 1; ii ++){
			fromIndex =  ++ toIndex;
			toIndex = fromIndex +skip;
			if (fromIndex >= K){
				continue;
			}
			if (toIndex >= K){
				toIndex = K-1;
			}
			MyThread_Record myThread_Record = new MyThread_Record(fromIndex, toIndex);
			myThread_Record.start();
			
		}
		System.out.println(")");
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
}

class MyThread_1 extends Thread{
	ArrayList<Integer> arrayList = new ArrayList<>();
	public MyThread_1(ArrayList<Integer> tempAlist){
		arrayList = tempAlist;
	}
	@Override
	public void run(){
		try {
			Thread.sleep(1000 * 10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Classify.STACK_Run.add(false);
		System.out.print(" + ");
		try{
			for (int ii = 0; ii < arrayList.size(); ii++){
				int docNo = arrayList.get(ii);
				//计算文档与各个中心之间的距离
				double maxDouble = K_Means.distanceDEFAULT;
				int cenIndex = 0;
				//选择合适的中心
				for (int cenNo = 0; cenNo < K_Means.centroidList.size(); cenNo ++){
					double tempDistance = Classify.distance(docNo, K_Means.centroidList.get(cenNo));
					if (maxDouble < tempDistance){
						maxDouble = tempDistance;
						cenIndex = cenNo;
					}
				}
				//将文档加入新类中
				Relation.add(cenIndex, docNo);
			}
		}finally {
			Classify.STACK_Run.pop();
			System.err.print("S("+ arrayList.size()+")   ");
		}
		Thread.currentThread().interrupt();
	}
}
class MyThread_Record extends Thread{
	int fromIndex;
	int toIndex;
	public MyThread_Record(int from, int to){
		fromIndex = from;
		toIndex = to;
	}
	@Override
	public void run(){
		Classify.STACK_Record.add(false);
		for (int cenNo = fromIndex; cenNo <= toIndex; cenNo++){
			Centroid centroid = null;
			if (Relation.relation.size() <= cenNo || cenNo <0){
				continue;
			}
			ArrayList<Integer> alArrayList = Relation.relation.get(cenNo);
			File file = new File(AllPath.outputPath_Type +"/" +"END");
			if (!file.exists() || !file.isDirectory()){
				file.mkdir();
			}
			synchronized (alArrayList) {
				BufferedWriter bw;
				try {
					bw = new BufferedWriter(new FileWriter(AllPath.outputPath_Type +"/END" + "/TYPE " + cenNo, true));
					for (int i = 0; i < alArrayList.size(); i++){
						bw.write(alArrayList.get(i)+"\n");
					}
					bw.close();
					alArrayList.clear();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Classify.STACK_Record.pop();
		Thread.currentThread().interrupt();
		return;
	}
	
}












