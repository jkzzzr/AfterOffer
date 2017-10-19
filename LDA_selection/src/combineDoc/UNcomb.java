package combineDoc;

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
import java.util.Map.Entry;

/**
 * 主要是和Comb_docInType区别一下
 * 功能是：将每个文档都写成LDA输入的格式，根据输入结果列表
 * @author Administrator
 *
 */
public class UNcomb {
	
	public int qid;
	public HashSet<Integer> docidLList = new HashSet<Integer>();
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	
	public void init(String input) throws IOException{
		
		BufferedReader br1 = new BufferedReader(new FileReader(input));
		String line1 = "";
		while ((line1 = br1.readLine())!=null){
			if (line1.startsWith(this.qid + "")){
				StringTokenizer st = new StringTokenizer(line1);
				st.nextToken();//qid;
				st.nextToken();//Q0;
				int docid = Integer.parseInt(st.nextToken());//docid;
				this.docidLList.add(docid);
			}
		}
		br1.close();
	}
	
	/**
	 * 
	 * @param docidLList	所有文档docid列表
	 * @param outputPath	输出路径
	 * @param typeText	记录每一行对应的docid
	 * @throws IOException
	 */
	public void write(HashSet<Integer> docidLList, String outputPath, String typeText) throws IOException{
		File file1 = new File(outputPath + "/" + this.qid);
		if (!file1.exists() || !file1.isDirectory()){
			file1.mkdir();
		}
		File file2 = new File(typeText + "/" + this.qid);
		if (!file2.exists() || !file2.isDirectory()){
			file2.mkdir();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file1+"/doc.dat"));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(file2+"/context"));
		bw.write(docidLList.size()+"\n");
		for (Integer docno : docidLList){
			bw2.write(docno+"\n");
		
			HashMap<Integer, Integer> termMap = Create.getDocItemList(docno);
				//某些词出现多次，那就写出多次
			for (Entry<Integer, Integer> entry : termMap.entrySet()){
				int termId = entry.getKey();
				int termFrequency = entry.getValue();
				for (int j = 0; j < termFrequency; j++){
					bw.write(termId+"\t");
				}
			}
			//某个类别下的所有文档，所有词汇输出完结之后，再回车
			bw.write("\n");
		}
		bw.close();
		bw2.close();
		return;
	}
	
	
	public static void main(String[] args) throws IOException {
		UNcomb uNcomb = new UNcomb();
		for (int i = 251; i <=300; i++){
			uNcomb.setQid(i);
			uNcomb.init("E:\\2014query\\result\\样本文档-检索结果");
			uNcomb.write(uNcomb.docidLList, "E:\\2014query\\LDA-Single\\input"
					, "E:\\2014query\\LDA-Single\\input");
			System.out.println("qid:\t" + i);
		}
	}
}
