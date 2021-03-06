package my;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class HelpWYR {

	public static void main(String[] args) throws IOException {
		System.out.println("==");
		func1("F:\\Fusion\\2010-wyr");
		writeAvg("F:\\Fusion\\2010-LL");
	}
	public static ArrayList<ArrayList<Double>> eRRIA = new ArrayList<ArrayList<Double>>();
	public static ArrayList<ArrayList<Double>> Andcg = new ArrayList<ArrayList<Double>>();
	public static int allnumber1 = 0;
	public static int allnumber2 = 0;
	

	public static boolean first1 = true;
	public static boolean first2 = true;
	public static void func1(String path) throws IOException{
		int lineNumber = 0;
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = "";
		
		
		while ((line = br.readLine())!=null){
			lineNumber++;
			
			if (line.contains("******")){
				
				if (line.contains("erria")){
					allnumber1++;
					br.readLine();
					for (int i = 0; i < 19;i++){
						if (first1 ){
							eRRIA.add(new ArrayList<Double>());
						}
						line = br.readLine();
						lineNumber++;
						StringTokenizer st = new StringTokenizer(line);
						st.nextToken();
						int j = -1;
						while (st.hasMoreElements()){
							j++;
							double d = Double.parseDouble(st.nextToken());
							if (first1){
								eRRIA.get(i).add(d);
							}else {
								double temp = eRRIA.get(i).get(j);
								eRRIA.get(i).set(j, temp+d);
							}
						}
						
					}
					first1 = false;
				}
				if (line.contains("ndcg")){
					allnumber2++;
					br.readLine();
					for (int i = 0; i < 19;i++){
						lineNumber++;
						if (first2){
							Andcg.add(new ArrayList<Double>());
						}
						line = br.readLine();
						StringTokenizer st = new StringTokenizer(line);
						st.nextToken();
						int j = -1;
						String look = "";
						try{
							while (st.hasMoreElements()){
								j++;
								look = st.nextToken();
								double d = Double.parseDouble(look);
								if (first2){
									Andcg.get(i).add(d);
								}else {
									double temp = Andcg.get(i).get(j);
									Andcg.get(i).set(j, temp+d);
								}
							}
						}catch(Exception exception){
							System.out.println(exception.getMessage());
							System.err.println(lineNumber+"\t"+line);
						}
						finally{
						//	System.out.println("123");
						}
						
					}
					first2 = false;
				}
			}
			
		}
		
	}
	
	public static void writeAvg(String path) throws IOException{
		BufferedWriter bw = new BufferedWriter( new FileWriter(path, true));
		bw.write("erria\n");
		for (int i = 0; i < 19;i++){
			for (int j = 0; j < eRRIA.get(i).size();j++){
				double result = eRRIA.get(i).get(j);
				bw.write(result/allnumber1+"\t");
			}
			bw.write("\n");
		}
		
		bw.write("andcg\n");
		for (int i = 0; i < 19;i++){
			for (int j = 0; j < Andcg.get(i).size();j++){
				double result = Andcg.get(i).get(j)/allnumber2;
				bw.write(result +"\t");
			}
			bw.write("\n");
		}
		
		bw.close();
		
	}

}
