package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import myMethod.Choose_Centroid;
import myMethod.Choose_MMR;

/**
 * 大文档方法（无论是LDA方法，还是，大文档方法）都会得到一个文档类别的MMR得分结果列表
 * 这个结果列表就是此处的输入
 * @author Lee
 *
 */
public class Choose_TotalMMR {

	public static int typenumber = 3;
	public static void main(String[] args) throws Exception {
		Choose_MMR choose_MMR = new Choose_MMR();
		System.out.println("WTF2");
		for (int i = 251; i <= 300; i++){
			System.out.println("qid\t" + i);
			
			
			
			BufferedReader brBufferedReader = new BufferedReader(new FileReader("E:\\2014query\\大文档--LDA-类别结果列表"));
		//	BufferedReader brBufferedReader = new BufferedReader(new FileReader("E:\\2013query\\类别多样化计算--LDA-Cosin"));
			String line = "";
			int count = 0;
			int[] a = new int[typenumber];
			while ((line =brBufferedReader.readLine())!=null){
				if (!line.startsWith(i+"")){
					continue;
				}
				if (count + 1 > typenumber){
					break;
				}
				StringTokenizer st = new StringTokenizer(line);
				st.nextToken();
				st.nextToken();
				int type = Integer.parseInt(st.nextToken());
				a[count] = type;
				count ++;
			}
			
			System.out.println("当前进度：\tqid:" +i);
			choose_MMR.clear(i);
			choose_MMR.init_chooseCen(a);
			HashSet<Integer> dataSet = choose_MMR.init_dataList("E:\\2014query\\result-原始，还没多样化的\\全局-自定义");
			choose_MMR.choose(dataSet, "E:\\2014query\\DPH_1.res.index.adhoc", "E:\\2014query\\check\\LDA."
					+ typenumber
					+ ".choose");
		}

	}

}
