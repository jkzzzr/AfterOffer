package my;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * 除了分数规范化，还要处理冗余问题，还要处理，query顺序错误问题
 * @author Administrator
 *
 */
public class Normalize {

	public static void main(String[] args) throws Exception {
		String path_inputDirectory = "F:\\Fusion\\2011\\original";
		String path_outputDirectory = "F:\\Fusion\\2011\\normalize";
		String []lists = new File(path_inputDirectory).list();
		Normalize normalize = new Normalize();
		int startqid = 101;
		for (String path:lists){
			System.out.println(path);
			for (int i = startqid; i < startqid +50; i++){
				normalize.read(path, i, path_inputDirectory+"\\"+path, path_outputDirectory+"\\"+path.replace("input.", ""));
			}
		}
	//	new Normalize().read("E:/ws/ws-1//Fusion/2013Nriskruns/ICTNET13RSR1", "E://ws//ws-1//Fusion/2013_new/ICTNET13RSR1");
	}
	
	public void read(String filename, int qid, String path_input, String path_output) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(path_input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(path_output, true));
		String temp = "";
		
		
		HashSet<String> docidSet = new HashSet<String>();
		while ((temp = br.readLine()) != null){
			StringTokenizer st = new StringTokenizer(temp);
			StringBuffer sb = new StringBuffer();
			int qqqqid = Integer.parseInt(st.nextToken());
			if (qqqqid != qid){
				continue;
			};
			sb.append(qqqqid+"\t");//qid
			sb.append(st.nextToken()+"\t");//Q0
			String tempdocid = st.nextToken();
			if (docidSet.contains(tempdocid)){
				continue;
			}
			docidSet.add(tempdocid);
			sb.append(tempdocid+"\t");//docid
			/*String rankString = st.nextToken();//No
			sb.append(rankString+"\t");
			int rank_1 = Integer.parseInt(rankString);*/
			int rank_1 = docidSet.size();
			sb.append(rank_1+"\t");
			st.nextToken();
			
	/*		double score = norm(rank_1);
			sb.append(score+"\t");
			sb.append(st.nextToken()+"\t");
			bw.write(sb.toString()+"\n");*/
			
			

			double score = norm_log(rank_1);
			if (score == 0){
				continue;
			}
			sb.append(score+"\t");
			sb.append(filename.replace("input.", "")+"\t");
			bw.write(sb.toString()+"\n");
		}
		bw.close();
		br.close();
	}

	public double norm(int rank){
		double result = 1.0/(rank + 60);
		
		return result;
	}

	public double norm_log(int rank){
		double result = Math.max(1 - 0.2 * Math.log(rank + 1), 0);
	//	System.out.println(result);
		return result;
	}
}
