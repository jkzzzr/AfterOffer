package filter.structure;

import java.util.HashMap;

import Err.WriteErr;

public class Doc_Origin_Builder {

	public HashMap<String, Doc_Origin> docOriginList = new HashMap<String, Doc_Origin>();

	public Doc_Origin get(String key){
		Doc_Origin result = null;
		if (docOriginList.containsKey(key)){
			result =  docOriginList.get(key);
		}else {
			Doc_Origin doc_Origin = new Doc_Origin(key);
			this.docOriginList.put(key, doc_Origin);
			result = doc_Origin;
		}
		if (result.WordCount.size() == 0){
			if (!WriteErr.ErrSet.contains(key)){
				WriteErr.ErrSet.add(key);
			}
		}
		return result ;
	}
/*	public void put(Integer key, Doc_Origin value){
		docOriginList.put(key, value);
	}*/
}
