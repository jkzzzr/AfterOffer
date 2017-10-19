import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


public class ComputeSim {

	public int qid;
	public int docNumber;
	public HashMap<Integer, ArrayList<Double>> type_ScoreList = new HashMap<Integer, ArrayList<Double>>();
	/**
	 * 返回<type - ArrayList<每个维度的得分>>
	 * @param inputPath	LDA ConText文件
	 * @param inputPath2	LDA主题相关度得分表（theta）
	 * @throws Exception
	 */
	public HashMap<Integer, ArrayList<Double>> init(String inputPath, String inputPath2) throws Exception{
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
		BufferedReader bReader2 = new BufferedReader(new FileReader(inputPath));
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
	
	/**
	 * 用KL散度，计算相似度，注意：KL散度，区分前项
	 * @param index1
	 * @param index2
	 * @return
	 */
	public double computeSim(int index1, int index2){
		ArrayList<Double> alArrayList1 = this.type_ScoreList.get(index1);
		ArrayList<Double> alArrayList2 = this.type_ScoreList.get(index2);
		double result = 0.0;
		for (int i = 0; i < alArrayList1.size(); i++){
			double score1 = alArrayList1.get(i);
			double score2 = alArrayList2.get(i);
			double tempScore = score1 * Math.log(score1 / score2);
			result +=tempScore;
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {

	}
	
	
	

}
