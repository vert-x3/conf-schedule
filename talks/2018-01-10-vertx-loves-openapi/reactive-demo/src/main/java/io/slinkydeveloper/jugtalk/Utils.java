package io.slinkydeveloper.jugtalk;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Francesco Guardiani @slinkydeveloper
 */
public class Utils {

    // Thank you StackOverflow
    public static String[] generateRandomWords(int numberOfWords)
    {
        String[] randomStrings = new String[numberOfWords];
        Random random = new Random();
        for(int i = 0; i < numberOfWords; i++)
        {
            char[] word = new char[random.nextInt(8)+3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
            for(int j = 0; j < word.length; j++)
            {
                word[j] = (char)('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }

    public static List<URL> fillURLsList() throws MalformedURLException {
        List<URL> list = new ArrayList<>();
        list.add(new URL("https://www.google.it/"));
        list.add(new URL("https://slinkydeveloper.github.io"));
        list.add(new URL("https://twitter.com"));
        list.add(new URL("https://facebook.com"));
        list.add(new URL("https://mvnrepository.com"));
        return list;
    }

}
