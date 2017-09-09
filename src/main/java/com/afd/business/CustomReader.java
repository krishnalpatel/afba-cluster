package com.afd.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.afd.dao.DocumentDao;
import com.afd.dao.TfidfTempDao;
import com.afd.dao.WordDao;
import com.afd.pojo.TfidfTemp;
import com.afd.pojo.Word;
import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;

@Component(value = "customReader")
@Scope(value = "prototype")
public class CustomReader {

	@Autowired
	private DocumentDao documentDao;
	@Autowired
	private WordDao wordDao;
	@Autowired
	private TFIDFCalculator tfidfCalculator;
	@Autowired
	private TfidfTempDao tfidfTempDao;
	@Autowired
	private DatumboxCaller datumboxCaller;

	public void readFile(File file) throws FileNotFoundException {
		// first, check if the document already exist in the database
		// i.e. if the document has already been read once		
		if (documentDao.containsDoc(file.getName())) {
			System.out.println("The document: " + file.getName() +
					" is already in the database. Reading skipped!");
		} else {			
			try {
				String fileText = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
				String filteredText = ExudeData.getInstance().filterStoppingsKeepDuplicates(fileText);
				
				Map<String, Object> map = datumboxCaller.extractKeywords(filteredText);
				Map<String, Integer> wordMap = (Map<String, Integer>) map.get("wordMap");
				int totalFrequency = (Integer) map.get("totalFrequency");
				
				// save and get the docId saved in the Document table
				documentDao.insertDocument(file.getName(), totalFrequency);
				Long docId = documentDao.getDocumentId(file.getName());

				// persist the map in the word table
				persistWordMap(wordMap, file, docId);

				// call the tf-idf calculator to calculate the tf-idf for each word in the map.
				//Map<String, Float> tfidfMap = tfidfCalculator.calculateTfidf(wordMap, docId, totalFrequency);

				// persist the map in the tfidf temp table
				//persistTfidfMap(tfidfMap, docId);

			} catch (IOException | InvalidDataException e) {
				e.printStackTrace();
			}
		}
	}

	private void persistWordMap(Map<String, Integer> wordMap, File file, Long docId) {
		for (Entry<String, Integer> entry : wordMap.entrySet()) {
			Word word = new Word();
			word.setWord(entry.getKey());
			word.setFrequency(entry.getValue());
			word.setDocId(docId);
			wordDao.insert(word);
		}
	}

	private void persistTfidfMap(Map<String, Float> tfidfMap, Long docId) {
		for (Entry<String, Float> entry : tfidfMap.entrySet()) {
			TfidfTemp tfidfTemp = new TfidfTemp();
			tfidfTemp.setWord(entry.getKey());
			tfidfTemp.setTfidf(entry.getValue());
			tfidfTemp.setDocId(docId);
			getTfidfTempDao().insert(tfidfTemp);
		}

	}

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

	public TFIDFCalculator getTfidfCalculator() {
		return tfidfCalculator;
	}

	public void setTfidfCalculator(TFIDFCalculator tfidfCalculator) {
		this.tfidfCalculator = tfidfCalculator;
	}

	public TfidfTempDao getTfidfTempDao() {
		return tfidfTempDao;
	}

	public void setTfidfTempDao(TfidfTempDao tfidfTempDao) {
		this.tfidfTempDao = tfidfTempDao;
	}

	public DatumboxCaller getDatumboxCaller() {
		return datumboxCaller;
	}

	public void setDatumboxCaller(DatumboxCaller datumboxCaller) {
		this.datumboxCaller = datumboxCaller;
	}

}
