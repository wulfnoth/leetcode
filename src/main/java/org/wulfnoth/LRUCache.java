package org.wulfnoth;

import java.util.HashMap;

class LRUCache {

	private static int id = 0;

	class Node {
		int timestamp;
		int key;
		int position;

		Node(int key) {
			this.key = key;
			updateTimestamp();
		}

		void updateTimestamp() {
			this.timestamp = id++;
		}

		@Override
		public int hashCode() {
			return key;
		}
	}

	int size = 0;
	Node[] heap; //minimum heap

	HashMap<Integer, Integer> cache = new HashMap<>();
	HashMap<Integer, Node> map = new HashMap<>();

	public LRUCache(int capacity) {
		heap = new Node[capacity];
	}

	public int get(int key) {
		int v = cache.getOrDefault(key, -1);
		if (v != -1) {
			Node node = map.get(key);
			node.updateTimestamp();
			downcast(node);
		}
		return v;
	}

	public void downcast(Node node) {

	}

	public void put(int key, int value) {
		if(map.containsKey(key)) {
			Node node = map.get(key);
			node.updateTimestamp();
			downcast(node);
			cache.put(key, value);
		}
	}

}
