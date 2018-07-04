package org.wulfnoth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class LRUCache {

	private static int temp = 0;

	private static int id = 0;

	class Node {
		int timestamp;
		int key;
		int value;
		int position;

		Node(int key, int value) {
			this.key = key;
			this.value = value;
			updateTimestamp();
		}

		void updateTimestamp() {
			this.timestamp = id++;
		}

	}

	Node[] heap; //minimum heap

	HashMap<Integer, Node> cache = new HashMap<>();

	public LRUCache(int capacity) {
		heap = new Node[capacity];
	}

	public int get(int key) {
		Node v = cache.get(key);
		if (v != null) {
			Node node = cache.get(key);
			node.updateTimestamp();
			downcast(node);
			System.out.printf("%d: %d\n", temp++, heap[0].key);
			return v.value;
		}
		System.out.printf("%d: %d\n", temp++, heap[0].key);
		return -1;
	}

	private void downcast(Node node) {
		if (node.position * 2 + 1 < cache.size()) { //left child is existed
			Node leftNode = heap[node.position * 2 + 1];
			if (leftNode.timestamp < node.timestamp) { //downcast
				swap(node.position, leftNode.position);
				downcast(node);
			} else if (node.position*2 + 2 < cache.size()){
				Node rightNode = heap[node.position*2 + 2];
				if (rightNode.timestamp < node.timestamp) {
					swap(node.position, rightNode.position);
					downcast(node);
				}
			}
		}
	}

	private void swap(int first, int second) {
		Node tmp = heap[first];
		heap[first] = heap[second];
		heap[second] = tmp;
		heap[first].position = first;
		heap[second].position = second;
	}

	public Object put(int key, int value) {
		if (heap.length != 0) {
			if(cache.containsKey(key)) {
				Node node = cache.get(key);
				node.updateTimestamp();
				downcast(node);
				node.value = value;
			} else if(cache.size() < heap.length) {
				Node node = new Node(key, value);
				node.position = cache.size();
				heap[cache.size()] = node;
				cache.put(key, node);
			} else {
				Node node = new Node(key, value);
				node.position = 0;
				cache.put(key, node);
				cache.remove(heap[0].key);
				heap[0] = node;
				downcast(node);
			}
		}
		System.out.printf("%d: %d\n", temp++, heap[0].key);
		return null;
	}

	public static void main(String[] args) {
		List<Object> list = new ArrayList<>();

		LRUCache cache = new LRUCache( 10);

		list.add(cache.put(10,13));
		list.add(cache.put(3,17));
		list.add(cache.put(6,11));
		list.add(cache.put(10,5));
		list.add(cache.put(9,10));
		list.add(cache.get(13));
		list.add(cache.put(2,19));
		list.add(cache.get(2));
		list.add(cache.get(3));
		list.add(cache.put(5,25));
		list.add(cache.get(8));
		list.add(cache.put(9,22));
		list.add(cache.put(5,5));
		list.add(cache.put(1,30));
		list.add(cache.get(11));
		list.add(cache.put(9,12));
		list.add(cache.get(7));
		list.add(cache.get(5));
		list.add(cache.get(8));
		list.add(cache.get(9));
		list.add(cache.put(4,30));
		list.add(cache.put(9,3));
		list.add(cache.get(9));
		list.add(cache.get(10));
		list.add(cache.get(10));
		list.add(cache.put(6,14));
		list.add(cache.put(3,1));
		list.add(cache.get(3));
		list.add(cache.put(10,11));
		list.add(cache.get(8));
		list.add(cache.put(2,14));
		list.add(cache.get(1));
		list.add(cache.get(5));
		list.add(cache.get(4));
		list.add(cache.put(11,4));
		list.add(cache.put(12,24));
		list.add(cache.put(5,18));
		list.add(cache.get(13));
		list.add(cache.put(7,23));
		list.add(cache.get(8));
		list.add(cache.get(12));
		list.add(cache.put(3,27));
		list.add(cache.put(2,12));
		list.add(cache.get(5));
		list.add(cache.put(2,9));
		list.add(cache.put(13,4));
		list.add(cache.put(8,18));
		list.add(cache.put(1,7));
		list.add(cache.get(6));
		list.add(cache.put(9,29));
		list.add(cache.put(8,21));
		list.add(cache.get(5));
		list.add(cache.put(6,30));
		list.add(cache.put(1,12));
		list.add(cache.get(10));
		list.add(cache.put(4,15));
		list.add(cache.put(7,22));
		list.add(cache.put(11,26));
		list.add(cache.put(8,17));
		list.add(cache.put(9,29));
		list.add(cache.get(5));
		list.add(cache.put(3,4));
		list.add(cache.put(11,30));
		list.add(cache.get(12));
		list.add(cache.put(4,29));
		list.add(cache.get(3));
		list.add(cache.get(9));
		list.add(cache.get(6));
		list.add(cache.put(3,4));
		list.add(cache.get(1));
		list.add(cache.get(10));
		list.add(cache.put(3,29));
		list.add(cache.put(10,28));
		list.add(cache.put(1,20));
		list.add(cache.put(11,13));
		list.add(cache.get(3));
		list.add(cache.put(3,12));
		list.add(cache.put(3,8));
		list.add(cache.put(10,9));
		list.add(cache.put(3,26));
		list.add(cache.get(8));
		list.add(cache.get(7));
		list.add(cache.get(5));
		list.add(cache.put(13,17));
		list.add(cache.put(2,27));
		list.add(cache.put(11,15));
		list.add(cache.get(12));
		list.add(cache.put(9,19));
		list.add(cache.put(2,15));
		list.add(cache.put(3,16));
		list.add(cache.get(1));
		list.add(cache.put(12,17));
		list.add(cache.put(9,1));
		list.add(cache.put(6,19));
		list.add(cache.get(4));
		list.add(cache.get(5));
		list.add(cache.get(5));
		list.add(cache.put(8,1));
		list.add(cache.put(11,7));
		list.add(cache.put(5,2));
		list.add(cache.put(9,28));
		list.add(cache.get(1));
		list.add(cache.put(2,2));
		list.add(cache.put(7,4));
		list.add(cache.put(4,22));
		list.add(cache.put(7,24));
		list.add(cache.put(9,26));
		list.add(cache.put(13,28));
		list.add(cache.put(11,26));

//		String result = "null,null,null,null,null,-1,null,19,17,null,-1,null,null,null,-1,null,-1,5,-1,12," +
//				"null,null,3,5,5,null,null,1,null,-1,null,30,5,30,null,null,null,-1,null,-1,24,null," +
//				"null,18,null,null,null,null,14,null,null,18,null,null,-1,null,null,null,null,null,18," +
//				"null,null,24,null,4,29,30,null,12,-1,null,null,null,null,29,null,null,null,null,-1,-1," +
//				"18,null,null,null,24,null,null,null,20,null,null,null,-1,18,18,null,null,null,null,20," +
//				"null,null,null,null,null,null,null";

		String result = "null,null,null,null,null,-1,null,19,17,null,-1,null,null,null,-1,null,-1,5,-1,12," +
				"null,null,3,5,5,null,null,1,null,-1,null,30,5,30,null,null,null,-1,null,-1,24,null," +
				"null,18,null,null,null,null,-1,null,null,18,null,null,-1,null,null,null,null,null,18," +
				"null,null,-1,null,4,29,30,null,12,-1,null,null,null,null,29,null,null,null,null,17,22,18,null,null," +
				"null,-1,null,null,null,20,null,null,null,-1,18,18,null,null,null,null,20,null,null,null,null," +
				"null,null,null";

		String[] mrs = result.split(",");
		IntStream.range(0, list.size()).forEach(index -> {
			if ("null".equals(mrs[index])) {
				if (list.get(index) != null) {
					System.out.println("Error(null): " + index);
				}
			} else {
				if ((Integer)list.get(index) != Integer.parseInt(mrs[index])) {
					System.out.printf("Error(expect: %d, actual: %d): %d\n", Integer.parseInt(mrs[index]), (Integer)list.get(index), index);
				}
			}
		});

//		String input = "10],[10,13],[3,17],[6,11],[10,5],[9,10],[13],[2,19],[2],[3],[5,25],[8],[9,22],[5,5],[1,30],[11]," +
//				"[9,12],[7],[5],[8],[9],[4,30],[9,3],[9],[10],[10],[6,14],[3,1],[3],[10,11],[8],[2,14],[1],[5],[4]," +
//				"[11,4],[12,24],[5,18],[13],[7,23],[8],[12],[3,27],[2,12],[5],[2,9],[13,4],[8,18],[1,7],[6],[9,29]," +
//				"[8,21],[5],[6,30],[1,12],[10],[4,15],[7,22],[11,26],[8,17],[9,29],[5],[3,4],[11,30],[12],[4,29],[3]," +
//				"[9],[6],[3,4],[1],[10],[3,29],[10,28],[1,20],[11,13],[3],[3,12],[3,8],[10,9],[3,26],[8],[7],[5],[13,17]," +
//				"[2,27],[11,15],[12],[9,19],[2,15],[3,16],[1],[12,17],[9,1],[6,19],[4],[5],[5],[8,1],[11,7],[5,2],[9,28]," +
//				"[1],[2,2],[7,4],[4,22],[7,24],[9,26],[13,28],[11,26";
//		String[] ss = input.split("\\],\\[");
//		for (String s: ss) {
//			if (s.contains(",")) {
//				String[] elems = s.split(",");
//				System.out.printf("list.add(cache.put(%s,%s));\n", elems[0], elems[1]);
//			} else {
//				System.out.printf("list.add(cache.get(%s));\n", s);
//			}
//		}

	}


	//["LRUCache","put","put","put","put","put","get","put","get","get","put","get","put","put","put","get","put","get","get","get","get","put","put","get","get","get","put","put","get","put","get","put","get","get","get","put","put","put","get","put","get","get","put","put","get","put","put","put","put","get","put","put","get","put","put","get","put","put","put","put","put","get","put","put","get","put","get","get","get","put","get","get","put","put","put","put","get","put","put","put","put","get","get","get","put","put","put","get","put","put","put","get","put","put","put","get","get","get","put","put","put","put","get","put","put","put","put","put","put","put"]
	//[
	// [10],[10,13],[3,17],[6,11],[10,5],[9,10],[13],[2,19],[2],[3],[5,25],[8],[9,22],[5,5],[1,30],[11],
	// [9,12],[7],[5],[8],[9],[4,30],[9,3],[9],[10],[10],[6,14],[3,1],[3],[10,11],[8],[2,14],[1],[5],[4],
	// [11,4],[12,24],[5,18],[13],[7,23],[8],[12],[3,27],[2,12],[5],[2,9],[13,4],[8,18],[1,7],[6],[9,29],
	// [8,21],[5],[6,30],[1,12],[10],[4,15],[7,22],[11,26],[8,17],[9,29],[5],[3,4],[11,30],[12],[4,29],[3],
	// [9],[6],[3,4],[1],[10],[3,29],[10,28],[1,20],[11,13],[3],[3,12],[3,8],[10,9],[3,26],[8],[7],[5],[13,17],
	// [2,27],[11,15],[12],[9,19],[2,15],[3,16],[1],[12,17],[9,1],[6,19],[4],[5],[5],[8,1],[11,7],[5,2],[9,28],
	// [1],[2,2],[7,4],[4,22],[7,24],[9,26],[13,28],[11,26]
	// ]
	//

	//[
	//      null,null,null,null,null,null,-1,null,19,17,null,-1,null,null,null,-1,null,-1,5,-1,12,
	//      null,null,3,5,5,null,null,1,null,-1,null,30,5,30,null,null,null,-1,null,-1,24,null,
	//      null,18,null,null,null,null,14,null,null,18,null,null,-1,null,null,null,null,null,18,
	//      null,null,24,null,4,29,30,null,12,-1,null,null,null,null,29,null,null,null,null,-1,-1,
	//      18,null,null,null,24,null,null,null,20,null,null,null,-1,18,18,null,null,null,null,20,
	//      null,null,null,null,null,null,null
	// ]



	//[
	//      null,null,null,null,null,null,-1,null,19,17,null,-1,null,null,null,-1,null,-1,5,-1,12,
	//      null,null,3,5,5,null,null,1,null,-1,null,30,5,30,null,null,null,-1,null,-1,24,null,
	//      null,18,null,null,null,null,-1,null,null,18,null,null,-1,null,null,null,null,null,18,
	//      null,null,-1,null,4,29,30,null,12,-1,null,null,null,null,29,null,null,null,null,17,22,18,null,null,
	//      null,-1,null,null,null,20,null,null,null,-1,18,18,null,null,null,null,20,null,null,null,null,
	//      null,null,null
	// ]

}
