package similarity;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


public class ComputeSim {

	public static String INPATH1 = "E:\\2014query\\LDA\\input";
	public static String INPATH2 = "E:\\2014query\\LDA\\input";
	public int qid;
	public int docNumber;
	public HashMap<Integer, ArrayList<Double>> type_ScoreList = new HashMap<Integer, ArrayList<Double>>();
	
	public HashMap<Integer, HashMap<Integer, Double>> sCOREMap = new HashMap<Integer, HashMap<Integer,Double>>();
	
	
	
	public void clear(int i){
		this.qid = i;
		this.type_ScoreList.clear();
		sCOREMap.clear();
		try {
			this.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回<type - ArrayList<每个维度的得分>>
	 * @param inputPath	LDA ConText文件
	 * @param inputPath2	LDA主题相关度得分表（theta）
	 * @throws Exception
	 */
	public HashMap<Integer, ArrayList<Double>> init() throws Exception{
		String inputPath = INPATH1 + "/" + this.qid +"/context";
		String inputPath2 = INPATH2 + "/"+ this.qid +"/model-final.theta";
		//读取类别说明文档
		BufferedReader bReader = new BufferedReader(new FileReader(inputPath));
		ArrayList<Integer> typeList = new ArrayList<Integer>();
		String line= "";
		while ((line = bReader.readLine())!=null){
			int tempType = Integer.parseInt(line);
			typeList.add(tempType);
		}
		this.docNumber = typeList.size();
		
		HashMap<Integer, ArrayList<Double>> result = new HashMap<Integer, ArrayList<Double>>();
		//读取theta表
		BufferedReader bReader2 = new BufferedReader(new FileReader(inputPath2));
		line = "";
		int tempDocNumber = -1;
		while ((line = bReader2.readLine())!=null){
			StringTokenizer st = new StringTokenizer(line);
			tempDocNumber ++;
			ArrayList<Double> scoreList = new ArrayList<Double>();
			while (st.hasMoreTokens()){
				double score = Double.parseDouble(st.nextToken());
				scoreList.add(score);
			}
			int key = typeList.get(tempDocNumber);
			result.put(key, scoreList);
		}
		this.type_ScoreList = result;
		return result;
		
		
	}
	
	public double getSim(int index1, int index2){
		if (this.sCOREMap.containsKey(index1)){
			HashMap<Integer, Double> tempHashMap = this.sCOREMap.get(index1);
			if (tempHashMap.containsKey(index2)){
				return tempHashMap.get(index2);
			}else {
				double result = this.computeSIM_cos(index1, index2);
				tempHashMap.put(index2, result);
				return result;
			}
		}else {
			HashMap<Integer, Double> tempHashMap = new HashMap<Integer, Double>();
			double result = this.computeSIM_cos(index1, index2);
			tempHashMap.put(index2, result);
			this.sCOREMap.put(index1, tempHashMap);
			return result;
		}
	}
	
	
	/**
	 * 用KL散度，计算相似度，注意：KL散度，区分前项
	 * @param index1
	 * @param index2
	 * @return
	 */
	private double computeDISTANCE_KL(int index1, int index2){
		
		ArrayList<Double> alArrayList1 = this.type_ScoreList.get(index1);
		ArrayList<Double> alArrayList2 = this.type_ScoreList.get(index2);
		double result = 0.0;
		for (int i = 0; i < alArrayList1.size(); i++){
			double score1 = alArrayList1.get(i);
			double score2 = alArrayList2.get(i);
			if (score1 > score2 *100||score2 > score1 * 100){
				continue;
			}
			double tempScore = score1 * Math.log(score1 / score2);
			result +=tempScore;
		}
		return result;
	//	return 1- result / 10;
	}
	
	/**
	 * 用cos相似度，计算相似度
	 * @param index1
	 * @param index2
	 * @return
	 */
	private double computeSIM_cos(int index1, int index2){
		
		ArrayList<Double> alArrayList1 = this.type_ScoreList.get(index1);
		ArrayList<Double> alArrayList2 = this.type_ScoreList.get(index2);
		double temp1 = 0.0;
		double temp2 = 0.0;
		double fenzi = 0.0;
		for (int i = 0; i < alArrayList1.size(); i++){
			double score1 = alArrayList1.get(i);
			double score2 = alArrayList2.get(i);
			fenzi += score1 * score2;
			temp1 += score1 * score1;
			temp2 += score2 * score2;
		}
		double result = fenzi / Math.sqrt(temp1 * temp2);
		return result;
	//	return 1- result / 10;
	}
	
	
	public static void main(String[] args) {

	}
	
	
	

}
