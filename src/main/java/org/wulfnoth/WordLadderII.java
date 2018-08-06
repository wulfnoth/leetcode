package org.wulfnoth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordLadderII {

	private boolean isClosed(String first, String second) {
		boolean different = false;
		for (int i=0; i<first.length(); i++)
			if (first.charAt(i) != second.charAt(i)) {
				if (!different)
					different = true;
				else
					return false;
			}
		return true;
	}

	private List<Integer> beginDistance = new ArrayList<>();
	private List<Integer> endDistance = new ArrayList<>();
	private HashMap<String, Integer> ids = new HashMap<>();
	private List<List<Integer>> lists = new ArrayList<>();

	public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
		List<List<String>> result = new ArrayList<>();

		for (int i = 0; i < wordList.size(); i++)
			ids.put(wordList.get(i), i);

		for (int i = 0; i < lists.size(); i++)
			lists.add(new ArrayList<>());

		for (int i = 0; i < wordList.size(); i++) {
			for (int j = i=1; j < wordList.size(); j++) {
				if (isClosed(wordList.get(i), wordList.get(j))) {
					lists.get(i).add(j);
					lists.get(j).add(i);
				}
			}
			beginDistance.add(isClosed(beginWord, wordList.get(i))?1:-1);
			if (isClosed(endWord, wordList.get(i))) endDistance.add(i);
		}

		return result;
	}

	public List<List<String>> handle() {
		return null;
	}

}
