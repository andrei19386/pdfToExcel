package main;

import org.apache.lucene.morphology.LuceneMorphology;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Lemmatizer {

    private final LuceneMorphology luceneMorph;

    public Lemmatizer(LuceneMorphology luceneMorph) {
        this.luceneMorph = luceneMorph;
    }

    public  void analyzer(String string,Map<String,Integer> lemmaMap) {
        String[] words = stringToWords(string); //Разбиваем блок на отдельные слова

        for(int i = 0; i < words.length; i++) {
                putIntoMap(words[i],lemmaMap);
        }
    }


    private  void putIntoMap(String lemmaString, Map<String,Integer> lemmaMap) {

        if(lemmaString == null || lemmaString.isEmpty() || isOfficialWord(lemmaString)) {
            return;
        }
        String lemmaNormal = getLemmas(lemmaString);
        if(lemmaMap.containsKey(lemmaNormal)) {
            int count = lemmaMap.get(lemmaNormal);
            lemmaMap.put(lemmaNormal,++count);
        } else {
            lemmaMap.put(lemmaNormal,1);
        }
    }

    public String getLemmas(String word) {
        if(luceneMorph == null) {
            return "";
        }
        String wordLower = word.toLowerCase(Locale.ROOT);
        if(!checkWord(wordLower)) {
            return "";
        }
        List<String> wordBaseForms =
                luceneMorph.getNormalForms(wordLower);
        if(wordBaseForms != null && !wordBaseForms.isEmpty()) {
            return wordBaseForms.get(0);
        }
        return "";
    }

    public boolean checkWord(String word) {
        if(!word.matches("[-а-яА-Я]+")) {
            return false;
        }
        return true;
    }

    public boolean isOfficialWord(String word) {
        String wordLower = word.toLowerCase(Locale.ROOT);
        if(!checkWord(wordLower)) {
            return true;
        }
        List<String> wordBaseForms =
                luceneMorph.getMorphInfo(wordLower);

        if(wordBaseForms.size() == 1) {
            String type = (wordBaseForms.get(0).split("[ ]"))[1];
            if (type.equals("ЧАСТ") ||
                type.equals("СОЮЗ") ||
                type.equals("МЕЖД") ||
                type.equals("ПРЕДЛ")
            ) {
                return true;
            }
        }
        return false;
    }

        public static String[] stringToWords(String string){
        String newString = string.replaceAll("[.,:?/+©&%#@!;—]","");
        String[] words = stringToWordsSimple(newString);

        return words;
    }

        public static String[] stringToWordsSimple(String string){
        String newString = string.replaceAll("[\n]"," ");
        newString = newString.replaceAll("[ ]+"," ");
        String[] words = newString.trim().split("[ ]");

        return words;
    }

    public static void main(String[] args) {

    }

}
