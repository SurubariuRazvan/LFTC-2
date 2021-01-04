package model;

import java.util.ArrayList;
import java.util.List;

public class MyTreeMap<K extends Comparable<K>, V> {
    private class Node {
        K key;
        V value;
        Node left;
        Node right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.right = null;
            this.left = null;
            size++;
        }
    }

    private class StackNode {
        Node value;
        StackNode next;

        public StackNode(Node value, StackNode next) {
            this.value = value;
            this.next = next;
        }
    }

    private class Stack {
        StackNode head;

        public Stack() {
            this.head = null;
        }

        void push(Node node) {
            this.head = new StackNode(node, this.head);
        }

        Node pop() {
            if (isEmpty())
                return null;
            else {
                Node node = this.head.value;
                this.head = this.head.next;
                return node;
            }
        }

        boolean isEmpty() {
            return this.head == null;
        }
    }

    private Node root;
    private int size;

    public MyTreeMap() {
        this.root = null;
        this.size = 0;
    }

    public V put(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
            return null;
        }
        Node current = root;
        while(true)
            if (key.compareTo(current.key) < 0) {
                if (current.left == null) {
                    current.left = new Node(key, value);
                    return null;
                }
                current = current.left;
            } else if (key.compareTo(current.key) > 0) {
                if (current.right == null) {
                    current.right = new Node(key, value);
                    return null;
                }
                current = current.right;
            } else
                return value;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V get(K key) {
        Node current = root;
        while(current != null)
            if (key.compareTo(current.key) < 0)
                current = current.left;
            else if (key.compareTo(current.key) > 0)
                current = current.right;
            else
                return current.value;
        return null;
    }

    public int size() {
        return size;
    }

    public List<K> keySet() {
        Stack stack = new Stack();
        Node current = root;
        List<K> list = new ArrayList<>();
        while(current != null || !stack.isEmpty()) {
            while(current != null) {
                stack.push(current);
                current = current.left;
            }
            current = stack.pop();
            list.add(current.key);
            current = current.right;
        }
        return list;
    }
}
