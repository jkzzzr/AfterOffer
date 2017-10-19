package filter.compute;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Map.Entry;

import OriginData.DataInput_My;
import filter.structure.Centroid;
import filter.structure.ClusterList;
import filter.structure.Data;


public class Cluster {
	//每个类中包含的文档数量
	public static int THETA = 10;
	
	
	public int qid = 0;
	//已经计算出来的相似度结果
	public  ComputeSim csim = null;
	//文档列表
	public ArrayList<String> doclist = new ArrayList<String>();
	//****************************
	//结果存储在：：：
	//存储分类信息的类
	public ClusterList clusterList = null;
	
	public Cluster(int q){
		this.qid = q;
		csim = new ComputeSim(this.qid);
		doclist = DataInput_My.Input_QD.get(q);
		clusterList = new ClusterList(q);
	}
	
	/**
	 * 让所有文档找到他对应的类别，并初始化所有类,同时保存记录的距离别<br>
	 * 结果：1、将所有文档分好了，所属的类别，存放在：ClusterList.doc_CenMap  (类型是：HashMap<Integer, Integer>)<br>
	 *  	<tt> 2、记录下了所有类别<tt>、存放在：ClusterList.cenlist  (类型是：ArrayList<Centroid>)<br>
	 * 	<tt>3、记录下了所有文档相似度计算的得分<tt>、存放在：ComputeSim.simMap类型是：HashMap<String, Double> <br>
	 */
	public void run(){
		System.out.println("开始计算分类");
		//遍历所有文档
		int show = doclist.size()/10;
		System.out.print("\tQuery: "
				+ this.qid
				+ "：");
		for (int i = 0; i < doclist.size(); i++){
			if (i % show == 0){
				System.out.print("\t"+(10 - i/show));
			}
			String docitem = doclist.get(i);
			TreeSet<Data> datalist = new TreeSet<Data>();
			//每个文档要计算出它与其他文档之间的距离
			for (int j = 0; j < doclist.size(); j++){
				//不能包含自身
				if (j ==i){
					continue;
				}
				String docitem2 = doclist.get(j);
				Double sim = this.csim.getSim(docitem, docitem2);
				Data tempData = new Data(docitem2, sim);
				datalist.add(tempData);
			}
			this.clusterList.add(docitem, datalist);
		}
		System.out.println("\t计算分类结束!!!!");
		
	}
	public void write(String path){
		System.out.println("写入数据");
		try {
		File file = new File(path+"doc_cen");
		if (!file.exists()|| !file.isDirectory()){
			file.mkdir();
		}
		write1(path+"doc_cen");
		//*******************
		file = new File(path+"cenlist");
		if (!file.exists()|| !file.isDirectory()){
			file.mkdir();
		}
		write2(path+"cenlist");
		//**********************
		file = new File(path+"simmap");
		if (!file.exists()|| !file.isDirectory()){
			file.mkdir();
		}
		write3(path+"simmap");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\t写入数据结束!!");
	}
	/**
	 * 写DOC-CEN
	 * @param basePath
	 * @throws IOException
	 */
	private void write1(String basePath) throws IOException{
		//ClusterList.doc_CenMap
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(basePath+"/" +qid));
		System.out.println( this.clusterList.doc_CenMap.size());
		for (Entry<String,Integer> entry: this.clusterList.doc_CenMap.entrySet()){
			bWriter.write(entry.getKey()+"\t"+entry.getValue()+"\n");
		}
		bWriter.close();
	}
	/**
	 * 写cenList
	 * @param basePath
	 * @throws IOException
	 */
	private void write2(String basePath) throws IOException{
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(basePath+"/" +qid));
		System.out.println(this.clusterList.cenlist.size());
		for (int i = 0; i < this.clusterList.cenlist.size(); i++){
			Centroid centroid = this.clusterList.cenlist.get(i);
			bWriter.write(i+"\t"+centroid.toString()+"\n");
		}
		bWriter.close();
	}
	
	private void write3(String basePath) throws IOException{
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(basePath+"/" +qid));
		System.out.println(this.csim.simMap.size()+"\t"+basePath+"/" +qid);
		for (Entry<String,Double> entry: this.csim.simMap.entrySet()){
			bWriter.write(entry.getKey()+"\t"+entry.getValue()+"\n");
		}
		bWriter.close();
	}
}
