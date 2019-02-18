package com.corenlp.test;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;

public class NLPTest {
	
	
	public static String text = "BJP President Amit Shah challenged the Supreme Court order on entry of women to Sabarimala temple & incited his partymen to defy the verdict. "
			+ "This blatant ridicule of SC judgement is in keeping with RSS-BJP's contempt for Constitution & the Supreme Court.";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Properties props = new Properties();
		
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse");
		
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		CoreDocument document = new CoreDocument(text);
		
		long ts1 = System.currentTimeMillis();
		
		pipeline.annotate(document);
		
		long ts2 = System.currentTimeMillis();
		
		System.out.println("Time to annotate : " + (ts2 - ts1) + " ms");
		int questions = 0;
		
		System.out.println("Num sentences = " + document.sentences().size());
		
		/*for(CoreSentence sentence: document.sentences()) {
			
			System.out.println("sentence : " + sentence.text());
			System.out.println("Dependency Parse:");
			
			sentence.dependencyParse().prettyPrint();
			
			if(sentence.text().endsWith("?")) {
				questions++;
			}
			
		}
		

		System.out.println("Num sentences ending with question mark = " + questions);*/
			
		/*List<String> posTags = document.sentences().stream()
				.flatMap(sent -> sent.posTags().stream()).collect(Collectors.toList());
		
		List<CoreLabel> tokens = document.sentences().stream()
				.flatMap(sent -> sent.tokens().stream()).collect(Collectors.toList());
		
		List<String> posList = IntStream.range(0, tokens.size())
				.mapToObj(i -> tokens.get(i).originalText() + "::" + posTags.get(i))
				.collect(Collectors.toList());
		
		
		List<String> nerTags = document.sentences().stream()
				.flatMap(sent -> sent.nerTags().stream()).collect(Collectors.toList());*/
		
		//posList.stream().forEach(posListItem -> System.out.println(posListItem));
		
		//nerTags.stream().forEach(System.out::println);
		
		/*for (CoreEntityMention em : document.entityMentions())
		      System.out.println("\tdetected entity: \t"+em.text()+"\t"+em.entityType());*/
		
		
		List<SemanticGraph> graphs = document.sentences().stream()
				.map(sent -> sent.dependencyParse()).collect(Collectors.toList());
		
		
		/*List<IndexedWord> vbNodes = graphs.stream()
				.flatMap(graph -> graph.getAllNodesByPartOfSpeechPattern("^VB").stream())
				.collect(Collectors.toList());*/
		
		List<SemanticGraphEdge> dobj_amod_edges = graphs.stream()
				.flatMap(graph -> graph.findAllRelns(GrammaticalRelation.valueOf("dobj")).stream())
				.collect(Collectors.toList());
		
		/*List<SemanticGraphEdge> amodEdges = graphs.stream()
				.flatMap(graph -> graph.findAllRelns(GrammaticalRelation.valueOf("amod")).stream())
				.collect(Collectors.toList());*/
		
		dobj_amod_edges.addAll(graphs.stream()
				.flatMap(graph -> graph.findAllRelns(GrammaticalRelation.valueOf("amod")).stream())
				.collect(Collectors.toList()));
		
		/*List<SemanticGraphEdge> dobj_amod_edges = Stream.concat(dobjEdges.stream(),amodEdges.stream())
				.collect(Collectors.toList());*/
		
		dobj_amod_edges.stream().forEach(edge -> {
			System.out.println(edge.getSource().originalText() + "::" + edge.getSource().tag() + "::"  
					+ edge.getTarget().originalText() + "::" + edge.getTarget().tag() + "::" 
					+ edge.getRelation());
		});
		
		System.out.println(dobj_amod_edges);
		
	}

}
