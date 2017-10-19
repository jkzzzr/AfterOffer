package diversity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import compute.Com_Sim_K_means;
import structure.Base;
import structure.Centroid;

public class MMR_totalType {

	private static final double lambda = 0.5;
	private Com_Sim_K_means com_sim_k_means= new Com_Sim_K_means();
	/**
	 * 由每个type包含哪些类，创建质心对象
	 */
	private HashMap<Integer, Base> typeList = new HashMap<Integer, Base>();
	
	/**
	 * 记录每个type的得分
	 */
	private HashMap<Integer, Double> type_score= new HashMap<Integer, Double>();
	
	private int qid = 0;
	private String inputPath = "";
	private String typePath = "";
	public ArrayList<Integer> resultTypeList = new ArrayList<Integer>();
	
	public MMR_totalType(String input, String typePP){
		this.inputPath = input;
		this.typePath = typePP;
	}
	public MMR_totalType(int qid, String inputPath, String typePath) throws IOException {
		this.qid = qid;
		initType(inputPath, typePath);
		com_sim_k_means.allDocs = new ArrayList<Base>(typeList.values());
		com_sim_k_means.qid = this.qid;
	}
	
	/**
	 * 
	 * @param typePath	自定义文件信息
	 * @throws IOException 
	 */
	public void initType(String input, String typePath) throws IOException{
		HashSet<Integer> docidLList = new HashSet<Integer>();
		BufferedReader br1 = new BufferedReader(new FileReader(input));
		String line1 = "";
		while ((line1 = br1.readLine())!=null){
			if (line1.startsWith(this.qid + "")){
				StringTokenizer st = new StringTokenizer(line1);
				st.nextToken();//qid;
				st.nextToken();//Q0;
				int docid = Integer.parseInt(st.nextToken());//docid;
				docidLList.add(docid);
			}
		}
		
		
		//类别-包含文档的list
		HashMap<Integer, ArrayList<Integer>> type_doclist = new HashMap<Integer, ArrayList<Integer>>();
		BufferedReader br = new BufferedReader(new FileReader(typePath));
		String line = "";
		while ((line = br.readLine())!=null){
			if (line.startsWith(this.qid + "")){
				StringTokenizer st = new StringTokenizer(line);
				st.nextToken();//qid;
				int docid = Integer.parseInt(st.nextToken());
				if (!docidLList.contains(docid)){
					continue;
				}
				
				
				st.nextToken();//origin-count;
				st.nextToken();//count;
				int type = Integer.parseInt(st.nextToken());//type
				double score = Double.parseDouble(st.nextToken());
				if (type_doclist.containsKey(type)){
					ArrayList<Integer> doc_list = type_doclist.get(type);
					doc_list.add(docid);
					double originscore = type_score.get(type);
					type_score.replace(type, originscore + score);
				}else {
					ArrayList<Integer> slArrayList= new ArrayList<Integer>();
					slArrayList.add(docid);
					type_doclist.put(type, slArrayList);
					type_score.put(type, score);
				}
			}	
		}
		
		for (Entry<Integer, ArrayList<Integer>> entry: type_doclist.entrySet()){
			ArrayList<Integer> alist = entry.getValue();
			Centroid centroid = new Centroid(alist);
			this.typeList.put(entry.getKey(), centroid);
		}
		this.Normalize();
		
		
		
		br.close();
	}
	
	
	public ArrayList<Integer> div() {
		int K = this.typeList.size();
		ArrayList<Integer> tempList = new ArrayList<Integer>(this.typeList.keySet());
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
			double max_score = -Double.MAX_VALUE;
			int max_index = 0;
			//每个结果，都是从剩余中找出最大得分的一个
			for (int j = 0; j < left_List.size(); j++){
				int index = left_List.get(j);
				index = tempList.get(index);
				double score = type_score.get(index);
				score = Math.sqrt(score);
				double part1 = lambda * score;
				double part2 = (1 - lambda) * max(j, left_List);
				double temp_score = part1 - part2;
				if (max_score < temp_score){
					max_score = temp_score;
					max_index = j;
				}
			}
			result.add(tempList.get(left_List.get(max_index)));
			this.type_score.replace(tempList.get(left_List.get(max_index)), max_score);
			left_List.remove(max_index);
			
	//		modify(left_List.get(max_index), i, max_index);
		}
		this.resultTypeList = result;
		return result;
	}
	
	/**
	 * 原始结果中排名j的文档，与之相似度得分最低的,得分
	 * @param j
	 * @return
	 */
	public double max(int j, ArrayList<Integer> left_al){
		double maxscore = -Double.MAX_VALUE;
		for (int i = 0; i < left_al.size(); i++){
			double tempSim = this.com_sim_k_means.getSim(left_al.get(i), left_al.get(j));
			if (maxscore < tempSim){
				maxscore = tempSim;
			}
		}
		return maxscore;
	}

	
	
/*	public void modify(int index, int order, double score){
		Data_ data_ = super.alData_.get(index);
		data_.setScore(score);
		data_.setOrder(order);
	}*/
	
	public void Normalize(){
		double maxscore = Collections.max(this.type_score.values());
		double minscore = Collections.min(this.type_score.values());
		double fenmu = maxscore - minscore;
		fenmu /= 2; //规范化到0-2之间
		for (Integer index : this.type_score.keySet()){
			double score = this.type_score.get(index);
			score = (score - minscore) / fenmu;
			this.type_score.replace(index, score);
		}
	}
	
	
	public void write(String outputPath) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath, true));
		for (int i = 0; i < this.resultTypeList.size(); i++){
			//类别
			int index = this.resultTypeList.get(i);
			double score = this.type_score.get(index);
			Data_ data_ = new Data_(this.qid, index, i, score);
			bw.write(data_ +"\n");
		}
		bw.close();
	}
	
	public void clear(int i ) throws IOException{
		this.type_score.clear();
		this.typeList.clear();
		this.com_sim_k_means = new Com_Sim_K_means(i, true);
		this.qid = i;
		initType(this.inputPath, this.typePath);
		com_sim_k_means.allDocs = new ArrayList<Base>(typeList.values());
		com_sim_k_means.qid = this.qid;
	}
	
	public static void main(String args[]) throws IOException{
		//示例：

		MMR_totalType mmr = new MMR_totalType("原始文档结果列表输入(docid)","自定义文件（最好是全文的）");
		for (int i = 251; i < 200; i++){
			mmr.clear(i);
			mmr.div();
			mmr.write("输出路径");
		}
	}
}
