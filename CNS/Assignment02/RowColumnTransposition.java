
package CNS.Assignment02;

import java.util.Arrays;
import java.util.Comparator;

public class RowColumnTransposition {

    // Helper class to store character and its original index for sorting
    static class KeyChar {
        char character;
        int originalIndex;

        KeyChar(char character, int originalIndex) {
            this.character = character;
            this.originalIndex = originalIndex;
        }
    }

    // Encrypts a message using the Row and Column Transposition cipher
    public static String encrypt(String text, String key) {
        String plaintext = text.replace(" ", "");
        int numCols = key.length();
        int numRows = (int) Math.ceil((double) plaintext.length() / numCols);

        // Pad the plaintext with a character (e.g., 'X') to fill the matrix
        while (plaintext.length() < numRows * numCols) {
            plaintext += 'X';
        }

        char[][] matrix = new char[numRows][numCols];
        int index = 0;
        // Fill the matrix row by row
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                matrix[r][c] = plaintext.charAt(index++);
            }
        }

        // Get the order of columns based on the alphabetical order of the key
        KeyChar[] keyChars = new KeyChar[numCols];
        for (int i = 0; i < numCols; i++) {
            keyChars[i] = new KeyChar(key.charAt(i), i);
        }
        Arrays.sort(keyChars, Comparator.comparing(kc -> kc.character));

        StringBuilder ciphertext = new StringBuilder();
        // Read the matrix column by column in the new sorted order
        for (KeyChar kc : keyChars) {
            int originalColIndex = kc.originalIndex;
            for (int r = 0; r < numRows; r++) {
                ciphertext.append(matrix[r][originalColIndex]);
            }
        }

        return ciphertext.toString();
    }

    // Decrypts a message using the Row and Column Transposition cipher
    public static String decrypt(String cipher, String key) {
        int numCols = key.length();
        int numRows = cipher.length() / numCols;

        char[][] matrix = new char[numRows][numCols];

        // Get the original column order from the key
        KeyChar[] keyChars = new KeyChar[numCols];
        for (int i = 0; i < numCols; i++) {
            keyChars[i] = new KeyChar(key.charAt(i), i);
        }
        Arrays.sort(keyChars, Comparator.comparing(kc -> kc.character));

        // Fill the matrix column by column based on the key's original order
        int cipherIndex = 0;
        for (KeyChar kc : keyChars) {
            int originalColIndex = kc.originalIndex;
            for (int r = 0; r < numRows; r++) {
                matrix[r][originalColIndex] = cipher.charAt(cipherIndex++);
            }
        }

        StringBuilder plaintext = new StringBuilder();
        // Read the matrix row by row to get the original message
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                plaintext.append(matrix[r][c]);
            }
        }

        return plaintext.toString().replace("X", ""); // Remove padding
    }

    public static void main(String[] args) {
        String plaintext = "WEAREDISCOVEREDFLEEATONCE";
        String key = "ZEBRA";

        String encrypted = encrypt(plaintext, key);
        System.out.println("Encrypted (Row-Column): " + encrypted);

        String decrypted = decrypt(encrypted, key);
        System.out.println("Decrypted (Row-Column): " + decrypted);
    }
}