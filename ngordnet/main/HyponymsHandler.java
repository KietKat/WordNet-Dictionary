package ngordnet.main;


import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.*;


public class HyponymsHandler extends NgordnetQueryHandler {
    private WordNet wm;
    private NGramMap ngramMap;

    public HyponymsHandler(WordNet wm, NGramMap ngramMap) {
        this.wm = wm;
        this.ngramMap = ngramMap;
    }

    private Set<String> intersection(Set<String> setOne, Set<String> setTwo) {
        Set<String> result = new TreeSet<>();
        for (String word : setTwo) {
            if (setOne.contains(word)) {
                result.add(word);
            }
        }
        return result;
    }


    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();

        Set<String> wordList = wm.getHyponymsOf(words.get(0));
        Map<Double, Set<String>> popularity;

        for (int i = 1; i < words.size(); i++) {
            wordList = intersection(wordList, wm.getHyponymsOf(words.get(i)));
        }

        /* Handle 3 cases:
               k < 0 -> illegalinputException, just return nothing or throws an exception!
               k > 0 -> return the limit k number of words.
               k == 0 -> no restriction, just return all.
         */
        if (k < 0) { return "[]"; }
        else if ( k == 0 ) { return wordList.toString(); }
        else {
            popularity = getWordsWithPopularity(startYear, endYear, wordList);
            wordList = handlingK(popularity, k);
        }

        return wordList.toString();
    }

    private Map<Double, Set<String>> getWordsWithPopularity(int startYear, int endYear, Set<String> hyponyms) {
        Map<Double, Set<String>> result = new TreeMap<>(Collections.reverseOrder());
        for (String hyponym: hyponyms) {
            // call time series to get the history by year
            TimeSeries ts = ngramMap.countHistory(hyponym, startYear, endYear);
            // add the history together
            double count = 0;
            for (Double num: ts.values()) {
                count += num;
            }

            //Since TreeMap can't be sorted by value, instead we use the count as key and group all the words that have the same count
            if (count > 0.0) {
                if (result.containsKey(count)) {
                    //if the count has already in result, just add the word into it
                    result.get(count).add(hyponym);
                } else {
                    //or else create a new collection for that key
                    Set<String> temp = new TreeSet<>();
                    temp.add(hyponym);
                    result.put(count, temp);
                }
            }
        }
        return result;
    }

    // return a list contains k words. If k > sortedWord size, return all element.
    // If k < sortedWord size, pick base on the popularity -> alphabetical order inside the same tree node
    private Set<String> handlingK(Map<Double, Set<String>> sortedWord, int k) {
        Set<String> result = new TreeSet<>();
        for (Double num: sortedWord.keySet()) {
            for (String word : sortedWord.get(num)) {
                result.add(word);
                k--;
                if (k == 0) {
                    break;
                }
            }
            if (k == 0) { break; }
        }
        return result;
    }
}