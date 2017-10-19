import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;


public class ChecktypeNumber {

	public static void main(String[] args) throws IOException {
		String typePath = "E:\\2013query\\全局-自定义";
		for (int i = 201; i < 250; i++){
			TreeSet<Integer> ts = new TreeSet<Integer>();
			BufferedReader br = new BufferedReader(new FileReader(typePath));
			String line = "";
			while ((line = br.readLine())!=null){
				if (line.startsWith(i + "")){
					StringTokenizer st = new StringTokenizer(line);
					st.nextToken();
					st.nextToken();
					st.nextToken();
					st.nextToken();
					int type = Integer.parseInt(st.nextToken());
					ts.add(type);
				}
			}
			
			Iterator< Integer> it = ts.iterator();
			System.err.print("QID:" + i 
					+"\t" +ts.size()+"\t");
			while (it.hasNext()){
				System.out.print(it.next()+"\t");
			}
			System.out.println();
			
		}
	}

}
