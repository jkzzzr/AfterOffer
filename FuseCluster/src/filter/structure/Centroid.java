package filter.structure;

import java.util.ArrayList;
import java.util.Collections;


public class Centroid {
	public ArrayList<String> doclist = new ArrayList<String>();
	public void sort(){
		if (this.doclist == null){
			return ;
		}
		if (this.doclist.size() ==0){
			return;
		}
		Collections.sort(this.doclist);
	}
	@Override
	public boolean equals(Object object){
		if (object == null){
			return false;
		}else {
			if (object instanceof Centroid){
				Centroid centroid = (Centroid) object;
				ArrayList<String> tempDocList = centroid.doclist;
				//如果this的列表为空
				if (this.doclist == null){
					if (tempDocList == null){
						return true;
					}else {
						return false;
					}
				}else {
					if (tempDocList == null){
						return false;
					}
				}
				//两者都不为空
				//先判别长度
				if(this.doclist.size() != tempDocList.size()){
					return false;
				}
				for (int i = 0; i < this.doclist.size(); i++){
					String temp1 = doclist.get(i);
					String temp2 = tempDocList.get(i);
					if (temp1.equals(temp2)){
						return false;
					}
				}
				return true;
			}
			return false;
		}
	}
	@Override
	public int hashCode(){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < doclist.size(); i ++){
			//用加号来间隔，防止1,23和12，3混淆了
			sb.append(doclist.get(i));
		}
		String string = sb.toString();
		return string.hashCode();
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		if (this.doclist != null && this.doclist.size() != 0){
			for (String docid: this.doclist){
				sb.append(docid+"\t");
			}
		}
		return sb.toString();
		
	}
}
