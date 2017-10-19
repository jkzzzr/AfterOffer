package diversity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class A_implicit {
	/**
	 * 结果文档，取百分之多少的文档应用读取
	 * 注意：特别的在样本文档结果中，可能会取百分之八十，因为后面的文档，可能是无关文档
	 */
	public static double percent = 1.0;
	
	
	
	
	public double lambda = 0.5;
	public String inputPath;
	public A_implicit(String inputPath, int qid) {
		try {
			init(inputPath, qid);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.inputPath = inputPath;
		this.qid = qid;
	}

	public int qid;
	//初时：记录原始结果文档中，每一行的数据
	//后来：将其中的得分和次序置换掉
	public ArrayList<Data_> alData_ = new ArrayList<Data_>();
	public ArrayList<Integer> resultList = new ArrayList<Integer>();
	/**
	 * 多样化算法
	 */
	public abstract ArrayList<Integer> div();
	
	/**
	 * 初始化原始结果文档
	 * @throws IOException 
	 */
	public ArrayList<Data_> init(String inputPath, int qid) throws IOException{
		this.alData_.clear();
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
		String line = "";
		while ((line = br.readLine())!=null){
			String []strings = line.split("[\t|\n|\r| ]");
			int tempqid = Integer.parseInt(strings[0]);
			if (tempqid < qid){
				continue;
			}else if (tempqid == qid){
				alData_.add(new Data_(line));
			}else {
				break;
			}
		}
		
		br.close();
		
		alData_ = (ArrayList<Data_>) alData_.subList(0, (int) (alData_.size() * A_implicit.percent));
		
		
		
		System.out.println("diversity.A_implicit.init(String, int)\t初始化结果文档结束 - 文档大小为：" + alData_.size());
		return this.alData_;
	}
}
