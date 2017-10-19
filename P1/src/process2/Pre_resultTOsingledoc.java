package process2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import compute.Com_Sim_K_means;

import structure.Base;
import structure.Create;
import structure.SingleDoc;
/**
 * 要做的就是，给一个结果列表，返回一个singledoc列表
 * @author Administrator
 *
 */
public class Pre_resultTOsingledoc {
//	private ArrayList<Base> alist = new ArrayList<Base>();
	
	/**使用示例：
	 * 
	 * @param argsStrings
	 */
	public static void main(String []argsStrings){
		int qid = 0;
		String inputPath = "原始排序结果位置(docid)";
		
		//初始化数据：将结果文档，结合索引，得到文档singleDoc信息，方便后续计算
		Pre_resultTOsingledoc p = new Pre_resultTOsingledoc();
		ArrayList<Integer> alArrayList = new ArrayList<Integer>();
		ArrayList<Base> result = new ArrayList<Base>();
		try {
			alArrayList = p.init_al_int(inputPath, qid);
			result = p.init_al_singledoc(alArrayList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//计算
		Com_Sim_K_means csk = new Com_Sim_K_means(qid, true);
		csk.allDocs = result;
		//具体执行
		csk.getSim(0, 0);
	}
	
	
	
	
	public static ArrayList<Base> run(String inputPath, int qid){
		Pre_resultTOsingledoc p = new Pre_resultTOsingledoc();
		ArrayList<Integer> alArrayList;
		ArrayList<Base> result;
		try {
			alArrayList = p.init_al_int(inputPath, qid);
			result = p.init_al_singledoc(alArrayList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * 输入文件为(排序次序  文档数字ID)
	 * @param inputPath
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Integer> init_al_int(String inputPath, int qid) throws Exception{
		ArrayList<Integer> result = new ArrayList<Integer>();
		BufferedReader br = new BufferedReader(new FileReader(inputPath));
		String line = "";
		while ((line = br.readLine())!=null){
			String [] strings = line.split("[\t| ]");
			int no = Integer.parseInt(strings[0]);
			/*if (no < qid){
				continue;
			}else if (no > qid){
				break;
			}*/
			if (no !=qid){
				continue;
			}
			int docid = Integer.parseInt(strings[1]);
			result.add(docid);
		}
		return result;
	}
	/**
	 * 输入docno序号，输出文档singledoc信息。没有的话，对应位置为null
	 * @param al
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Base> init_al_singledoc(ArrayList<Integer> al) throws Exception{
		Create create = new Create();
		HashMap<Integer, Base> hm = create.init(new HashSet<Integer>(al));
		create.destory();
		ArrayList<Base> result = new ArrayList<Base>();
		for (int i = 0; i < al.size();i++){
			int index = al.get(i);
			if (hm.containsKey(index)){
				result.add(hm.get(index));
			}else {
				result.add(null);
			}
		}
		return result;
	}
	
	
	
}
