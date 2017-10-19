package test.filter.compute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import filter.structure.Data;

public class Cluster {

	public static void main(String args[]){
		new Cluster().run();
	}
	/**
	 * 调试了这个TreeSet是否有用
	 */
	public void run(){
		ArrayList<String> doclist = new ArrayList<String>(){{
			add("11");
			add("12");
			add("13");
			add("14");
			add("15");
			add("16");
			add("17");
			add("18");
			
		}};
		for (int i = 0; i < doclist.size(); i++){
			String docitem = doclist.get(i);
			TreeSet<Data> datalist = new TreeSet<Data>();
			//每个文档要计算出它与其他文档之间的距离
			for (int j = 0; j < doclist.size(); j++){
				//不能包含自身
				if (j ==i){
					continue;
				}
				String docitem2 = doclist.get(j);
				Double sim = 34.0;
				Data tempData = new Data(docitem2, sim);
				datalist.add(tempData);
				System.out.println(docitem2+"\t"+sim+"\t"+datalist.size());
			}
			Iterator<Data> it = datalist.iterator();
			while(it.hasNext()){
				Data data = it.next();
				System.out.println(data.score+"==="+data.docid );
			}
			System.out.println("END");
			System.exit(1);
		}
	}
}
