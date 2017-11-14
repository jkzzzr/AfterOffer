package myMethod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import diversity.Data_;

/**
 * 功能：给定几个质心和全集检索结果，返回这几个质心的全局多样化结果
 * 输入：选择的质心ID，原始的检索结果，原始检索结果中每个文档对应属于哪个质心-->全局自定义文档
 * 输出：写到文档中
 * @author Lee
 *
 */
public class Choose_MMR {
	//定义选择的文档总数
	public static boolean isWritePart = false;
	public static int counttt = 100;
	private  HashSet<Integer> chooseCentroid = new HashSet<Integer>();
	/**
	 * 存放几个类中的所有文档的检索结果
	 */
	private ArrayList<Data_> dataList = new ArrayList<Data_>();
	private int qid;
	
	public void init_chooseCen(int ...x){
		if (x.length == 0){
			return;
		}
		for (int i = 0; i < x.length; i++){
			chooseCentroid.add(x[i]);
		}
		return;
	}
	
	/**
	 * 输入：全局-自定义  路径
	 * 注：全局自定义结果样式为：qid  docid   origin-count原排名  count现在排名 type所属类别 score
	 * 返回this.qid下，对应type是我们需要的docid。这个docid在ChooseCentroid中定义
	 * @param inputPath
	 * @throws IOException
	 */
	public HashSet<Integer> init_dataList(String inputPath, String origin_path, String outputPath) throws IOException{
		if (isWritePart){
			writePart(inputPath, origin_path, outputPath);
		}
		
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
		String line = "";
		br.readLine();
		HashSet<Integer> docidSet = new HashSet<Integer>();
		int count = 0;
		while ((line = br.readLine())!=null){
			if (!line.startsWith(this.qid+"")){
				continue;
			}
		//	String [] strings = line.split("[\t| | *|( )*]");
			StringTokenizer st = new StringTokenizer(line);
			st.nextToken();
			int docc = Integer.parseInt(st.nextToken());
			st.nextToken();
			st.nextToken();
			int type = Integer.parseInt(st.nextToken());
			if (chooseCentroid.contains(type)){
				if (count > Choose_MMR.counttt){
					break;
				}
				count ++;
				docidSet.add(docc);
			}
		}
		br.close();
		return docidSet;
	}
	/**
	 * 
	 * @param inputPath  自定义文件
 	 * @param origin_path   原始检索结果
	 * @param outputPart  按照分类输出若干个文件
	 * @throws IOException
	 */
	public void writePart(String inputPath, String origin_path, String outputPart) throws IOException{
		String line = "";
		int count = 0;
		File dir = new File(outputPart + File.separator + this.qid);
		if (!dir.exists() || !dir.isDirectory()){
			dir.mkdirs();
		}
		HashMap<Integer, Integer> doc_typeMap = new HashMap<Integer, Integer>();	
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
		while ((line = br.readLine())!=null){
			if (!line.startsWith(this.qid+"")){
				continue;
			}
		//	String [] strings = line.split("[\t| | *|( )*]");
			StringTokenizer st = new StringTokenizer(line);
			st.nextToken();
			int docc = Integer.parseInt(st.nextToken());
			st.nextToken();
			st.nextToken();
			int type = Integer.parseInt(st.nextToken());
			if (this.chooseCentroid.contains(type)){
				if (count > Choose_MMR.counttt){
					break;
				}
				count ++;
				doc_typeMap.put(docc, type);
			}
		}
		br.close();
		
		BufferedReader br2 = new BufferedReader(new FileReader(origin_path));
		
		String line2 = "";
		br2.readLine();
		int order = 0;
		while ((line2 = br2.readLine())!=null){
			if (!line2.startsWith(this.qid+"")){
				continue;
			}
			String [] strings = line2.split("[\t| ]");
			int docid = Integer.parseInt(strings[2]);
			if (doc_typeMap.containsKey(docid)){
				Data_ data_ = new Data_(line2);
				data_.setOrder(order++);
				int temptype = doc_typeMap.get(docid);
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputPart + File.separator + this.qid+File.separator+temptype, true));
				bw.write(data_.toString() + "\n");
				bw.close();
			}
		}
		br.close();
		br2.close();
	}
	
	
	/**
	 * 需要挑选出的文档id, 原始全文检索结果， 
	 * @param docidSet
	 * @param origin_path	原始全文检索结果
	 * @param outputPath
	 * @throws IOException
	 */
	public void choose(HashSet<Integer> docidSet, String origin_path, String outputPath) throws IOException{
		File file = new File(outputPath);
		if (!file.exists()){
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath, true));
		BufferedReader br = new BufferedReader(new FileReader(origin_path));
		String line = "";
		br.readLine();
		int order = 0;
		while ((line = br.readLine())!=null){
			if (!line.startsWith(this.qid+"")){
				continue;
			}
			String [] strings = line.split("[\t| ]");
			int docid = Integer.parseInt(strings[2]);
			if (docidSet.contains(docid)){
				Data_ data_ = new Data_(line);
				data_.setOrder(order++);
				bw.write(data_.toString()+"\n");
			}
		}
		br.close();
		bw.close();
		return;
	}
	public void clear(int qi){
		this.qid = qi;
		this.chooseCentroid.clear();
		this.dataList.clear();
	}
	
	public static void main(String[] args) throws IOException {
		Choose_MMR choose_MMR = new Choose_MMR();
		for (int i = 151; i<=200; i++){
			System.out.println("当前进度：\tqid:" +i);
			choose_MMR.clear(i);
			//初始化选择的集合
			choose_MMR.init_chooseCen(1,2,3);
			//需要全局自定义文档，告诉我们，哪些文档是包含在选择的集合中的
			HashSet<Integer> dataSet = choose_MMR.init_dataList("E:\\实验数据\\全局-自定义", "", "");
			//从原始的全文检索结果中，挑出选择的文档
			choose_MMR.choose(dataSet, "E:\\实验数据\\123.res", "E:\\实验数据\\choose.res");
		}
	}

}
