package org.wulfnoth;

import java.util.Stack;

public class BasicCaculator {

	public static void main(String[] args) {
		System.out.println(new Solution().calculate("1 + 1"));
	}

}

class Solution {

	class Node {
		final boolean isSymbol;
		char symbol = '?';
		int value = -1;
		Node next = null;
		Node last = null;

		Node(int value) {
			this.value = value;
			this.isSymbol = false;
		}

		Node(char symbol) {
			this.symbol = symbol;
			this.isSymbol = true;
		}

		void add(Node other) {
			value += other.value;
		}

		void minus(Node other) {
			value -= other.value;
		}

		@Override
		public String toString() {
			if (isSymbol) {
				return symbol + "";
			} else {
				return value + "";
			}
		}
	}

	class CalList {

		Node head;
		Node tail;
		int size = 0;

		void add(Node node) {
			if (size == 0) {
				head = node;
				tail = node;
			} else {
				tail.next = node;
				node.last = tail;
				tail = node;
			}
			size++;
		}

		void remove(Node node) {
			if (size == 1) {
				head = tail = null;
			} else if (node == head) {
				node.next.last = null;
				head = node.next;
			} else if (node == tail) {
				node.last.next = null;
				tail = node.last;
			} else {
				node.last.next = node.next;
				node.next.last = node.last;
			}
			size--;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			if (head != null) {
				Node tmp = head;
				while (tmp != null) {
					sb.append(tmp);
					tmp = tmp.next;
				}
			}
			return sb.toString();
		}

	}

	public int calculate(String s) {
		Stack<Character> stack = new Stack<>();
		CalList list = new CalList();
		boolean isRightParenthesis = false;

		int i = 0;
//		while (i < s.length()) {
//			char c = s.charAt(i);
//			int value = 0;
//			while (c >= '0' && c <= '9' || c == ' ') {
//				if (c != ' ') {
//					value += 0
//				}
//			}
//		}

//		for (char c: s.toCharArray()) {
//			if (c >= '0' && c <= '9') {
//				lastValue *= 10;
//				lastValue += c - '0';
//			} else if (c == '+' || c == '-') {
//				if (!isRightParenthesis) {
//					list.add(new Node(lastValue));
//					lastValue = 0;
//				}
//				while (!stack.empty() && '(' != stack.peek())
//					list.add(new Node(stack.pop()));
//				stack.push(c);
//				isRightParenthesis = false;
//			} else if (c == '(') {
//				stack.push(c);
//			} else if (c == ')') {
//				isRightParenthesis = true;
//				list.add(new Node(lastValue));
//				lastValue = 0;
//				while (!stack.empty() && '(' != stack.peek())
//					list.add(new Node(stack.pop()));
//				if (!stack.empty())
//					stack.pop();
//			}
//		}

		while (!stack.empty() && '(' != stack.peek())
			list.add(new Node(stack.pop()));

		System.out.println(list);

		Node iter = list.head;
		while (iter != null) {
			if (iter.isSymbol) {
				Node v1 = iter.last.last;
				Node v2 = iter.last;
				if (iter.symbol == '+') v1.add(v2);
				else v1.minus(v2);

				iter = iter.next;
				list.remove(v2);
				list.remove(v2.next);
//				System.out.println(list);
			} else {
				iter = iter.next;
			}
		}

		return list.head.value;
	}
}