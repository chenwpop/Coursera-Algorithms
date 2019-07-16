/* *****************************************************************************
 *  Name: Chen Wang
 *  Date: Apr 21, 2019
 *  Description: Assignment solution to Queues
 *  Usage: javac-algs4 RandomizedQueue.java
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Randomized iteratable queue based on resizing array
 *
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] resizingArray; // resizing array to store data
    private int pointer; // pointer to current index

    /**
     * Randomized queue iterator
     */
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int pointer; // pointer to next element
        private final int[] index; // array of shuffle indices

        /**
         * constructor, O(n)
         *
         * @param capacity: current size of randomized queue
         */
        public RandomizedQueueIterator(int capacity) {
            index = new int[capacity];
            for (int i = 0; i < capacity; ++i) index[i] = i;
            StdRandom.shuffle(index);
        }

        /**
         * whether reach to the end, O(1)
         *
         * @return true for not, vice verse
         */
        public boolean hasNext() {
            return pointer != index.length;
        }

        /**
         * what's the next element? O(1)
         *
         * @return next element
         */
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return resizingArray[index[pointer++]];
        }
    }

    /**
     * constructor, O(n)
     */
    public RandomizedQueue() {
        resizingArray = (Item[]) new Object[2];
        pointer = -1;
    }

    /**
     * whether the queue is empty? O(1)
     *
     * @return true for empty, vice versa
     */
    public boolean isEmpty() {
        return pointer == -1;
    }

    /**
     * what's the size of queue? O(1)
     *
     * @return size of the queue
     */
    public int size() {
        return pointer + 1;
    }


    private void resize(int newCapacity) {
        Item[] newArray = (Item[]) new Object[newCapacity];
        for (int i = 0; i <= pointer; ++i) newArray[i] = resizingArray[i];
        resizingArray = newArray;
    }

    /**
     * enqueue an item, amortized O(1)
     *
     * @param item: the item to be enqueued
     */
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (pointer == resizingArray.length - 1) resize(2 * resizingArray.length);
        resizingArray[++pointer] = item;
    }

    /**
     * dequeue a random item, O(1)
     *
     * @return: the dequeued item
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        // generate a random index from [0, pointer]
        int index = StdRandom.uniform(pointer + 1);
        // switch the random element and the last element
        Item temp = resizingArray[index];
        resizingArray[index] = resizingArray[pointer];
        resizingArray[pointer--] = null;
        // resize array
        if (pointer > -1 && pointer + 1 <= resizingArray.length / 4)
            resize(resizingArray.length / 2);
        return temp;
    }

    /**
     * @return a random item, O(1)
     */
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return resizingArray[StdRandom.uniform(pointer + 1)];
    }

    /**
     * generate a random iterator, O(n)
     *
     * @return a random iterator
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(pointer + 1);
    }

    /**
     * main method for unit testing
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // do unit test here
    }
}
