import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
// Charles Williams
// Encrypts, decrypts, and breaks Caesar ciphers
// KNOWN ISSUES - more likely to have incorrect break solution when phrase is too short,
// has several double letter pairs, or contains no 'e'

/**
 *
 */
public class Caesar {

  private static final HashMap<Character, Double> frequencies = new HashMap<Character, Double>();

  // standard English alphabet from A to Z
  private static final char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
      'l',
      'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

  // initializes the frequencies of each letter in standard English alphabet into hashmap
  private static void initFrequencies() {
    frequencies.put('a', 8.167);
    frequencies.put('b', 1.492);
    frequencies.put('c', 2.202);
    frequencies.put('d', 4.253);
    frequencies.put('e', 12.702);
    frequencies.put('f', 2.228);
    frequencies.put('g', 2.015);
    frequencies.put('h', 6.094);
    frequencies.put('i', 6.966);
    frequencies.put('j', 0.153);
    frequencies.put('k', 1.292);
    frequencies.put('l', 4.025);
    frequencies.put('m', 2.406);
    frequencies.put('n', 6.749);
    frequencies.put('o', 7.507);
    frequencies.put('p', 1.929);
    frequencies.put('q', 0.095);
    frequencies.put('r', 5.987);
    frequencies.put('s', 6.327);
    frequencies.put('t', 9.356);
    frequencies.put('u', 2.758);
    frequencies.put('v', 0.978);
    frequencies.put('w', 2.560);
    frequencies.put('x', 0.150);
    frequencies.put('y', 1.994);
    frequencies.put('z', 0.077);
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    initFrequencies();
    Scanner input = new Scanner(System.in);
    System.out.println(
        "Please enter a key:\n" + "\"e\" to encrypt a message,\n" + "\"d\" to decrypt a message,\n"
            + "\"b\" to break an encrypted message,\n" + "or any other key to quit:");
    String selection = input.nextLine();
    switch (selection) {
      case "e":
      case "E":
        System.out.println("Encryption Selected");
        System.out.println(
            "Please enter a message to encrypt:");
        String ePlainText = input.nextLine();
        System.out.println("Please enter an integer between 0 and 25 inclusive for the shift:");
        int eKey = input.nextInt();
        System.out.println("Your encrypted message is: " + encrypt(ePlainText, eKey));
        break;
      case "d":
      case "D":
        System.out.println("Decryption Selected");
        System.out.println(
            "Please enter an encrypted message:");
        String dCipherText = input.nextLine();
        System.out.println("Please enter the key of this message (0 - 25 inclusive):");
        int dKey = input.nextInt();
        System.out.println("Your original message is: " + decrypt(dCipherText, dKey));
        break;
      case "b":
      case "B":
        System.out.println("Break Encryption Selected");
        System.out.println(
            "Please enter an encrypted message to break:");
        String bCipherText = input.nextLine();
        System.out.println("The possible solutions are:");
        printSolutions(getSolutions(bCipherText));
        System.out.println("The most likely solution is:");
        System.out.println(mostLikely(getSolutions(bCipherText)) + "\n");
        System.out.println("Was this correct? - Enter \"y\" or \"n\":");
        String confirmed = input.next();
        if (confirmed.equals("y") || confirmed.equals("Y")) {
          System.out.println("Awesome! Thank you for your feedback.");
        } else {
          System.out.println("That's too bad, I'll work to improve!");
        }
        System.exit(0);
      default:
        System.out.println("Exiting...Thank you");
        System.exit(0);
    }
  }

  /**
   *
   * @param ePlainText
   * @param eKey
   * @return
   */
  private static String encrypt(String ePlainText, int eKey) {
    char[] oldMessage = ePlainText.toCharArray();
    char[] newMessage = new char[oldMessage.length];
    for (int j = 0; j < oldMessage.length; j++) {
      boolean lowerCase = Character.isLowerCase(oldMessage[j]);
      int currentLetterIndex = new String(alphabet).indexOf(Character.toLowerCase(oldMessage[j]));
      if (oldMessage[j] == ' ') {
        newMessage[j] = ' ';
      } else if (currentLetterIndex != -1) {
        if (lowerCase) {
          newMessage[j] = alphabet[Math.floorMod((currentLetterIndex + eKey), 26)];
        } else {
          newMessage[j] = Character
              .toUpperCase(alphabet[Math.floorMod((currentLetterIndex + eKey), 26)]);
        }
      } else {
        newMessage[j] = oldMessage[j];
      }
    }
    return new String(newMessage);
  }

  /**
   *
   * @param dCipherText
   * @param dKey
   * @return
   */
  private static String decrypt(String dCipherText, int dKey) {
    char[] newMessage = dCipherText.toCharArray();
    char[] oldMessage = new char[newMessage.length];
    for (int k = 0; k < newMessage.length; k++) {
      boolean lowerCase = Character.isLowerCase(newMessage[k]);
      int currentLetterIndex = new String(alphabet).indexOf(Character.toLowerCase(newMessage[k]));
      if (newMessage[k] == ' ') {
        oldMessage[k] = ' ';
      } else if (currentLetterIndex != -1) {
        if (lowerCase) {
          oldMessage[k] = alphabet[Math.floorMod((currentLetterIndex - dKey), 26)];
        } else {
          oldMessage[k] = Character
              .toUpperCase(alphabet[Math.floorMod((currentLetterIndex - dKey), 26)]);
        }
      } else {
        oldMessage[k] = newMessage[k];
      }
    }
    return new String(oldMessage);
  }

  /**
   *
   * @param cipherText
   * @return
   */
  private static String[] getSolutions(String cipherText) {
    String[] solutions = new String[26];
    for (int i = 0; i < solutions.length; i++) {
      solutions[i] = decrypt(cipherText, i);
    }
    return solutions;
  }

  /**
   *
   * @param solutions
   * @return
   */
  private static String mostLikely(String[] solutions) {
    Integer[] probabilities = new Integer[solutions.length];
    for (int i = 0; i < probabilities.length; i++) {
      probabilities[i] = getProbability(solutions[i]);
    }
    int likelyIndex = Arrays.asList(probabilities).indexOf(maxValue(probabilities));
    return solutions[likelyIndex];
  }

  /**
   *
   * @param string
   * @return
   */
  private static int getProbability(String string) {
    char[] letters = string.toCharArray();
    int probability = 0;
    for (char letter : letters) {
      int currentLetterIndex = new String(alphabet).indexOf(Character.toLowerCase(letter));
      if (currentLetterIndex == -1) {
        probability += 0;
      } else {
        probability += frequencies.get(Character.toLowerCase(letter));
      }
    }
    return probability;
  }

  /**
   *
   * @param frequencies
   * @return
   */
  private static int maxValue(Integer[] frequencies) {
    int max = frequencies[0];
    for (int value : frequencies) {
      if (max < value) {
        max = value;
      }
    }
    return max;
  }

  /**
   *
   * @param solutions
   */
  private static void printSolutions(String[] solutions) {
    for (String solution : solutions) {
      System.out.println(solution);
    }
  }
}
