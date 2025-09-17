package Activities;

public class Pyramid {

    public static void main(String[] args) {
        int rows = 4; // We will create a pyramid with 4 rows.

        // The outer loop handles the rows.
        for (int i = 1; i <= rows; i++) {
            
            // First inner loop: prints the spaces before the stars.
            // The number of spaces decreases with each new row.
            for (int j = 1; j <= rows - i; j++) {
                System.out.print(" ");
            }

            // Second inner loop: prints the stars.
            // The number of stars increases with each new row (2*i - 1).
            for (int k = 1; k <= 2 * i - 1; k++) {
                System.out.print("*");
            }

            // After printing a row, move to the next line.
            System.out.println();
        }
    }
}