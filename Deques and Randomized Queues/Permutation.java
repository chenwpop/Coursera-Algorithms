import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/* *****************************************************************************
 *  Name: Chen
 *  Date: Apr 21, 2019
 *  Description: Permutation based on RandomizedQueue
 *  Usage: javac-algs4 Permutation.java
 *         java-algs4 Permutation [output capacity]
 **************************************************************************** */

/**
 * functional class to generate random permutations with RandomizedQueue
 */
public class Permutation {
    public static void main(String[] args) {
        int capacity = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        // Reservoir Algorithm
        for (int i = 0; i < capacity; ++i) randomizedQueue.enqueue(StdIn.readString());
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            int index = StdRandom.uniform(capacity++ + 1);
            if (index < randomizedQueue.size()) {
                randomizedQueue.dequeue();
                randomizedQueue.enqueue(item);
            }
        }
        for (String string : randomizedQueue)
            StdOut.println(string);
    }
}
