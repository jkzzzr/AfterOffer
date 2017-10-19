package test.filter.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeSet;

import filter.structure.Centroid;
import filter.structure.Data;

/**
 * 测试添加方法是否成功，意思就是，文档列表相同，那么就可以看作是同一个对象
 * @author lee
 *
 */
public class ClusterList {
	public static ArrayList<Centroid> cenlist = new ArrayList<Centroid>();
	/**
	 * 列表中，原始数据为(0+8+9)和（8+0+9）
	 * 通过for循环添加的是(0+8+9)=>(原始的0文档，和相似度最大的9和8，然后9和8又排序了，变成0+8+9)
	 * 然后将这个结果加入到列表中后，变成了2个
	 * @param args
	 */
	public static void main(String[] args) {
		Centroid centroid = new Centroid();
		centroid.doclist = new ArrayList<String>(){{add("0");add("8");add("9");}};
		Centroid centroid23 = new Centroid();
		centroid23.doclist = new ArrayList<String>(){{add("8");add("0");add("9");}};
		cenlist.add(centroid23);
		cenlist.add(centroid);
		
		for (int i = 0; i < cenlist.size(); i++){
			Centroid centroid22 = cenlist.get(i);
			for (String integer : centroid22.doclist){
				System.out.print(integer+"\t");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("添加一个对象到中心列表中");
		TreeSet<Data> ts = new TreeSet<Data>();
		for (int i = 0; i < 10; i++){
			Data data = new Data(i+"", i*100);
			ts.add(data);
			System.out.print(ts.size());
		}
		System.out.println();
		System.out.println("添加TreeSet");
		add(0+"", ts);
		System.out.println(cenlist.size());
		for (int i = 0; i < cenlist.size(); i++){
			Centroid centroid2 = cenlist.get(i);
			for (String integer : centroid2.doclist){
				System.out.print(integer+"\t");
			}
			System.out.println();
		}
		System.out.println();
	}
	public static void add(String docid, TreeSet<Data> ts){
		int K = 2;
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
			System.out.print("@@");
		}else {
			index = cenlist.indexOf(centroid);
			System.out.print("==");
		}
		System.out.println(index+"***");
	}
}
