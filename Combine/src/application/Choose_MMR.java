package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import structure.Data_;

/**
 * 给定几个质心和全集检索结果，返回这几个质心的全局多样化结果
 * @author Administrator
 *
 */
public class Choose_MMR {

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
	 * 返回this.qid下，对应type是我们需要的docid
	 * @param inputPath
	 * @throws IOException
	 */
	public HashSet<Integer> init_dataList(String inputPath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
		String line = "";
		br.readLine();
		HashSet<Integer> docidSet = new HashSet<Integer>();
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
				docidSet.add(docc);
			}
		}
		br.close();
		return docidSet;
	}
	/**
	 * 需要挑选出的文档id, 原始全文检索结果， 
	 * @param docidSet
	 * @param origin_path
	 * @param outputPath
	 * @throws IOException
	 */
	public void choose(HashSet<Integer> docidSet, String origin_path, String outputPath) throws IOException{
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
			int type = Integer.parseInt(strings[2]);
			if (docidSet.contains(type)){
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
			choose_MMR.init_chooseCen(1,2,3);
			HashSet<Integer> dataSet = choose_MMR.init_dataList("E:\\实验数据\\全局-自定义");
			choose_MMR.choose(dataSet, "E:\\实验数据\\123.res", "E:\\实验数据\\choose.res");
		}
	}

}
