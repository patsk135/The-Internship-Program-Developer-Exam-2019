import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FilenameFilter;

public class Hangman {
    private static final Scanner kb = new Scanner(System.in);
    public static void main(String[] args) {
        try {
            String catagory = chooseCatagory();
            String[] wordWithHint = randomWord(catagory);
            String word = wordWithHint[0];
            String hint = wordWithHint[1];
            System.out.println("\nHint: " + hint + "\n");
            startGuessing(word);
        } catch(Exception e) {
            System.out.println(e);
        }
        
    }

    private static String chooseCatagory() {
        File folder = new File("./");
        String[] listOfFiles = folder.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });

        System.out.println("Select Catagory (Number of the catagory): ");
        for (int i = 0; i < listOfFiles.length; i++) {
            System.out.println((i+1) + ". " + listOfFiles[i].substring(0, listOfFiles[i].lastIndexOf(".")));
        }
        int catagory = kb.nextInt();
        return listOfFiles[catagory - 1];
    }

    private static String[] randomWord(String catagory) throws Exception {
        Scanner fr = new Scanner(new File(catagory));
        List<String> words = new ArrayList<String>();
        while (fr.hasNextLine()) {
            words.add(fr.nextLine());
        }
        int random = (int) (Math.random() * words.size());
        String[] wordWithHint = words.get(random).split("::Hint::");
        fr.close();
        return wordWithHint;
    }

    private static void startGuessing(String word) {
        char[] playerWord = initPlayerWord(word);
        int score = 0;
        int remainingWrongGuess = 10;
        String wrongGuess = "";

        while(true) {
            if(isPlayerWin(playerWord)) {
                System.out.println("Answer: " + word + "\nYou Win!");
                break;
            } else if(remainingWrongGuess == -1) {
                System.out.println("Answer: " + word + "\nYou Lose!");
                break;
            }
            printCurrentGuessedWord(playerWord, score, remainingWrongGuess, wrongGuess);
            System.out.println("Guess a Character: ");
            char guessedChar = Character.toLowerCase(kb.next().charAt(0));
            int indexOfGuessedChar = word.toLowerCase().indexOf(guessedChar);
            if(wrongGuess.indexOf(guessedChar) != -1) {
                System.out.println("Repeated Wrong Guessed.");
            } else if(indexOfGuessedChar == -1) {
                remainingWrongGuess--;
                wrongGuess += guessedChar + " ";
            } else if(playerWord[indexOfGuessedChar] != '_') {
                System.out.println("Repeated Guessed.");
            } else {
                score += 10;
                for(int i = 0; i < word.length(); i++){
                    if(word.toLowerCase().charAt(i) == guessedChar) {
                        playerWord[i] = word.charAt(i);
                    }
                }
            }
        }
    }

    private static char[] initPlayerWord(String word) {
        char[] playerWord = word.toCharArray();
        for(int i = 0; i < playerWord.length; i++) {
            if(Character.isLetter(playerWord[i])) {
                playerWord[i] = '_';
            }
        }
        return playerWord;
    }

    private static boolean isPlayerWin(char[] currentGuessed) {
        boolean containUnknownChar = false;
        for(char c : currentGuessed) {
            if(c == '_') {
                containUnknownChar = true;
            }
        }
        return !containUnknownChar;
    }

    private static void printCurrentGuessedWord(char[] playerWord, int score, int remainingWrongGuess, String wrongGuess) {
        for(int i = 0; i < playerWord.length; i++) {
            System.out.print(playerWord[i] + " ");
        }
        System.out.println("  score " + score + ", remaining wrong guess " + remainingWrongGuess + ", wrong guessed: " + wrongGuess);
    }
}