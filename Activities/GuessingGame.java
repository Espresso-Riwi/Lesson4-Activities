package Activities;

import java.util.Random;
import java.util.Scanner;

public class GuessingGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int secretNumber = random.nextInt(100) + 1;
        int guess;

        System.out.println("I've picked a number between 1 and 100. Try to guess it!");

        // The do-while loop ensures the game runs at least once.
        do {
            System.out.print("Enter your guess: ");
            guess = scanner.nextInt();

            if (guess == secretNumber) {
                System.out.println("Congratulations! You guessed the number.");
            } else if (guess > secretNumber) {
                System.out.println("Too high.");
            } else {
                System.out.println("Too low.");
            }

            // Check if the guess is very close.
            if (Math.abs(guess - secretNumber) <= 5 && guess != secretNumber) {
                System.out.println("You are very close! (±5)");
            }

        } while (guess != secretNumber); // The loop continues until the correct number is guessed.

        scanner.close();
    }
}