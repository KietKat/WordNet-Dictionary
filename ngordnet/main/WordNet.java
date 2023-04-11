package ngordnet.main;

import edu.princeton.cs.algs4.In;

import java.util.*;


public class WordNet {
    public TreeMap<String, ArrayList<Node>> wordToKey = new TreeMap<String, ArrayList<Node>>();

    public TreeMap<Integer, Node> numberToWord = new TreeMap<Integer, Node>();


    public WordNet(String synSet, String hyponymsFiles) {

        // Adding Stuff Here

        synSetParser(synSet);
        parseHyponyms(hyponymsFiles);

    }


    private void synSetParser(String synSet) {
        List<String> lines = readFile(synSet);
        for (String line : lines) {
            String[] items = line.split(",");
            int key = Integer.parseInt(items[0]);
            String[] words = parseContent(items[1]);
            Node temp = new Node(words);
            for (String word : words) {
                if (!wordToKey.containsKey(word)) {
                    ArrayList temporary = new ArrayList<Node>();
                    temporary.add(temp);
                    wordToKey.put(word, temporary);
                } else {
                    wordToKey.get(word).add(temp);
                }
            }
            numberToWord.put(key, temp);
        }
    }

    private void parseHyponyms(String hyponymsFiles) {
        List<String> lines = readFile(hyponymsFiles);
        for (String line : lines) {
            String[] items = line.split(",");
            int key = Integer.parseInt(items[0]);
            for (int i = 1; i < items.length; i++) {
                int num = Integer.parseInt(items[i]);
                //Node temp = numberToWord

                numberToWord.get(key).getChildList().add(numberToWord.get(num));
            }
        }
    }


    public boolean isInWordsFile(String word) {
        return wordToKey.containsKey(word);
    }

    // read file by the lines
    private static List<String> readFile(String filename) {
        In in = new In(filename);
        return List.of(in.readAllLines());
    }

    // tokenize each lines by comma
    private String[] parseContent(String words) {
        return words.split(" ");
    }

    //find all the nodes contain a word
    public ArrayList<Node> getNode(String givenWord) {
        ArrayList<Node> result = wordToKey.get(givenWord);
        return result;
    }

    /* Methods in case user search the word by its index number
    public ArrayList<Node> getNodeFromIndex(int index) {
        ArrayList<Node> result = numberToWord.get(index).getChildList();
        return result;
    }

    public ArrayList<String> getWord(Node n) {
        ArrayList<String> result = n.words;
        Collections.sort(result);
        return result;
    }

     */

    public Set<String> getHyponymsOf(String key) {
        Set<String> result = new TreeSet<>();
        ArrayList<Node> nodes = getNode(key);
        if (nodes == null) {
            return result;
        }
        //recursive call to get add all the nodes and their parents into the arraylist.
        for (Node node : nodes) {
            getWordHelper(node, result);
        }
        return result;
    }

    private void getWordHelper(Node node, Set<String> words) {
        if (node.words != null) {
            words.addAll(node.words);
        }
        for (Node child : node.children) {
            getWordHelper(child, words);
        }
    }


    /* class node:
        Attributes:
            +words: ArrayList<String> -> store its content
            +children: ArrayList<Node> -> store its children
        Methods:
            +Node(Str[] str) -> initiate with a list of word (node content)
    */
    class Node {
        public ArrayList<String> words = new ArrayList<>();
        public ArrayList<Node> children = new ArrayList<>();

        public Node(String[] str) {
            words = new ArrayList<>(List.of(str));
        }

        public ArrayList<Node> getChildList() {
            return children;
        }

        public String Words() {
            String temp = "";
            for (String word : words) {
                temp = temp + word + " ";
            }
            return temp;
        }
    }
}

