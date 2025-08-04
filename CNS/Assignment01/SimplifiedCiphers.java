import java.util.*;
// Note: For Hill Cipher, you would still need a way to perform matrix inverse modulo 26.
// The provided full code has this logic. For true simplification without external libraries,
// Hill Cipher becomes significantly more complex to write from scratch for modular inverse.

public class SimplifiedCiphers {

    // --- 1. Caesar Cipher ---
    // Theory: C = (P + k) mod 26
    // P = (C - k) mod 26
    public static String caesarEncrypt(String plaintext, int shift) {
        StringBuilder result = new StringBuilder();
        plaintext = plaintext.toUpperCase(); // Convert to uppercase for simplicity
        shift = shift % 26;

        for (char c : plaintext.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                char encryptedChar = (char) (((c - 'A' + shift) % 26) + 'A');
                result.append(encryptedChar);
            } else {
                result.append(c); // Keep non-alphabetic chars as is
            }
        }
        return result.toString();
    }

    public static String caesarDecrypt(String ciphertext, int shift) {
        StringBuilder result = new StringBuilder();
        ciphertext = ciphertext.toUpperCase(); // Convert to uppercase for simplicity
        shift = shift % 26;

        for (char c : ciphertext.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                // (c - 'A' - shift) could be negative, so add 26 before modulo
                char decryptedChar = (char) (((c - 'A' - shift + 26) % 26) + 'A');
                result.append(decryptedChar);
            } else {
                result.append(c); // Keep non-alphabetic chars as is
            }
        }
        return result.toString();
    }

    // --- 2. Playfair Cipher ---
    // Requires a 5x5 matrix generation and helper for finding positions.
    // The matrix generation and position finding are somewhat complex to simplify further
    // without losing core functionality. I'll provide the combined essentials.

    private static char[][] playfairMatrix; // Static for simplicity, typically passed or returned

    // Helper to generate the 5x5 Playfair matrix
    private static void generatePlayfairMatrix(String key) {
        playfairMatrix = new char[5][5];
        key = key.toUpperCase().replaceAll("[^A-Z]", "").replace('J', 'I');
        LinkedHashSet<Character> charSet = new LinkedHashSet<>();

        for (char c : key.toCharArray()) {
            charSet.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c != 'J') {
                charSet.add(c);
            }
        }

        Iterator<Character> it = charSet.iterator();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (it.hasNext()) {
                    playfairMatrix[i][j] = it.next();
                }
            }
        }
    }

    // Helper to find character position in the matrix
    private static int[] findPlayfairCharPos(char c) {
        for (int r = 0; r < 5; r++) {
            for (int col = 0; col < 5; col++) {
                if (playfairMatrix[r][col] == c) {
                    return new int[]{r, col};
                }
            }
        }
        return null; // Should not happen with valid input
    }

    public static String playfairEncrypt(String plaintext, String key) {
        generatePlayfairMatrix(key); // Generate matrix
        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z]", "").replace('J', 'I');

        StringBuilder preparedText = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            preparedText.append(plaintext.charAt(i));
            if (i + 1 < plaintext.length()) {
                if (plaintext.charAt(i) == plaintext.charAt(i + 1)) {
                    preparedText.append('X');
                }
            }
        }
        if (preparedText.length() % 2 != 0) {
            preparedText.append('X');
        }

        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < preparedText.length(); i += 2) {
            char char1 = preparedText.charAt(i);
            char char2 = preparedText.charAt(i + 1);

            int[] pos1 = findPlayfairCharPos(char1);
            int[] pos2 = findPlayfairCharPos(char2);

            if (pos1[0] == pos2[0]) { // Same row
                ciphertext.append(playfairMatrix[pos1[0]][(pos1[1] + 1) % 5]);
                ciphertext.append(playfairMatrix[pos2[0]][(pos2[1] + 1) % 5]);
            } else if (pos1[1] == pos2[1]) { // Same column
                ciphertext.append(playfairMatrix[(pos1[0] + 1) % 5][pos1[1]]);
                ciphertext.append(playfairMatrix[(pos2[0] + 1) % 5][pos2[1]]);
            } else { // Rectangle
                ciphertext.append(playfairMatrix[pos1[0]][pos2[1]]);
                ciphertext.append(playfairMatrix[pos2[0]][pos1[1]]);
            }
        }
        return ciphertext.toString();
    }

    public static String playfairDecrypt(String ciphertext, String key) {
        generatePlayfairMatrix(key); // Generate matrix
        ciphertext = ciphertext.toUpperCase().replaceAll("[^A-Z]", "").replace('J', 'I'); // Should be done for key, but not cipher text

        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 2) {
            char char1 = ciphertext.charAt(i);
            char char2 = ciphertext.charAt(i + 1);

            int[] pos1 = findPlayfairCharPos(char1);
            int[] pos2 = findPlayfairCharPos(char2);

            if (pos1[0] == pos2[0]) { // Same row
                plaintext.append(playfairMatrix[pos1[0]][(pos1[1] + 4) % 5]); // +4 is equivalent to -1 mod 5
                plaintext.append(playfairMatrix[pos2[0]][(pos2[1] + 4) % 5]);
            } else if (pos1[1] == pos2[1]) { // Same column
                plaintext.append(playfairMatrix[(pos1[0] + 4) % 5][pos1[1]]);
                plaintext.append(playfairMatrix[(pos2[0] + 4) % 5][pos2[1]]);
            } else { // Rectangle
                plaintext.append(playfairMatrix[pos1[0]][pos2[1]]);
                plaintext.append(playfairMatrix[pos2[0]][pos1[1]]);
            }
        }
        return plaintext.toString();
    }

    // --- 3. Hill Cipher (Simplified for 2x2 matrix only) ---
    // Requires a matrix library or custom matrix operations for inverse.
    // The modular inverse calculation is inherent complexity.
    // I'll keep the direct calculations as in the original code, as simplifying them
    // would mean removing the core math of Hill Cipher.
    // (P = K^-1 * C mod 26) requires matrix inverse modulo 26.

    // Helper for modulo 26, ensuring positive results
    private static int mod26(int x) {
        int result = x % 26;
        return result < 0 ? result + 26 : result;
    }

    // Helper for modular inverse
    private static int modInverse(int a, int m) {
        a = mod26(a); // Ensure 'a' is positive and within 0-25
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) return x;
        }
        return -1; // Inverse does not exist
    }

    public static String hillEncrypt(String plaintext, int[][] keyMatrix) {
        if (keyMatrix.length != 2 || keyMatrix[0].length != 2) {
            throw new IllegalArgumentException("Key matrix must be 2x2 for this simplified example.");
        }

        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z]", "");
        if (plaintext.length() % 2 != 0) plaintext += 'X'; // Pad if odd length

        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i += 2) {
            int p1 = plaintext.charAt(i) - 'A';
            int p2 = plaintext.charAt(i + 1) - 'A';

            int c1 = mod26(keyMatrix[0][0] * p1 + keyMatrix[0][1] * p2);
            int c2 = mod26(keyMatrix[1][0] * p1 + keyMatrix[1][1] * p2);

            ciphertext.append((char)(c1 + 'A'));
            ciphertext.append((char)(c2 + 'A'));
        }
        return ciphertext.toString();
    }

    public static String hillDecrypt(String ciphertext, int[][] keyMatrix) {
        if (keyMatrix.length != 2 || keyMatrix[0].length != 2) {
            throw new IllegalArgumentException("Key matrix must be 2x2 for this simplified example.");
        }

        // Calculate determinant and its modular inverse
        int det = mod26(keyMatrix[0][0] * keyMatrix[1][1] - keyMatrix[0][1] * keyMatrix[1][0]);
        int detInv = modInverse(det, 26);
        if (detInv == -1) {
            throw new IllegalArgumentException("Key matrix is not invertible modulo 26. Cannot decrypt.");
        }

        // Calculate inverse key matrix modulo 26
        int[][] invKeyMatrix = new int[2][2];
        invKeyMatrix[0][0] = mod26(detInv * keyMatrix[1][1]);
        invKeyMatrix[0][1] = mod26(-detInv * keyMatrix[0][1]);
        invKeyMatrix[1][0] = mod26(-detInv * keyMatrix[1][0]);
        invKeyMatrix[1][1] = mod26(detInv * keyMatrix[0][0]);

        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 2) {
            int c1 = ciphertext.charAt(i) - 'A';
            int c2 = ciphertext.charAt(i + 1) - 'A';

            int p1 = mod26(invKeyMatrix[0][0] * c1 + invKeyMatrix[0][1] * c2);
            int p2 = mod26(invKeyMatrix[1][0] * c1 + invKeyMatrix[1][1] * c2);

            plaintext.append((char)(p1 + 'A'));
            plaintext.append((char)(p2 + 'A'));
        }
        return plaintext.toString();
    }

    // --- 4. Vigenere Cipher ---
    // Theory: Ci = (Pi + Ki) mod 26
    // Pi = (Ci - Ki) mod 26
    public static String vigenereEncrypt(String plaintext, String key) {
        StringBuilder result = new StringBuilder();
        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z]", ""); // Only alphabet
        key = key.toUpperCase().replaceAll("[^A-Z]", ""); // Only alphabet

        int keyIndex = 0;
        for (char pChar : plaintext.toCharArray()) {
            if (pChar >= 'A' && pChar <= 'Z') {
                int pVal = pChar - 'A';
                int kVal = key.charAt(keyIndex % key.length()) - 'A';
                char encryptedChar = (char) (((pVal + kVal) % 26) + 'A');
                result.append(encryptedChar);
                keyIndex++;
            } else {
                result.append(pChar); // Keep non-alphabetic chars as is
            }
        }
        return result.toString();
    }

    public static String vigenereDecrypt(String ciphertext, String key) {
        StringBuilder result = new StringBuilder();
        ciphertext = ciphertext.toUpperCase().replaceAll("[^A-Z]", ""); // Only alphabet
        key = key.toUpperCase().replaceAll("[^A-Z]", ""); // Only alphabet

        int keyIndex = 0;
        for (char cChar : ciphertext.toCharArray()) {
            if (cChar >= 'A' && cChar <= 'Z') {
                int cVal = cChar - 'A';
                int kVal = key.charAt(keyIndex % key.length()) - 'A';
                // (cVal - kVal) could be negative, so add 26 before modulo
                char decryptedChar = (char) (((cVal - kVal + 26) % 26) + 'A');
                result.append(decryptedChar);
                keyIndex++;
            } else {
                result.append(cChar); // Keep non-alphabetic chars as is
            }
        }
        return result.toString();
    }

    // --- Example Usage (main method for testing) ---
    public static void main(String[] args) {
        System.out.println("----- Caesar Cipher -----");
        String caesarText = "HELLO WORLD";
        int caesarShift = 3;
        String caesarEncrypted = caesarEncrypt(caesarText, caesarShift);
        System.out.println("Plaintext: " + caesarText);
        System.out.println("Key (shift): " + caesarShift);
        System.out.println("Encrypted: " + caesarEncrypted);
        System.out.println("Decrypted: " + caesarDecrypt(caesarEncrypted, caesarShift));
        System.out.println();

        System.out.println("----- Playfair Cipher -----");
        String playfairText = "HELLO"; // Original example
        String playfairKey = "MONARCHY";
        String playfairEncrypted = playfairEncrypt(playfairText, playfairKey);
        System.out.println("Plaintext: " + playfairText);
        System.out.println("Key: " + playfairKey);
        System.out.println("Encrypted: " + playfairEncrypted);
        // Note: Playfair decryption might not perfectly reverse 'X' padding added during encryption if original had doubles
        System.out.println("Decrypted: " + playfairDecrypt(playfairEncrypted, playfairKey));
        System.out.println();

        System.out.println("----- Hill Cipher (2x2) -----");
        // For Hill Cipher, plaintext length MUST be a multiple of 2 (or padded)
        String hillText = "HELP";
        int[][] hillKey = { {3, 3}, {2, 5} }; // Key from assignment
        System.out.println("Plaintext: " + hillText);
        System.out.println("Key Matrix: ");
        System.out.println(" [ " + hillKey[0][0] + " " + hillKey[0][1] + " ]");
        System.out.println(" [ " + hillKey[1][0] + " " + hillKey[1][1] + " ]");
        try {
            String hillEncrypted = hillEncrypt(hillText, hillKey);
            System.out.println("Encrypted: " + hillEncrypted);
            String hillDecrypted = hillDecrypt(hillEncrypted, hillKey);
            System.out.println("Decrypted: " + hillDecrypted);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println();

        System.out.println("----- Vigenere Cipher -----");
        String vigenereText = "HELLO WORLD"; // Original example
        String vigenereKey = "KEY";
        String vigenereEncrypted = vigenereEncrypt(vigenereText, vigenereKey);
        System.out.println("Plaintext: " + vigenereText);
        System.out.println("Key: " + vigenereKey);
        System.out.println("Encrypted: " + vigenereEncrypted);
        System.out.println("Decrypted: " + vigenereDecrypt(vigenereEncrypted, vigenereKey));
        System.out.println();
    }
}