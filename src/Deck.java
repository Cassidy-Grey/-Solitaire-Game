/**
 * This is the Deck class!
 * @author Cassidy
 * @version 1.1
 * */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Deck {
    public static final int MAXDECK = 52;
    private static String[] template = new String[MAXDECK];

    public ArrayList<String> pack;


    /**
     * Constructor for a Deck
     */
    public Deck() {
        pack = new ArrayList<>();
    }

    /**
     * This is the loadCards Method
     * It loads a template deck from file.
     */
    private static String[] loadCards() throws IOException {
        String cardfile = "cards.txt";

        try (FileReader fr = new FileReader(cardfile);
             BufferedReader br = new BufferedReader(fr);
             Scanner infile = new Scanner(br)) {


            int i = 0;
            infile.useDelimiter("\r?\n|\r");
            while (infile.hasNext()) {
                String card = infile.next() + infile.next();

                template[i] = card;
                i++;
            }
        }
        return template;
    }

    /**
     * This is the shuffle Method
     * It can be called in order to shuffle the deck.
     */
    public void shuffle() throws IOException {
        template = loadCards();
        shuffler(this.pack);
    }

    /**
     * Shuffler Method
     * Picks random cards from the template deck
     *  @param pack - The pack to be shuffled
     *
     */
    private void shuffler(ArrayList<String> pack) {
        pack.clear();

        //copy template to new array
        String[] sorted = new String[MAXDECK];
        System.arraycopy(Deck.template, 0, sorted, 0, MAXDECK);

        int size = MAXDECK;
        Random rand = new Random();
        for (int i = 0; i < MAXDECK; i++) {
            int x = rand.nextInt(size);
            pack.add(sorted[x]);

            //Move cards after the used card along
            if (size - 1 - x >= 0) System.arraycopy(sorted, x + 1, sorted, x, size - 1 - x);
            size -= 1;
        }
    }

    /**
     * This is the getPackString Method
     * It returns the contents of the pack as a string!
     * */
    public String getPackString() {
        System.out.println("Here is the pack!");
        StringBuilder result = new StringBuilder();
        for (String card : pack) {
            result.append(card);
            result.append(" ");
        }

        return "" + result;
    }

    /**
     * This is the getPackLength Method
     * It returns the amount of cards in the pack!
     * */
    public int getPackLength() {
        return pack.size();
    }

    /**
     * This is the head Method
     * It returns the first character in a String!
     * This method is primarily used to find the number of a card
     * */
    public char head(String str){
        return str.charAt(0);
    }

    /**
     * This is the lst Method
     * It returns the last character in a String!
     * This method is primarily used to find the suit of a card
     * */
    public char lst(String str){
        int x = str.length();
        x -= 1;
        return str.charAt(x);
    }

    /**
     * This is the take Method
     * It removes the first card in the pack and returns it!
     * */
    public String take() {
        if (pack.size() > 0) {
            String card = pack.get(0);
            pack.remove(0);
            return card;
        } else {
            System.err.println("Invalid index given!");
            return "ah";
        }
    }

    /**
     * This is the getCard Method
     * It returns the card at a given valid index in the pack!
     *
     * @param x - The index to find
     * */
    public String getCard(int x) {
        if ((pack.size() >= x) && (x >= 0)) {
            return pack.get(x);
        } else {
            System.err.println("Invalid index given!");
            return "ah";
        }
    }

    /**
     * This is the getMAXDECK Method
     * It returns the maximum size of the deck!
     * */
    public static int getMAXDECK() {
        return MAXDECK;
    }
}