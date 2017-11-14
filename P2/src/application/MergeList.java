package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;


import diversity.Data_;

public class MergeList {

	public int qid = 0;
	public HashSet<ArrayList<Data_>> originSet = new HashSet<ArrayList<Data_>>();
	public ArrayList<Data_> resultList = new ArrayList<Data_>();
	 
	public static void main(String[] args) {
		run(2014, "E:\\2014query\\individual.10.choose2", "E:\\2014query\\individual.10.choose2\\mergeList."+2014);
//		run(2013, "E:\\2013query\\individual.10.choose2", "E:\\2013query\\individual.10.choose2\\mergeList."+2013);
	}
	
	
	public static void run(int year, String inputFoler, String outputPath) {
		int startQid = (year - 2009) * 50 +1;
		int toQid = (year -2009 + 1) * 50;
		MergeList mergeList = new MergeList();
		for (int tempQid = startQid; tempQid <= toQid; tempQid++){
			mergeList.clear(tempQid);
			try {
				mergeList.read(inputFoler);
				mergeList.merge1();
				mergeList.write(outputPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void write(String outputPath) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath, true));
		for (int i = 0;i < resultList.size(); i++){
			bw.write(resultList.get(i).toString()+"\n");
		}
		bw.close();
	}
	
	public void clear(int tempQ){
		this.qid = tempQ;
		this.originSet.clear();
		this.resultList.clear();
	}
	
	/**
	 * 
	 * @param inputFolder  文件夹名字，这个文件夹下面存储了若干文档，文档名为type，内容为对应type下的排序列表
	 * @return  HashSet<ArrayList<Data_>>
	 * @throws IOException
	 */
	public void read(String inputFolder) throws IOException{
		System.out.println("!!!\t"+this.qid);
		HashSet<ArrayList<Data_>> result = new HashSet<ArrayList<Data_>>();
		
		HashSet<Integer> testSet = new HashSet<Integer>();
		
		File file = new File(inputFolder + File.separator + this.qid);
		File[] files = file.listFiles();
		for (File tempFile : files){
			ArrayList<Data_> tempList = new ArrayList<Data_>();
			BufferedReader br = new BufferedReader(new FileReader(tempFile));
			String line = "";
			while ((line = br.readLine())!=null){
				Data_ data_ = new Data_(line);
				tempList.add(data_);

				if (testSet.contains(data_.getDocid())){
					System.err.println("!!!\t"+data_.getDocid()+"\t"+tempFile.getName());
				}else {
					testSet.add(data_.getDocid());
				}
			}
			result.add(tempList);
		}
		this.originSet = result;
		return ;
	}
	
	/**
	 *  规则：原始分数按照对数计算--> 1- 0.2 log(r + 1)  (递增：排名越高分数越好)
	 *      每个分类下面的文档数量N，作为参数log(N+1)  (递增：数量越多分数越高)
	 *      
	 * @param originSet
	 * @return
	 */
	public void merge1(){
		ArrayList<Data_> result = new ArrayList<Data_>();
		TreeSet<Data_> ts = new TreeSet<Data_>(new Comparator<Data_>() {
			@Override
			public int compare(Data_ o1, Data_ o2) {
				int result = o1.getScore() > o2.getScore() ? 1: o1.getScore() < o2.getScore() ? -1 :0;
				return result;
			}
		});
		for (ArrayList<Data_> templist : this.originSet){
			for (int i = 0; i < templist.size(); i++){
				Data_ tempdData_ = templist.get(i);
				tempdData_.setOrder(i);
				tempdData_.setScore(normalize(i) * Math.log(templist.size() + 1));
				ts.add(tempdData_);
			}
		}
		Iterator<Data_> it = ts.iterator();
		while (it.hasNext()){
			result.add(it.next());
		}
		this.resultList = result;
		return ;
	}

	private double normalize(int i) {
		double result = 1 - 0.2 * Math.log1p(i + 1);
		return result;
	}

}
