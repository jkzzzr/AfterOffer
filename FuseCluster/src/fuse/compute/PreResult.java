package fuse.compute;

import java.util.ArrayList;
import java.util.HashMap;

import filter.structure.Centroid;

public class PreResult {
	/**
	 * 上接：查询下：
	 * 			1、q - 分类信息
	 * 			2、q - d1 - d2 - score
	 * 			3、q - 
	 */
	public static HashMap<Integer, ArrayList<Centroid>> Q_CentrMap = new HashMap<>();
	public static HashMap<Integer, HashMap<String, HashMap<String, Double>>> Q_SimMap = 
			new HashMap<>();
	//文档列表信息从上面这个第二个的keyset得到
/*	*//**
	 * 中途还需要：
	 * 			1、q - docid - 融合原始分数
	 *//*
	public static HashMap<Integer, HashMap<Integer, Double>> Q_OriginScoreMap = null;*/
	
}
