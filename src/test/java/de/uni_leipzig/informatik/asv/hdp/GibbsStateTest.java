package de.uni_leipzig.informatik.asv.hdp;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GibbsStateTest {

	private static Corpus _corpus;
	private HDPGibbsSampler _state;
	private String _outputDir = "/Users/arnim/Desktop/hdp/test/";;


	@BeforeClass
	public static void setUpBefore() throws Exception {
		_corpus = new Corpus();
		_corpus.read(_corpus.getClass().getResourceAsStream("test.corpus"));
	}

	@Before
	public void setUp() throws Exception {
		_state = new HDPGibbsSampler();
	}


	@Test
	public void testCorpus() throws FileNotFoundException {
		assertEquals(20, _corpus.docs.size());
		assertEquals(100, _corpus.sizeVocabulary);
		assertEquals(400, _corpus.totalNumberOfWords);
	}
	
	
	@Test
	public void testDefaults() throws FileNotFoundException {
		assertEquals(.5, _state.beta, Double.MIN_VALUE);
		assertEquals(1, _state.numberOfTopics); // initial number of topics
		assertEquals(1, _state.numberOfTopics); // initial number of topics
		assertEquals(1, _state.gamma, Double.MIN_VALUE); // initial number of topics
		assertEquals(1, _state.alpha, Double.MIN_VALUE); // initial number of topics
	}
	
	@Test
	public void testInitGibbsState() throws FileNotFoundException {
		assertEquals(0, _state.sizeOfVocabulary);
		assertNull(_state.docStates); 
		_state.initGibbsState(_corpus);
		assertEquals(1, _state.numberOfTopics); 
		assertEquals(100, _state.sizeOfVocabulary); 
		assertEquals(10, _state.docStates[10].docID); 
		assertEquals(20, _state.docStates.length); 
		assertTrue(_state.wordCountByTopic[0]==400); 
		assertTrue(_state.wordCountByTopic[1]==0); 
		assertEquals(2, _state.wordCountByTopic.length);  
		assertEquals(_state.wordCountByTopic.length, _state.wordCountByTopicAndDocument.length); 
		assertEquals(_state.wordCountByTopic.length, _state.numberOfTablesByTopic.length); 
		assertEquals(_state.wordCountByTopic.length, _state.wordCountByTopic.length); 
		assertEquals(_state.wordCountByTopic.length, _state.wordCountByTopicAndTerm.length); 
	}
	
	

	private void _testCONSISTENCY(){
		for (int i = 0; i < _state.docStates.length; i++) {
			DOCState docState = _state.docStates[i];
			int counter[] = new int[docState.numberOfTables];
			for (int w = 0; w < docState.words.length; w++) {
				counter[docState.words[w].tableAssignment] ++;
			}
			for (int t = 0; t < docState.numberOfTables; t++) {
				int h = docState.wordCountByTable[t];
				assertEquals(h, counter[t]);
				
			}
		}
	}
	
	@Test
	public void testWordCountByTableTableAssignments() {
		for ( int i = 0; i <= 100; i++){
			_state.initGibbsState(_corpus);
			_testCONSISTENCY();
		}
	}
	
	
	@Test
	public void testHDPGibbsSampler() throws FileNotFoundException {
		_state.beta = 0.5;
		_state.numberOfTopics = 4;
		_state.gamma = 1.0; 
		_state.alpha = 1.0;
		_state.initGibbsState(_corpus);
		_state.run(_outputDir, true, 10, 1000, 10);
	}

}
