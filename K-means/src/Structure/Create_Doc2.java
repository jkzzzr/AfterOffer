package Structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.Environment;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.Pointer;
import org.terrier.structures.PostingIndex;
import org.terrier.structures.postings.IterablePosting;

import K_means.AllPath;
import K_means.K_Means;

public class Create_Doc2 {
	public static double percent = AllPath.percent;
	
	
	private Index index = null;
	private MetaIndex meta = null;
	private DocumentIndex doi = null;
	private PostingIndex<Pointer> directIndex = null;
	private Lexicon<String> lexicon = null;
	public int alldocnumber = 0;
	
	
//	public static ArrayList<Integer> alldoclsit = new ArrayList<Integer>();
	
	public static void main(String args[]) throws Exception{
		
	}
	
	public Create_Doc2() {
		this.index = Index.createIndex();
		this.meta = index.getMetaIndex();
		this.doi = index.getDocumentIndex();
		this.directIndex = (PostingIndex<Pointer>)index.getDirectIndex();
		this.lexicon = index.getLexicon();
		this.alldocnumber = doi.getNumberOfDocuments();
	}

	/**
	 * 负责对上一步已经初始化了的alldoclist进行重筛选
	 * @return
	 * @throws Exception
	 */
	public ArrayList<SingleDoc> init() throws Exception{
		System.out.println("init_single_list");
		ArrayList<SingleDoc> result = new ArrayList<SingleDoc>();
		int docNumber = doi.getNumberOfDocuments();
		System.out.println(docNumber+"");
		for (int i = 0; i < docNumber; i++){
			if (!isChoosen()){
				continue;
			}
			K_Means.alldoclist.add(i);
		}
		System.out.println("@Create_Doc2 - init_single_list()"
				+ "\n\t alldocNumber: "+ docNumber
				+ "\n\t alldoclsit.size(): "+ K_Means.alldoclist.size());
		return result;
	}

	private boolean isChoosen(){
		double rand = Math.random();
		if (rand < percent){
			return true;
		}
		return false;
	}

	public String  getdocString(int docno) throws Exception{
		String docString = meta.getItem("docno", docno);
		return docString;
	}
	/**
	 * 根绝文档ID，获取该文档的信息
	 * @param docno
	 * @return
	 * @throws Exception
	 */
	public SingleDoc getDocItemList(int docno) {
//		System.out.println("docno!!!!!!!!!!" + docno);
		SingleDoc single = null;
		try {
		HashMap<Integer, Double>hmap = new HashMap<Integer, Double>();
		int wordCount = 0;
		IterablePosting pi;
		synchronized (index) {
				pi = directIndex.getPostings(doi.getDocumentEntry(docno));
		}
		while (pi.next()!=IterablePosting.EOL){
			int freq = pi.getFrequency();
			hmap.put(pi.getId(), (double)freq);
			wordCount +=freq;
		}
		single = new SingleDoc(hmap, wordCount);
		} catch (IOException e) {
			System.out.println(docno+"====="+ K_Means.alldoclist.contains(docno));
//			e.printStackTrace();
		}
		return single;
	}
	
	public String getItemString(int termno){
		Map.Entry<String,LexiconEntry> leeEntry = lexicon.getLexiconEntry(termno);
		String termString = leeEntry.getKey();
		return termString;
	}
}
