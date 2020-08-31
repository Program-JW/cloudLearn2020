package liKou;

import java.util.Arrays;
import java.util.Comparator;

public class test {


    static int respace(String[] dictionary, String sentence) {
        //对数组进行重新排序
//        Arrays.sort(dictionary, new Comparator<String>() {
//            public int compare(String o1, String o2) {
//                return o1.length() > o2.length() ? -1 : 1;
//            }
//        });


        Arrays.sort(dictionary, (String o1, String o2) -> {
            return o1.length() > o2.length() ? -1 : 1;
        });

        for (int i = 0; i < dictionary.length; i++) {
            System.out.println(dictionary[i]);
        }

        for (int i = 0; i < dictionary.length; i++) {

            // 如何实现最长匹配原则
            if (sentence.contains(dictionary[i])) {
                sentence = sentence.replaceAll(dictionary[i], "");
            }
        }
        System.out.println(sentence);

        return sentence.length();

    }


    static int lengthOfLongestSubstring(String s) {
        if (s.length() <= 1) {
            return s.length();
        }
        int dp[] = new int[s.length() + 1];
        dp[0] = 1;
        for (int i = 1; i < s.length(); i++) {
            //int large = 0;
            String mid = "";
            for (int j = i; j >= 0; j--) {
                //System.out.println(s.charAt(j));
                if (!mid.contains(s.charAt(j) + "")) {
                    mid = mid + s.charAt(j);
                } else {
                    break;
                }

            }
            dp[i]=Math.max(dp[i-1],mid.length());

        }
        for (int i = 0; i < dp.length; i++) {
            System.out.println(dp[i]);
        }

        return dp[s.length()-1];
    }

    public static void main(String[] args) {

//        String[] dictionary = {"looked", "just", "like", "her", "brother"};
//        String sentence = "jesslookedjustliketimherbrother";
//        System.out.println(respace(dictionary, sentence));
        String s="abcabcbb";
        System.out.println(lengthOfLongestSubstring(s));


    }

}
