import java.util.*;

public class CaesarCipher {

    public static String encrypt(String plaintext, int shift) {
        StringBuilder result = new StringBuilder();
        plaintext = plaintext.toUpperCase();
        shift = shift % 26;
        for (char c : plaintext.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                char encryptedChar = (char) (((c - 'A' + shift) % 26) + 'A');
                result.append(encryptedChar);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    public static String decrypt(String ciphertext, int shift) {
        StringBuilder result = new StringBuilder();
        ciphertext = ciphertext.toUpperCase();
        shift = shift % 26;
        for (char c : ciphertext.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                char decryptedChar = (char) (((c - 'A' - shift + 26) % 26) + 'A');
                result.append(decryptedChar);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    public static void main(String[] args) {
        String text = "HELLO WORLD";
        int shift = 3;
        String encrypted = encrypt(text, shift);
        System.out.println("Plaintext: " + text);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypt(encrypted, shift));
    }
}
