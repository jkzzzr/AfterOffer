package fuse.compute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import OriginData.DataInput_My;
import fuse.structure.ScoreBuilder;

/**
 * 结果：
 * 			1、将查询对应的所有文档的得分计算出来，存放在：result中，数据类型：HashMap<Integer,Double>
 * @author lee
 *
 */
public class ClusterFuse {

	public static double lambda = 0.6;
	private Integer qid = 0;
	//所有待计算的文档
	private ArrayList<String> doclist = new ArrayList<String>();
	//文档的融合分数<br>doc - score
	private HashMap<String, Double> fusescore_Origin = new HashMap<String, Double>();
	//所有融合分数之和
	private double pdq_2 = -1;
	//pcq分数<br>collection - score
	private ArrayList<Double> pcq_ = null;
	//结果列表
	public HashMap<String, Double> result = new HashMap<String, Double>();
	public ClusterFuse(int qid){
		init(qid);
	}
	public void init(Integer qid){
		this.qid = qid;
		doclist = DataInput_My.Input_QD.get(qid);
		fusescore_Origin = ScoreBuilder.getFusescore_Origin(this.qid);
		//计算pdq_2
		double temp = 0.0;
		for (Entry<String, Double> entry: fusescore_Origin.entrySet()){
			temp +=entry.getValue();
		}
		pdq_2 = temp;
		pcq_ = ScoreBuilder.getPcq(this.qid);
		
	}
	public void run(){
		for (int i= 0; i < doclist.size(); i++){
			String docid = doclist.get(i);
			double part1 = (1 - ClusterFuse.lambda) * compute_DQ(docid);
			double part2 = ClusterFuse.lambda * compute_Part2(docid);
			double score = part1 + part2;
	//		System.out.println(part1+"\t"+part2);
			result.put(docid, score);
		}
	}
	private double compute_DQ(String docid) {
		double fusescore = fusescore_Origin.get(docid);
		double fenmu = this.pdq_2 ;
		double result = fusescore / fenmu;
		return result;
	}
	private double compute_Part2(String docid) {
		ArrayList<Double> tempPdc = ScoreBuilder.getPdc(qid).get(docid);
		double result = 0.0;
		for (int collectionNo = 0; collectionNo < tempPdc.size(); collectionNo++){
			double score1 = pcq_.get(collectionNo);
			double score2 = tempPdc.get(collectionNo);
			double tempresult = score1 * score2;
			result +=tempresult;
		}
		return result;
	}
}
