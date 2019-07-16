/* *****************************************************************************
 *  Name: Chen Wang
 *  Date: Jul 15, 2019
 *  Description: Assignment solution of Queues
 *  Usage: javac-algs4 Deque.java
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A double-ended queue that supports adding or removing items *  from both sides
 *
 * @param <Item> type parameter of items in queue
 */
public class Deque<Item> implements Iterable<Item> {
    private final Node front; // pointer to the front
    private final Node end; // pointer to the end
    private int count; // counter of deque size

    /**
     * inner class Node
     */
    private class Node {
        private final Item item; // value of current node
        private Node next; // pointer to next node
        private Node pre; // pointer to previous node

        /**
         * constructor, O(1)
         *
         * @param item item contained in this node
         */
        public Node(Item item) {
            this.item = item;
            this.next = null;
            this.pre = null;
        }
    }

    /**
     * inner class DequeIterator, iterates from front to end
     */
    private class DequeIterator implements Iterator<Item> {
        private Node start; // pointer to the front node

        /**
         * constrcutor, O(1)
         *
         * @param node: pointer to the front node
         */
        public DequeIterator(Node node) {
            start = node; // 8 bytes
        }

        /**
         * whether reach to the end, O(1)
         *
         * @return true for more nodes following, vice versa
         */
        public boolean hasNext() {
            return start.next.item != null;
        }

        /**
         * return the next item from front to end, O(1)
         *
         * @return the next item
         */
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = start.next.item; // 8 bytes
            start = start.next;
            return item;
        }
    }

    /**
     * constructor, O(1)
     */
    public Deque() { // 16 bytes
        // for each Deque item, we have 48 bytes
        front = new Node(null); // 16 bytes
        end = new Node(null); // 16 bytes
        front.next = end; // 8 bytes
        end.pre = front; // 8 bytes
    }

    /**
     * Is the deque empty? O(1)
     *
     * @return true for empty, vice versa
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * return the number of elements on the deque, O(1)
     *
     * @return number of elements on the deque
     */
    public int size() {
        return count;
    }

    /**
     * add the item to the front, O(1)
     *
     * @param item: item to be added
     */
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node node = new Node(item); // 16 bytes
        node.next = front.next;
        front.next.pre = node;
        front.next = node;
        node.pre = front;
        count++;
    }


    /**
     * add the item to the end, O(1)
     *
     * @param item: item to be added
     */
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node node = new Node(item); // 16 bytes
        node.pre = end.pre;
        end.pre.next = node;
        end.pre = node;
        node.next = end;
        count++;
    }

    /**
     * remove and return the item from the front, O(1)
     *
     * @return the removed node from the front
     */
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Node node = front.next; // 8 bytes
        node.next.pre = front;
        front.next = node.next;
        node.next = null;
        node.pre = null;
        count--;
        return node.item;
    }

    /**
     * remove and return the item from the end, O(1)
     *
     * @return the removed node from the end
     */
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Node node = end.pre; // 8 bytes
        node.pre.next = end;
        end.pre = node.pre;
        node.next = null;
        node.pre = null;
        count--;
        return node.item;
    }

    /**
     * return an iterator over items in order from front to end, O(1)
     *
     * @return an iterator over items from front to end
     */
    public Iterator<Item> iterator() {
        return new DequeIterator(front); // 16 bytes
    }

    /**
     * main method for unit test
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // do unit test here
    }
}
