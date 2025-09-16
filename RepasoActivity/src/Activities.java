import java.util.HashMap;
import java.util.Scanner;

public class Activities {

    public void pyramid(){
        int rows = 4;
        for (int i = 1; i <= rows; i++) {

            for (int j = 1; j <= rows + i - 1; j++) {
                if (j <= rows - i) {
                    System.out.print(" "); // spaces
                } else {
                    System.out.print("*"); // stars
                }
            }
            System.out.println();
        }
    }

    public void reverse(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter an integer: ");
        int number = sc.nextInt();

        int original = number;
        int reversed = 0;

        while (number != 0) {
            int digit = number % 10;
            reversed = reversed * 10 + digit;
            number /= 10;
        }

        System.out.println("Reversed number: " + reversed);

        if (original == reversed) {
            System.out.println("It's a palindrome!");
        } else {
            System.out.println("It's not a palindrome.");
        }
    }

    public void riddle(){
        Scanner sc = new Scanner(System.in);
        int secret = (int)(Math.random() * 100) + 1;
        int guess;

        System.out.println("Guess the number between 1 and 100 (enter 0 to quit).");

        do {
            System.out.print("Your guess: ");
            guess = sc.nextInt();

            if (guess == 0) {
                System.out.println("Game over. The number was: " + secret);
                break;
            }

            if (guess == secret) {
                System.out.println("Correct! You guessed it.");
                break;
            } else if (Math.abs(secret - guess) <= 5) {
                System.out.println("Very close!");
            } else if (guess < secret) {
                System.out.println("Too low.");
            } else {
                System.out.println("Too high.");
            }
        } while (guess != secret);
    }

    public void frequency(){
        String[] words = {"Hello", "world", "hello", "World"};

        HashMap<String, Integer> frequency = new HashMap<>();

        for (String word : words) {
            String lower = word.toLowerCase();
            frequency.put(lower, frequency.getOrDefault(lower, 0) + 1);
        }

        for (String key : frequency.keySet()) {
            System.out.println(key + " = " + frequency.get(key));
        }
    }
}
