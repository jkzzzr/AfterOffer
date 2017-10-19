package filter.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import filter.compute.Cluster;

public class ClusterList {

	public int qid = 0;
	public ArrayList<Centroid> cenlist = new ArrayList<Centroid>();
	public HashMap<String, Integer> doc_CenMap = new HashMap<String, Integer>();
	public ClusterList(int q){
		this.qid = q;
	}
	/**
	 * 将文档对应的最近的列表添加进来，在这个函数中计算出它所属的类别
	 * @param docid
	 * @param ts
	 */	
	public void add(String docid, TreeSet<Data> ts){
		int K = Cluster.THETA - 1;
		Centroid centroid = new Centroid();
		ArrayList<String> al = new ArrayList<String>();
		//把自己加进去！
		al.add(docid);
		Iterator<Data> it = ts.iterator();
		for (int i = 0 ; i < K; i++){
			Data data = it.next();
			String tempdoc = data.docid;
			//防止重复的了
			if (!al.contains(tempdoc)){
				al.add(tempdoc);
			}else {
				i--;
			}
		}
		//给docid列表按英文字母从小到大的排序，这样方便下面的cenList的contains识别
		Collections.sort(al);
		centroid.doclist = al;
		int index = 0;
		if (!cenlist.contains(centroid)){
			index = cenlist.size();
			cenlist.add(centroid);
		}else {
			index = cenlist.indexOf(centroid);
		}
		doc_CenMap.put(docid, index);
	}
	
}
