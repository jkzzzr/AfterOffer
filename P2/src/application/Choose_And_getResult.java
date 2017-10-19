package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import myMethod.Choose_Centroid;
import myMethod.Choose_MMR;

/**
 * 小文档方法
 * @author Lee
 *
 */
public class Choose_And_getResult {

	public static int choose_num = 10;
	public static void main(String[] argsStrings) throws Exception{{
		System.out.println("WTF1");
		Choose_MMR choose_MMR = new Choose_MMR();
		Choose_Centroid choose_Centroid = new Choose_Centroid();
		System.out.println("WTF2");
		for (int i = 251; i <= 300; i++){
			System.out.println("qid\t" + i);
			choose_Centroid.clear(i);
			ArrayList<Integer> sample_Result = choose_Centroid.init_result("E:\\2014query\\样本文档mmr");
			HashMap<Integer, Integer> typeSet = choose_Centroid.init_type
					("E:\\实验数据\\FR-数据\\分类信息-样本文档\\sample-序列化结果");
			choose_Centroid.count(sample_Result, typeSet);
			int [] a = choose_Centroid.choose(choose_num);
			
			System.out.print("当前进度：\tqid:" +i+"\t");
			for (int ij = 0; ij < a.length; ij++){
				System.out.print(a[ij]+"\t");
			}
			System.out.println();
			choose_MMR.clear(i);
			choose_MMR.init_chooseCen(a);
			HashSet<Integer> dataSet = choose_MMR.init_dataList("E:\\2014query\\result-原始，还没多样化的\\全局-自定义");
			choose_MMR.choose(dataSet, "E:\\2014query\\DPH_1.res.index.adhoc ", "E:\\2014query\\individual."
					+ choose_num
					+ ".choose");
		}
	}
	}
}
