import java.util.*;
import java.util.stream.IntStream;
import java.io.File;
import java.io.FilenameFilter;

public class Hangman {
    public static void main(String[] args) throws Exception {
        Scanner kb = new Scanner(System.in);
        File folder = new File("./");
        String[] listOfFiles = folder.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });

        System.out.println("Select Catagory: ");
        for (String file : listOfFiles) {
            System.out.println(file.substring(0, file.lastIndexOf(".")));
        }
        int catagory = kb.nextInt();

        System.out.println(listOfFiles[catagory - 1]);
        Scanner fileReader = new Scanner(new File(listOfFiles[catagory - 1]));
        List<String> words = new ArrayList<String>();
        while (fileReader.hasNextLine()) {
            words.add(fileReader.nextLine());
        }
        int ran = (int) (Math.random() * 6 - 1);
        String[] word = words.get(ran).split("::Hint::");
        int[] check = new int[word[0].length()];
        int score = 0;
        int remain = 10;
        String wrong = "";
        if (word.length > 1) {
            System.out.println("\nHint: " + word[1] + "\n");
        }
        while (IntStream.of(check).anyMatch(x -> x == 0)) {
            for (int i = 0; i < word[0].length(); i++) {
                if (check[i] == 1 || !(Character.toLowerCase(word[0].charAt(i)) >= 'a'
                        && Character.toLowerCase(word[0].charAt(i)) <= 'z')) {
                    check[i] = 1;
                    System.out.print(word[0].charAt(i) + " ");
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.print("Score " + score + ", remaining wrong guess " + remain);
            if (!wrong.equals("")) {
                System.out.print(", wrong guessed: " + wrong);
            }
            System.out.println();
            char guessWord = Character.toLowerCase(kb.next().charAt(0));
            if(wrong.indexOf(guessWord) != -1){
                System.out.println("Repeated Answer!");
                continue;
            }
            if (word[0].toLowerCase().indexOf(guessWord) == -1) {
                wrong += guessWord + " ";
                remain--;
                if(remain == -1) {
                    break;
                }
            } else {
                for (int i = 0; i < word[0].length(); i++) {
                    if (word[0].toLowerCase().charAt(i) == guessWord && check[i] == 0) {
                        check[i] = 1;
                        score += 10;
                    }
                }
            }
        }
        if(remain == -1){
            System.out.println("Answer: " + word[0] + "You Lose!");
        }
        System.out.println("Answer: " + word[0] + "\nYou Win!");

        kb.close();
        fileReader.close();
    }
}