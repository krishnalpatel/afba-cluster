package com.afd.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.afd.dao.BagOfWordsDao;
import com.afd.dao.DocumentDao;
import com.afd.dao.WordDao;
import com.afd.pojo.BagOfWords;
import com.afd.pojo.Document;
import com.afd.pojo.Word;
import com.afd.pojo.WordStat;

@Component(value="tfidfCalculator")
@Scope(value="prototype")
public class TFIDFCalculator {
	
	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	private WordDao wordDao;
	
	@Autowired
	private BagOfWordsDao bagOfWordsDao; 

	public void calculate() {
		
		List<Document> docList = documentDao.getDocumentList();
		
		for (Document document : docList) {
			List<Word> wordList = wordDao.getWordListForDocId(document.getId());
			List<BagOfWords> bagOfWordsList = bagOfWordsDao.getBagOfWordsList();
			List<WordStat> wordStatList = new ArrayList<>();
			
			for (BagOfWords bagOfWords : bagOfWordsList) {
				WordStat wordStat = new WordStat();
				
				String word = bagOfWords.getWord();
				//System.out.println("Word: "+ word);
				
				int index = -1;
				//find tf in this doc
				for (Word word2 : wordList) {
					if(word2.getWord().equals(word)){
						index = wordList.indexOf(word2);
						break;
					}
				}
				//System.out.println("index: "+ index);
				
				if(index != -1){
					//find no of docs conatining the word
					int docCount = wordDao.getDocCountWithWord(word);
					//System.out.println("docCount: "+ docCount);
					
					//calculate idf of that word
					double idf = idf(docList.size(), docCount);
					//System.out.println("IDF: "+ idf);
					wordStat.setIdf(idf);
					
					Word wordAtIndex = wordList.get(index);
					//System.out.println("Word: "+ wordAtIndex);
					int tf=0;
					tf = wordAtIndex.getFrequency();
					//calc tf-weighted i.e. 1+log(tf)
					double tf_weighted = (double) (1+ Math.log(tf));
					wordStat.setTf_weighted(tf_weighted);
					
					//System.out.println("WordStat: "+wordStat);
					wordStatList.add(wordStat);
				}
			}
			
			double totalweight = 0.0;
			for (WordStat wordStat : wordStatList) {
				totalweight += (wordStat.getTf_weighted()*wordStat.getTf_weighted());
			}
			//System.out.println("total weight: " + totalweight);
			totalweight = Math.sqrt(totalweight);
			//System.out.println("Sqrt: "+ totalweight);
			
			double similarity = 0.0;
			for (WordStat wordStat : wordStatList) {
				wordStat.setNormalized(wordStat.getTf_weighted()/totalweight);
				similarity += (wordStat.getIdf()*wordStat.getNormalized());
			}
			System.out.println("Similarity: "+ similarity);
			document.setSimilarity(similarity);
			documentDao.insertDocument(document);
		}
		
	}
/*
	public Map<String, Float> calculateTfidf(Map<String, Integer> wordMap, Long docId, int totalFreq) {
		
		Map<String, Float> tfidfMap = new HashMap<>();
		int docCount = documentDao.getNoOfDocuments();
		for (Entry<String, Integer> entry : wordMap.entrySet()) {
			String key = entry.getKey();
			tfidfMap.put(key, tf(entry.getValue(), totalFreq)*idf(key, docCount));
		}	
		return tfidfMap;
	}
*/
	private double idf(int totalDocs, int docCountWithWord) {
		return Math.log((double)totalDocs/(double)docCountWithWord);
	}
/*
	private float tf(int freq, int totalFreq) {
		return (float)freq/(float)totalFreq;
	}
*/
	public DocumentDao getDocumentDao() {
		return documentDao;
	}

	public void setDocumentDao(DocumentDao documentDao) {
		this.documentDao = documentDao;
	}

	public WordDao getWordDao() {
		return wordDao;
	}

	public void setWordDao(WordDao wordDao) {
		this.wordDao = wordDao;
	}

	public BagOfWordsDao getBagOfWordsDao() {
		return bagOfWordsDao;
	}

	public void setBagOfWordsDao(BagOfWordsDao bagOfWordsDao) {
		this.bagOfWordsDao = bagOfWordsDao;
	}

	
}
