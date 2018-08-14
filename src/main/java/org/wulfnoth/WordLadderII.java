package org.wulfnoth;

import java.util.*;

public class WordLadderII {

	private class Heap {

		int cursor = 0;
		ArrayList<Node> data = new ArrayList<>();
		HashMap<Integer, Integer> idToPosition = new HashMap<>();

		boolean notEmpty() {
			return cursor != 0;
		}

		void update(Node node) {
			if (idToPosition.containsKey(node.id)) {
				upCast(idToPosition.get(node.id));
			} else {
				offer(node);
			}
		}

		Node take() {
			if (notEmpty()) {
				Node result = data.get(0);
				idToPosition.remove(result.id);
				cursor--;
				if (notEmpty()) {
					data.set(0, data.get(cursor));
					downCast(0);
				}
				return result;
			}
			return null;
		}

		private void offer(Node elem) {
			if (cursor == data.size()) {
				data.add(elem);
			} else {
				data.set(cursor, elem);
			}
			idToPosition.put(elem.id, cursor);
			cursor++;
			upCast(cursor -1);
		}

		void upCast(int pos) {
			if (pos != 0) {
				int parentPos = (pos-1)/2;
				if (data.get(parentPos).distance > data.get(pos).distance) {
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
			if (leftPos < cursor) {
				if (targetDistance > data.get(leftPos).distance) {
					targetDistance = data.get(leftPos).distance;
					targetPos = leftPos;
				}
				if (rightPos < cursor && targetDistance > data.get(rightPos).distance)
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

	private class Node {
		String word;
		int distance;
		int id;
		List<Node> last;
		List<Node> neighbor;

		@Override
		public String toString() {
			return word + ": " +word + "(" + distance + ")";
		}

		Node(int id, int distance, String word) {
			this.word = word;
			this.distance = distance;
			this.id = id;
			last = new ArrayList<>();
			neighbor = new ArrayList<>();
		}

		Node(int id, String word) {
			this(id, Integer.MAX_VALUE-1, word);
		}

		boolean update(Node lastNode) {
			int newDistance = lastNode.distance+1;
			if (newDistance < distance) {
				distance = newDistance;
				last.clear();
				last.add(lastNode);
				return true;
			} else if (newDistance == distance) {
				last.add(lastNode);
			}
			return false;
		}

	}

//	void printGraph(List<Node> graphs) {
//		for (Node matrix : graphs) {
//			System.out.println(matrix.word);
//			for (Node nei: matrix.neighbor) {
//				System.out.println("\t" + nei.word);
//			}
//		}
//	}

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

		dijkstra(graphMap, beginId);

		List<List<String>> result = new ArrayList<>();

		if (graphMap.get(endId).distance != Integer.MAX_VALUE - 1) {
			Stack<Node> init = new Stack<>();
			init.push(graphMap.get(endId));
			PriorityQueue<Stack<Node>> tmp = new PriorityQueue<>();
			tmp.offer(init);
			while (!tmp.isEmpty()) {
				Stack<Node> stack = tmp.poll();
				List<Node> lasts = stack.peek().last;
				for (Node node: lasts) {
//					(Stack<Node>)stack.clone()).push(node)
				}
			}

		}

		return result;
	}

	public void dijkstra(List<Node> graph, int startId) {
		Heap heap = new Heap();
		heap.update(graph.get(startId));
		while (heap.notEmpty()) {
			Node top = heap.take();
			for (Node node : top.neighbor) {
				if (node.update(top)) {
					heap.update(node);
				}
			}
		}
	}

	public static void main(String[] args) {
		String beginWord = "hit";
		String endWord = "cog";
		String[] wordList = {"hot","dot","dog","lot","log","cog"};
		new WordLadderII().findLadders(beginWord, endWord, Arrays.asList(wordList));
	}

}
