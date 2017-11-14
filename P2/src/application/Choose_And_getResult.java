package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import myMethod.Choose_Centroid;
import myMethod.Choose_MMR;

/**
 * 小文档方法：
 *     1：选择合适的质心-->Choose_Centroid
 *     2：到原始结果列表中，挑选出对应的文档，得到新的结果列表-->Choose_MMR
 * @author Lee
 *
 */
public class Choose_And_getResult {

	public static int choose_num = 10;
	
	
	public static int year = 2013;
 	static String quanJuZiDingyi = "E:\\2013query\\全局-自定义";
	static String originPath = "E:\\2013query\\all.index.adhoc";
	static String outputPath = "E:\\2013query\\individual."+ choose_num+ ".choose2";
	static String sampleMMR = "E:\\2013query\\样本文档-检索结果.mmr";
	
/*	public static int year = 2014;
	static String quanJuZiDingyi = "E:\\2014query\\result-原始，还没多样化的\\全局-自定义";
	static String originPath = "E:\\2014query\\DPH_1.res.index.adhoc ";
	static String outputPath = "E:\\2014query\\individual." + choose_num+ ".choose2";
	static String sampleMMR = "E:\\"+ "2014"+ "query\\样本文档mmr";*/
	
	
	public static void main(String[] argsStrings) throws Exception{
		Choose_MMR.isWritePart = true;
		System.out.println("WTF1");
		Choose_Centroid choose_Centroid = new Choose_Centroid();
		Choose_MMR choose_MMR = new Choose_MMR();
		System.out.println("WTF2");
		int startQid = (year - 2009) *50 +1;
		int endQid = (year - 2008) * 50;
		for (int i = startQid; i <= endQid; i++){
			System.out.println("qid\t" + i);
			choose_Centroid.clear(i);
			ArrayList<Integer> sample_Result = choose_Centroid.init_result(sampleMMR);
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
			
			HashSet<Integer> dataSet = choose_MMR.init_dataList(quanJuZiDingyi
					, originPath
					, outputPath);
			choose_MMR.choose(dataSet, originPath, outputPath+".file");
		}
	}
}
