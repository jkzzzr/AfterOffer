package filter.structure;

import java.util.HashMap;

import OriginData.DataInput_My;

public class Doc_Origin {

	public HashMap<String, Integer> WordCount = new HashMap<String, Integer>();
	public String docid = "";
	public Doc_Origin(String doc){
		this.docid = doc;
		WordCount = getdoc(doc);
	}
	private HashMap<String, Integer> getdoc(String integer){
			HashMap<String, Integer> result = new HashMap<String, Integer>(
				DataInput_My.Input_D_W.get(integer));
		return result;
	}
}
