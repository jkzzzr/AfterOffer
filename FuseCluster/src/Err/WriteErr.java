package Err;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class WriteErr {

	public static void main(String[] args) {

	}

	public static HashSet<String> ErrSet = new HashSet<>();
	public static void writeErr(String path){
		try {
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(path, true));
		TreeSet<String> ts = new TreeSet<>(ErrSet);
		Iterator it = ts.iterator();
		while (it.hasNext()){
			bWriter.write(it.next()+"\n");
		}
		bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
