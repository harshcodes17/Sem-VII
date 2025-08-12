package CNS.Assignment02;

import java.util.Arrays;

public class RailFenceCipher {
    // Encrypts a message using the Rail Fence cipher
    public static String encrypt(String text, int key) {
        // Create an array of StringBuilder objects to represent the rails
        StringBuilder[] rails = new StringBuilder[key];
        for (int i = 0; i < key; i++) {
            rails[i] = new StringBuilder();
        }

        int row = 0;
        int direction = 1; // 1 for down, -1 for up

        // Traverse the plaintext in a zigzag pattern
        for (char c : text.toCharArray()) {
            rails[row].append(c);
            row += direction;

            // Reverse direction at the top and bottom rails
            if (row == 0 || row == key - 1) {
                direction *= -1;
            }
        }

        // Concatenate the rails to form the ciphertext
        StringBuilder result = new StringBuilder();
        for (StringBuilder rail : rails) {
            result.append(rail);
        }

        return result.toString();
    }

    // Decrypts a message using the Rail Fence cipher
    public static String decrypt(String cipher, int key) {
        // First, calculate the length of each rail to reconstruct the matrix
        int[] railLengths = new int[key];
        int row = 0;
        int direction = 1;
        for (int i = 0; i < cipher.length(); i++) {
            railLengths[row]++;
            row += direction;
            if (row == 0 || row == key - 1) {
                direction *= -1;
            }
        }

        // Distribute the ciphertext characters into the rails based on the lengths
        String[] rails = new String[key];
        int charIndex = 0;
        for (int i = 0; i < key; i++) {
            rails[i] = cipher.substring(charIndex, charIndex + railLengths[i]);
            charIndex += railLengths[i];
        }

        // Reconstruct the plaintext by reading in a zigzag pattern
        StringBuilder result = new StringBuilder();
        int[] railIndices = new int[key]; // To keep track of the current character index in each rail
        row = 0;
        direction = 1;

        for (int i = 0; i < cipher.length(); i++) {
            result.append(rails[row].charAt(railIndices[row]++));
            row += direction;
            if (row == 0 || row == key - 1) {
                direction *= -1;
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String plaintext = "WEAREDISCOVEREDFLEEATONCE";
        int key = 3;

        String encrypted = encrypt(plaintext, key);
        System.out.println("Encrypted (Rail Fence): " + encrypted);

        String decrypted = decrypt(encrypted, key);
        System.out.println("Decrypted (Rail Fence): " + decrypted);
    }
}