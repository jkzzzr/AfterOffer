package filter.structure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;

import OriginData.IDF;

public class Doc_Miu {
	public HashMap<String, Double> WordProbability = new HashMap<String, Double>();
	public double ARFA = 0.0;
	public Integer docid = 0;
	public int miu = 0;
	public int qid = 0;
	public Doc_Miu(Doc_Origin doc, int miu1,int q){
		this.miu = miu1;
		this.qid = q;
		HashMap<String, Double> result = new HashMap<String, Double>();
		double fenmu = miu;
	//	System.out.println("分子");
		for (Entry<String, Integer> entry: doc.WordCount.entrySet()){
			String word = entry.getKey();
			Integer count = entry.getValue();
			fenmu +=count;
			double proOnCollection = getWordProOnCollection(word);
			double fenzi = count + miu * proOnCollection;
			result.put(word, fenzi);
			
		}
		/*System.out.println();try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Entry<Integer, Double> entry : result.entrySet()){
			double value = entry.getValue() / fenmu;
			result.put(entry.getKey(), value);
		}*/
		final double fenmu2 = fenmu;
		result.forEach((o1,o2)->{
			o2 /= fenmu2;
			result.put(o1, o2);
		});
		for (Entry<String, Double> entry : result.entrySet()){
			result.put(entry.getKey(), entry.getValue()/fenmu);
		}
		this.WordProbability = result;
		ARFA = miu/fenmu;
	}
	public Double getWordProOnCollection(String i){
		double wordcountoncollection = IDF.wordFrequencyOnCollection.get(this.qid).get(i);
		return wordcountoncollection;
	}
	public double get(String key){
		if (WordProbability.containsKey(key)){
			return WordProbability.get(key);
		}else {
			return ARFA;
		}
	}
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		TreeSet<String> ts = new TreeSet<>(this.WordProbability.keySet());
		Iterator<String> iterator = ts.iterator();
		while (iterator.hasNext()){
			String key = iterator.next();
			sb.append("("+ key +","+this.WordProbability.get(key)+")\t");
		}
		String string = sb.toString();
		return string;
	}
}
