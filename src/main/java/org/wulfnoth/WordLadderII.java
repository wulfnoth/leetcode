package org.wulfnoth;

import java.util.*;

public class WordLadderII {

	private class Heap {

		int size = 0;
		ArrayList<Node> data = new ArrayList<>();
		HashMap<Integer, Integer> idToPosition = new HashMap<>();

		boolean notEmpty() {
			return size != 0;
		}

		void update() {

		}

		Node take() {
			return null;
		}

		void offer(Node elem) {
			if (size <= data.size()) {
				data.add(elem);
			} else {
				data.set(size, elem);
			}
			idToPosition.put(elem.id, size);
			size++;
			upCast(size);
		}

		void upCast(int pos) {
			if (pos != 0) {
				int parentPos = (pos-1)/2;
				if (data.get(parentPos).compareTo(data.get(pos)) > 0) {
					Collections.swap(data, parentPos, pos);
					idToPosition.put(data.get(parentPos).id, parentPos);
					idToPosition.put(data.get(pos).id, pos);
					upCast(parentPos);
				}
			}
		}

		void downCast(int pos) {
			int leftPos = pos*2 + 1;
			int rightPos = pos*2 + 2;
			int targetPos = -1;
			int targetDistance = data.get(pos).distance;
			if (leftPos < size) {
				if (targetDistance > data.get(leftPos).distance) {
					targetDistance = data.get(leftPos).distance;
					targetPos = leftPos;
				}
				if (rightPos < size && targetDistance > data.get(rightPos).distance)
					targetPos = rightPos;
			}
			if (targetPos != -1) {
				Collections.swap(data, pos, targetPos);
				idToPosition.put(data.get(targetPos).id, targetPos);
				idToPosition.put(data.get(pos).id, pos);
				downCast(targetPos);
			}
		}


	}

	private class Node implements Comparable<Node> {
		String word;
		int distance;
		int id;
		List<Integer> last;
		List<Node> neighbor;

		Node(int id, int distance, String word) {
			this.word = word;
			this.distance = distance;
			this.id = id;
			last = new ArrayList<>();
			neighbor = new ArrayList<>();
		}

		Node(int id, String word) {
			this(id, Integer.MAX_VALUE, word);
		}

		boolean update(int lastId, int newDistance) {
			if (newDistance < distance) {
				last.clear();
				last.add(lastId);
			} else if (newDistance == distance) {
				last.add(lastId);
			} else {
				return false;
			}
			return true;
		}

		@Override
		public int compareTo(Node o) {
			return distance - o.distance;
		}

		@Override
		public boolean equals(Object obj) {
			Node other = (Node)obj;
			return id == other.id;
		}

		@Override
		public int hashCode() {
			return Integer.hashCode(id);
		}
	}

	private boolean isClosed(Node firstNode, Node secondNode) {
		String first = firstNode.word;
		String second = secondNode.word;
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

		List<Node> graphMap = new ArrayList<>();
		int beginId = wordList.indexOf(beginWord);
		int endId = wordList.indexOf(endWord);

		for (int i = 0; i < wordList.size(); i++)
			graphMap.add(new Node(i, wordList.get(i)));

		if (beginId == -1) {
			beginId = graphMap.size();
			graphMap.add(new Node(beginId, 0, beginWord));
		} else {
			graphMap.get(beginId).distance = 0;
		}

		if (endId == -1) {
			endId = graphMap.size();
			graphMap.add(new Node(endId, endWord));
		}

		for (int i = 0; i < graphMap.size(); i++)
			for (int j = i+1; j < graphMap.size(); j++)
				if (isClosed(graphMap.get(i), graphMap.get(j))) {
					graphMap.get(i).neighbor.add(graphMap.get(j));
					graphMap.get(j).neighbor.add(graphMap.get(i));
				}

		return null;
	}

	public void dijkstra(List<Node> graph, int startId) {
		PriorityQueue<Node> queue = new PriorityQueue<>();
		queue.offer(graph.get(startId));
	}

}
