package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;
 
abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; // the frequency of this tree
    public HuffmanTree(int freq) { frequency = freq; }
 
    // compares on the frequency
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}
 
class HuffmanLeaf extends HuffmanTree {
    public final String value; // the character this leaf represents
    public final int length;
 
    public HuffmanLeaf(int freq, String val, int len) {
        super(freq);
        value = val;
        length = len;
    }
}
 
class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; // subtrees
 
    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}
 
public class HuffmanCode {
    // input is an array of frequencies, indexed by character code
    public static HuffmanTree buildTree1(int[] charFreqs) {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        // initially, we have a forest of leaves
        // one for each non-empty character
        for (int i = 0; i < charFreqs.length; i++)
        		if (charFreqs[i] > 0)
        			trees.offer(new HuffmanLeaf(charFreqs[i], "" + (char)i, 2));
 
        assert trees.size() > 0;
        // loop until there is only one tree left
        while (trees.size() > 1) {
            // two trees with least frequency
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();
 
            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }
    
    // input is an array of frequencies, indexed by character code
    public static HuffmanTree buildTree2(int[][] charFreqs) {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        // initially, we have a forest of leaves
        // one for each non-empty character
        for (int i = 0; i < charFreqs.length; i++)
        	for (int j = 0; j < charFreqs.length; j++)
        		if (charFreqs[i][j] > 0)
        			trees.offer(new HuffmanLeaf(charFreqs[i][j], "" + (char)i + (char)j, 2));
 
        assert trees.size() > 0;
        // loop until there is only one tree left
        while (trees.size() > 1) {
            // two trees with least frequency
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();
 
            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }
    
    // input is an array of frequencies, indexed by character code
    public static HuffmanTree buildTree3(int[][][] charFreqs) {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        // initially, we have a forest of leaves
        // one for each non-empty character
        for (int i = 0; i < charFreqs.length; i++)
        	for (int j = 0; j < charFreqs.length; j++)
            	for (int k = 0; k < charFreqs.length; k++)
            		if (charFreqs[i][j][k] > 0)
            			trees.offer(new HuffmanLeaf(charFreqs[i][j][k], "" + (char)i + (char)j + (char)k, 2));
 
        assert trees.size() > 0;
        // loop until there is only one tree left
        while (trees.size() > 1) {
            // two trees with least frequency
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();
 
            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }
 
    public static int printCodes(HuffmanTree tree, StringBuffer prefix, int counter) {
    	assert tree != null;
        if (tree instanceof HuffmanLeaf) {
        	//counter++;
            HuffmanLeaf leaf = (HuffmanLeaf)tree;
        	counter += prefix.length()*leaf.frequency;
 
            // print out character, frequency, and code for this leaf (which is just the prefix)
            System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);
 
        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode)tree;
 
            // traverse left
            prefix.append('0');
            counter = printCodes(node.left, prefix, counter);
            prefix.deleteCharAt(prefix.length()-1);
 
            // traverse right
            prefix.append('1');
            counter = printCodes(node.right, prefix, counter);
            prefix.deleteCharAt(prefix.length()-1);
        }
        return counter;
    }
 
    public static void main(String[] args) throws FileNotFoundException {
        String test = new String();
        Scanner scanner = new Scanner(new File("seneca.txt"));
        while (scanner.hasNextLine()){
            test = test.concat(scanner.nextLine());
        }
        scanner.close();
        int przed = test.length()*8;
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        int length = 2;

        // we will assume that all our characters will have
        // code less than 256, for simplicity
        int[] charFreqs1 = new int[512];
        int[][] charFreqs2 = new int[512][512];
        int[][][] charFreqs3 = new int[512][512][512];
        // read each character and record the frequencies
        int i = 0;
        char f = 'a';
        char g = 'a';
        for (char c : test.toCharArray()) {
        	if (i == 0) {
        		f = c;
        		i++;
        		continue;
        	}
        	i--;
            charFreqs2[f][c]++;
        }
        i = 0;
        for (char c : test.toCharArray()) {
        	if (i == 0) {
        		f = c;
        		i++;
        		continue;
        	} 
        	if (i == 1){
        		g = c;
        		i++;
        		continue;
        	}
        	i = 0;
            charFreqs3[f][g][c]++;
        }
        for (char c : test.toCharArray()) {
            charFreqs1[c]++;
        }
 
        // build tree
        HuffmanTree tree1 = buildTree1(charFreqs1);
        HuffmanTree tree2 = buildTree2(charFreqs2);
        HuffmanTree tree3 = buildTree3(charFreqs3);
 
        // print out results
        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        counter1 = printCodes(tree1, new StringBuffer(), counter1);
        counter2 = printCodes(tree2, new StringBuffer(), counter2);
        counter3 = printCodes(tree3, new StringBuffer(), counter3);
        System.out.println("przed: " + przed);
        System.out.println("po 1: " + counter1);
        System.out.println("po 2: " + counter2);
        System.out.println("po 3: " + counter3);
        System.out.println("k1: " + ((float)przed - (float)counter1)/(float)przed);
        System.out.println("k2: " + ((float)przed - (float)counter2)/(float)przed);
        System.out.println("k3: " + ((float)przed - (float)counter3)/(float)przed);
    }
}