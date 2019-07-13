/* *****************************************************************************
 *  Name: Chen Wahng
 *  Date: 19-07-11
 *  Description: The solution to Coursera Algorithms II, Week 1 assignment
 *  Since WildCard is prohibited in this course, I replace all arrays of
 *  collections of generics as collections of collections of generics.
 *  for example, Set<Integer>[] -> List<Set<Integer>>()
 *  Otherwise, use {@code Set<Integer>[] example = new Set<?>[size]} to
 *  simplify the program
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An immutable class to store the data of WordNet
 */
public class WordNet {
    private final Map<String, List<Integer>> wordDict;
    private final SAP sap;
    private final List<String> synsets;

    /**
     * Constructor takes the name of two input files
     *
     * @param synsetsPath   the name of synets files
     * @param hypernymsPath the name of hypernyms files
     */
    public WordNet(String synsetsPath, String hypernymsPath) {
        if (synsetsPath == null || hypernymsPath == null)
            throw new IllegalArgumentException();
        // construct @param synsets and @param wordDict
        In synsetsIn = new In(synsetsPath);
        wordDict = new HashMap<String, List<Integer>>();
        synsets = new ArrayList<>();
        int index;
        while (synsetsIn.hasNextLine()) {
            String line = synsetsIn.readLine();
            String[] fields = line.split(",");
            index = Integer.parseInt(fields[0]);
            synsets.add(fields[1]);
            for (String string : fields[1].split(" ")) {
                if (!wordDict.containsKey(string))
                    wordDict.put(string, new ArrayList<>());
                wordDict.get(string).add(index);
            }
        }

        // construct the digraph according to hypernyms file
        In hypernymsIn = new In(hypernymsPath);
        Digraph hypernyms = new Digraph(synsets.size());
        while (hypernymsIn.hasNextLine()) {
            String[] line = hypernymsIn.readLine().split(",");
            for (int i = 1; i < line.length; ++i) {
                hypernyms.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
            }
        }
        // check whether it's a DAG
        DirectedCycle diCycle = new DirectedCycle(hypernyms);
        if (diCycle.hasCycle()) throw new IllegalArgumentException();
        // check whether it's a single rooted DAG
        int count = 0;
        for (int i = 0; i < hypernyms.V(); ++i) {
            if (hypernyms.outdegree(i) == 0) count++;
        }
        if (count > 1) throw new IllegalArgumentException();
        // construct @param sap
        sap = new SAP(hypernyms);

    }

    /**
     * return all WordNet nouns
     *
     * @return Iterable interface of all nouns
     */
    public Iterable<String> nouns() {
        return wordDict.keySet();
    }

    /**
     * is the word a WordNet noun?
     *
     * @param word the word
     * @return true for noun
     */
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return wordDict.containsKey(word);
    }

    /**
     * distance between nounA and nounB
     *
     * @param nounA word A
     * @param nounB word B
     * @return distance between word A and B
     */
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return sap.length(wordDict.get(nounA), wordDict.get(nounB));
    }

    /**
     * a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB in a
     * shortest path (defined below)
     *
     * @param nounA word A
     * @param nounB word B
     * @return the name of synset
     */
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        int ancestor = sap.ancestor(wordDict.get(nounA), wordDict.get(nounB));
        return synsets.get(ancestor);
    }

    /**
     * main function for unit test
     *
     * @param args cmd args
     */
    public static void main(String[] args) {
        // do unit test here
    }
}
