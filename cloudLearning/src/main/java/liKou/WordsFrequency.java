package liKou;

import java.util.HashMap;

public class WordsFrequency {

    public static void main(String[] args) {
        WordsFrequency wordsFrequency = new WordsFrequency(new String[]{"i", "have", "an", "apple", "he", "have", "a", "pen"});
        System.out.println(wordsFrequency.get("you")); //返回0，"you"没有出现过
        System.out.println(wordsFrequency.get("have"));; //返回2，"have"出现2次
        wordsFrequency.get("an"); //返回1
        wordsFrequency.get("apple"); //返回1
        wordsFrequency.get("pen"); //返回1
    }

    private static HashMap<String, Integer> map = new HashMap<>();

    public WordsFrequency(String[] book) {

        for (int i = 0; i < book.length; i++) {
            if (map.get(book[i]) != null) {
                map.put(book[i], map.get(book[i]) + 1);
            } else {
                map.put(book[i], 1);
            }
        }

    }

    public int get(String word) {
        return map.get(word) == null ? 0 : map.get(word);

    }


}
