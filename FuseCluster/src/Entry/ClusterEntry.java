package Entry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import Err.WriteErr;
import Input.c1;
import filter.compute.Cluster;
import filter.compute.ComputeSim;
import filter.structure.ClusterList;

/**
 * 将所有文档分类的入口
 * @author lee
 *
 */
public class ClusterEntry {

	/**
	 * 让所有文档找到他对应的类别，并初始化所有类,同时保存记录的距离别<br>
	 * 结果：1、将所有文档分好了，所属的类别，存放在：ClusterList.doc_CenMap  (类型是：HashMap<Integer, Integer>)<br>
	 *  	<tt> 2、记录下了所有类别<tt>、存放在：ClusterList.cenlist  (类型是：ArrayList<Centroid>)<br>
	 * 	<tt>3、记录下了所有文档相似度计算的得分<tt>、存放在：ComputeSim.simMap类型是：HashMap<String, Double> <br>
	 */
	public static void main(String args[]){
		System.out.println("初始化所有数据开始");
		int year = FuseEntry.year;
		c1.main("/home/lee/DATA/data/new-"+year, "/home/lee/音乐/WordList_perDoc_Path");
		System.out.println("初始化所有数据结束");
		int qid = 1;
		int first = (year-2009) * 50 +1;
		int end = (year - 2009) * 50 +50;
		for (int i = first; i <=end; i ++){
			Cluster cluster = new Cluster(i);
			cluster.run();
			cluster.write("/home/lee/音乐/output-2/");
		}
		
		WriteErr.writeErr("/home/lee/DATA/data/err-nullKey");
		
		
		
	}

}
