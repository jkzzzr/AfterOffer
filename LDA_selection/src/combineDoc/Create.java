package combineDoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.Pointer;
import org.terrier.structures.PostingIndex;
import org.terrier.structures.postings.IterablePosting;


public class Create {
	private static Index index = null;
	private static MetaIndex meta = null;
	private static DocumentIndex doi = null;
	private static PostingIndex<Pointer> directIndex = null;
	private static Lexicon<String> lexicon = null;
	static{
		init();
	}
	
	public static void init(){
		index = Index.createIndex();
		meta = index.getMetaIndex();
		doi = index.getDocumentIndex();
		directIndex = (PostingIndex<Pointer>)index.getDirectIndex();
		lexicon = index.getLexicon();
	}
	
	
	/**
	 * 根绝文档ID，获取该文档的信息
	 * @param docno
	 * @return
	 * @throws Exception
	 */
	public static HashMap<Integer, Integer> getDocItemList(int docno) {
		HashMap<Integer, Integer>hmap  = null;
		try {
			hmap = new HashMap<Integer, Integer>();
			IterablePosting pi;
			synchronized (index) {
					pi = directIndex.getPostings(doi.getDocumentEntry(docno));
			}
			while (pi.next()!=IterablePosting.EOL){
				int freq = pi.getFrequency();
				hmap.put(pi.getId(), freq);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hmap;
	}
	
}
