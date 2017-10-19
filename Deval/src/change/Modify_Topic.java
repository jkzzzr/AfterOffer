package change;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Modify_Topic {

	public static void main(String[] args) throws IOException {
		BufferedReader bReader = new BufferedReader(new FileReader("E:\\2014Query\\2014query-origin.txt"));
		BufferedWriter bwBufferedWriter = new BufferedWriter(new FileWriter("E:\\2014Query\\2014query.txt"));
		String line = "";
		while ((line = bReader.readLine()) !=null){
			DD dd = run(line);
			bwBufferedWriter.write(dd.toString() +"\n");
		}
		bwBufferedWriter.close();
		bReader.close();
	}

	public static DD run(String line){
		String [] strings = line.split("[:]");
		DD dd = new DD();
		dd.qid = strings[0];
		dd.qString = strings[1];
		return dd;
	}
}
class DD{
	public String qid;
	public String qString;
	public String toString(){
		String result = "<TOP><NUM>"
				+ qid
				+ "</NUM><TITLE>"
				+ qString
				+ "</TITLE></TOP>";
		return result;
	}
}