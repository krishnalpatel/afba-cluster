package com.afd.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.afd.pojo.Word;

@Repository(value = "wordDao")
@EnableTransactionManagement()
public class WordDao extends Dao {

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void insert(Word word) {
		template.save(word);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int getWordCount(String word) {
		int wordCount = 0;
		for (Word w : (List<Word>)template.find("select w from Word w where word='"+word+"'")) {
			wordCount+=w.getFrequency();
		}
		return wordCount;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int getDocCountWithWord(String word) {
		return ((List<Word>)template.find("select w from Word w where word='"+word+"'")).size();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<Word> getWordListForDocId(Long docId) {
		return (List<Word>)template.find("select w from Word w where w.docId="+docId+
				" and w.word in (select b.word from BagOfWords b)");
	}

}
