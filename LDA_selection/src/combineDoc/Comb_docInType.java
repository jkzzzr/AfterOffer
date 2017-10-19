package combineDoc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import org.terrier.structures.postings.IterablePosting;

/**
 * 按文档所属类别合并所有文档
 * @author Administrator
 *
 */
public class Comb_docInType {

	public int qid;
	
	
	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	/**
	 * 初始化得到 - <类别 - list<类别下结果文档list>>
	 * @param input	原始检索结果
	 * @param typePath 所属类别的自定义文档
	 * @return
	 * @throws IOException
	 */
	public HashMap<Integer, ArrayList<Integer>> initType(String input, String typePath) throws IOException{
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
		br1.close();
		
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
				if (type_doclist.containsKey(type)){
					ArrayList<Integer> doc_list = type_doclist.get(type);
					doc_list.add(docid);
				}else {
					ArrayList<Integer> slArrayList= new ArrayList<Integer>();
					slArrayList.add(docid);
					type_doclist.put(type, slArrayList);
				}
			}	
		}
		br.close();
		return type_doclist;
	}
	
	/**
	 * 写出：格式为LDA输入格式：
	 * 	类别数量
	 * 类别下所有文档term  list
	 * @param hmHashMap<类别 - list<类别下结果文档list>>
	 * @param outputPath	
	 * @param typeText	对上面的输出文档的补充，记录，文档中每一行代表的是哪个类别
	 * @throws IOException
	 */
	public void write(HashMap<Integer, ArrayList<Integer>> hmHashMap, String outputPath, String typeText) throws IOException{
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
		bw.write(hmHashMap.size()+"\n");
		for (Integer integer : hmHashMap.keySet()){
			bw2.write(integer+"\n");
			ArrayList<Integer> docList = hmHashMap.get(integer);
			for (int i = 0; i < docList.size(); i++){
				int docno = docList.get(i);
				HashMap<Integer, Integer> termMap = Create.getDocItemList(docno);
				//某些词出现多次，那就写出多次
				for (Entry<Integer, Integer> entry : termMap.entrySet()){
					int termId = entry.getKey();
					int termFrequency = entry.getValue();
					for (int j = 0; j < termFrequency; j++){
						bw.write(termId+"\t");
					}
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
		Comb_docInType comb_docInType = new Comb_docInType();
		for (int i = 251; i <=300; i++){
			comb_docInType.setQid(i);
			HashMap<Integer, ArrayList<Integer>> hmHashMap = comb_docInType.initType("E:\\2014query\\result\\样本文档-检索结果", "E:\\2014query\\result\\全局-自定义");
			comb_docInType.write(hmHashMap, "E:\\2014query\\LDA\\input", "E:\\2014query\\LDA\\input");
			System.out.println("qid:\t" + i);
		}
	}

}
