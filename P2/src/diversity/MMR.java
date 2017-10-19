package diversity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import similarity.Sim;

public class MMR extends A_implicit {

	private Sim sim = null;
	
	public MMR(int qid, String inputPath) {
		super(inputPath, qid);
		this.sim = new Sim(super.qid, super.inputPath);
	}
	@Override
	public ArrayList<Integer> div() {
		int K = super.alData_.size();
		//存储剩余文档，文档结果序号
		ArrayList<Integer> left_List = new ArrayList<Integer>();
		//存储结果列表的，文档结果序号
		ArrayList<Integer> result = new ArrayList<Integer>();
//		ArrayList<Data_> result = new ArrayList<Data_>();
		for (int i = 0; i < K; i++){
			left_List.add(i);
		}
		//选择出K个结果
		for (int i = 0; i < K; i++){
			double max_score = Double.MAX_VALUE;
			int max_index = 0;
			//每个结果，都是从剩余中找出最大得分的一个
			for (int j = 0; j < left_List.size(); j++){
				int index = left_List.get(j);
				double part1 = super.lambda * super.alData_.get(index).getScore();
				double part2 = (1 - super.lambda) * max(j, left_List);
				double temp_score = part1 + part2;
				if (max_score > temp_score){
					max_score = temp_score;
					max_index = j;
				}
			}
			result.add(left_List.get(max_index));
			left_List.remove(max_index);
			
			modify(left_List.get(max_index), i, max_index);
		}
		super.resultList = result;
		return result;
	}
	/**
	 * 原始结果中排名j的文档，与之相似度得分最低的,得分
	 * @param j
	 * @return
	 */
	public double max(int j, ArrayList<Integer> left_al){
		double maxscore = Double.MIN_VALUE;
		for (int i = 0; i < left_al.size(); i++){
			double tempSim = this.sim.getSim(i, j);
			if (maxscore < tempSim){
				maxscore = tempSim;
			}
		}
		return maxscore;
	}
	
	public void modify(int index, int order, double score){
		Data_ data_ = super.alData_.get(index);
		data_.setScore(score);
		data_.setOrder(order);
	}
	
	
	
	public void write(String outputPath) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
		for (int i = 0; i < super.resultList.size(); i++){
			int index = super.resultList.get(i);
			Data_ data_ = super.alData_.get(index);
			bw.write(data_.toString() + "\n");
		}
		bw.close();
	}
	
	
	
	public static void main(String args[]) throws IOException{
		//示例：
		MMR mmr = new MMR(0, "原始文档结果列表输入(docid)");
		mmr.div();
		mmr.write("输出路径");
	}
}
