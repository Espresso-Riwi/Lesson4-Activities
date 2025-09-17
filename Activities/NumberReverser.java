package Activities; 

import java.util.Scanner;

public class NumberReverser {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an integer: ");
        int originalNumber = scanner.nextInt();
        int number = originalNumber;
        int reversedNumber = 0;

        // Use a while loop to reverse the number.
        // The loop continues as long as the number is not 0.
        while (number != 0) {
            int lastDigit = number % 10; // Get the last digit of the number.
            reversedNumber = reversedNumber * 10 + lastDigit; // Build the new reversed number.
            number = number / 10; // Remove the last digit from the original number.
        }

        System.out.println("The reversed number is: " + reversedNumber);

        // Check if the number is a palindrome (capicúa).
        if (originalNumber == reversedNumber) {
            System.out.println("It is a palindromic!");
        } else {
            System.out.println("It is not a palindromic.");
        }

        scanner.close();
    }
}