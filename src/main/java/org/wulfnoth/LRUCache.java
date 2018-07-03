package org.wulfnoth;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.HashMap;

public class LRUCache {

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
			return v.value;
		}
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

	public void put(int key, int value) {
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
	}

	public static void main(String[] args) {
		LRUCache cache = new LRUCache( 2 /* capacity */ );

		cache.put(1, 1);
		cache.put(2, 2);
		System.out.println(cache.get(1)); //1
		cache.put(3, 3);    // evicts key 2
		System.out.println(cache.get(2)); // returns -1 (not found)
		cache.put(4, 4);    // evicts key 1
		System.out.println(cache.get(1));// returns -1 (not found)
		System.out.println(cache.get(3));// returns 3
		System.out.println(cache.get(4));// returns 4
		System.out.println(cache.get(2));
	}

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
	//      null,null,3,5,5,null,null,1,null,-1,null,30,5,30,null,null,null,-1,null,-1,24,null,null,
	//      18,null,null,null,null,-1,null,null,18,null,null,-1,null,null,null,null,null,18,null,null,
	//      -1,null,4,29,30,null,12,-1,null,null,null,null,29,null,null,null,null,17,22,18,null,null,
	//      null,-1,null,null,null,20,null,null,null,-1,18,18,null,null,null,null,20,null,null,null,null,
	//      null,null,null
	// ]

}
