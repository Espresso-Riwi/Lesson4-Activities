package Activities;

import java.util.Scanner;
import java.util.HashMap;

public class WordFrequency {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        // We use a HashMap to store the words and their counts.
        HashMap<String, Integer> wordCount = new HashMap<>();

        System.out.println("Enter words. Type 'end' to see the results.");
        
        // This loop will run forever until the user types "end".
        while (true) {
            System.out.print("Enter a word: ");
            String input = scanner.next();
            
            // We convert the input to lowercase to count "Hola" and "hola" as the same word.
            String word = input.toLowerCase();

            // We check if the user typed "end" to stop the program.
            if (word.equals("end")) {
                break;
            }

            // We update the word count in the HashMap.
            if (wordCount.containsKey(word)) {
                // If the word exists, we increase its count.
                wordCount.put(word, wordCount.get(word) + 1);
            } else {
                // If it's a new word, we add it with a count of 1.
                wordCount.put(word, 1);
            }
        }

        // We print the final results.
        System.out.println("\n--- Word Frequencies ---");
        for (String key : wordCount.keySet()) {
            System.out.println(key + ": " + wordCount.get(key));
        }

        scanner.close(); // Remember to close the scanner.
    }
}