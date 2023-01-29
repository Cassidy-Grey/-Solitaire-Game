/**
 * The Game of patience main class
 * @author Faisal Rezwan, Chris Loftus, Lynda Thomas and Cassidy
 * @version 5.0
 */

import java.util.ArrayList;
import java.io.*;
import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.aber.dcs.cs12320.cards.gui.javafx.CardTable;
import uk.ac.aber.dcs.cs12320.cards.gui.javafx.Name;

import java.util.Collections;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Game extends Application {
    static private final String filename = "scores.txt";

    private CardTable cardTable;
    private Deck deck;
    private ArrayList<Pile> piles;
    private ArrayList<Score> scores;
    private Name n;


    @Override
    public void start(Stage stage) throws IOException {
        cardTable = new CardTable(stage);

        deck = new Deck();
        deck.shuffle();
        piles = new ArrayList<>();
        scores = new ArrayList<>();

        load();

        // The interaction with this game is from a command line
        // menu. We need to create a separate non-GUI thread
        // to run this in. DO NOT REMOVE THIS.
        Runnable commandLineTask = () -> {
            try {
                showMenu();
                n = new Name(stage);
                addScore(n);
                System.out.println("To fully quit, close the graphical window!");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };
        Thread commandLineThread = new Thread(commandLineTask);
        // This is how we start the thread.
        // This causes the run method to execute.
        commandLineThread.start();
    }

    // //////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }

    public void play() {
        Application.launch();
    }

    /**
     * This is the showMenu Function
     * It displays the menu and handles the responses given.
     */
    private void showMenu() throws IOException, InterruptedException {
        do {
            showGUI();
            while(!cardTable.getMoved()) {
                sleep(500);
            }
            checkGUI();
        } while (!(cardTable.getCard1() == "q"));

    }

    /**
     * This is the deal Function
     * It takes the first card in the deck and adds it to a new Pile!
     */
    private void deal(){
        if (deck.getPackLength() > 0) {
            String card = deck.take();
            Pile pile = new Pile(piles.size(), card);
            piles.add(pile);
        } else {
            System.out.println("The deck is empty!");
        }
    }

    /**
     * This is the move Function
     * It combines two piles
     *
     * @param j - Piles to jump across, only 1 and 3 are valid
     * @param start - the Pile which goes on top
     */
    private void move(int j, int start){
        //Get cards from Piles
        if ( start >= 0 && start <= piles.size() ) {
            Pile pile = piles.get(start);
            String top = pile.getCard(0);

            int ind = start - j;
            if ( ind >= 0 && ind <= piles.size() ) {
                pile = piles.get(ind);
                String bottom = pile.getCard(0);

                //Check whether move is valid and make move if true
                if (checkMove(top, bottom) && ((j == 1) || (j == 3) || (j==-1) || (j==-3))) {
                    ind = pile.getPosition();

                    pile = piles.get(start);

                    pile.setPosition(ind);
                    piles.remove(ind);

                    arrangePiles();
                    return;
                }
            }
        }
        System.out.println("This move is not possible!");
    }

    private void move(String card1, String card2){
        int top = -1;
        int bottom = -1;

        for (Pile pile: piles){
            if (pile.getCard(0).equals(card1)){
                top = piles.indexOf(pile);
            } else if (pile.getCard(0).equals(card2)) {
                bottom = piles.indexOf(pile);
            }
        }

        if (top != -1 && bottom != -1){
            move(top - bottom, top);
        } else {
            System.err.println("Invalid GUI Move!");
        }
    }

    /**
     * This is the arrangePiles Function
     * It makes sure all Pile positions are correct!
     */
    private void arrangePiles(){
        Collections.sort(piles);

        int pos = 0;
        for (Pile i : piles) {
            if (i.getPosition() != pos) {
                i.setPosition(pos);
            }
            pos++;
        }
    }

    /**
     * This is the checkMove Function
     * It tests whether a move is valid!
     *
     * @param top - One card in the move
     * @param bottom - The other card in the move
     */
    private boolean checkMove(String top, String bottom){
        return (deck.head(top) == deck.head(bottom)) || (deck.lst(top) == deck.lst(bottom));
    }

    /**
     * This is the auto Function
     * It automatically performs a move for the user!
     * Three-pile jumps get priority. If multiple moves are possible, the user may pick which one to perform.
     */
    private void auto(){
        if (piles.size() != 0){
        int max = piles.size() * 2;
        String[] moves = new String[max]; //holds cards

        //initialise array
        for (int i = 0; i < max; i++){
            moves[i] = "";
        }

        //find possible moves
        addMoves(moves, 3);
        if (checkLength(moves) == 0) { addMoves(moves, 1); } //if there are no 3s, add 1s

        //make move
        if (checkLength(moves) > 2){
            cardTable.hintHighlight(moves); //If multiple jumps of 3 or 1 exist, allow player to pick
        } else if (checkLength(moves) > 0){
            move(moves[0], moves[1]);
            cardTable.setCard1("");
        } } else {
            System.out.println("No Moves Possible!");
        }
    }

    /**
     * This is the addMoves Function
     * It adds all valid moves for the given Piles to an array!
     *
     * @param moves - The array to which the moves will be added
     * @param jump - The amount of Piles to jump when finding the bottom Pile
     */
    private void addMoves(String[] moves, int jump){
        int i = 0;
        for (Pile pile : piles) {
            int pos = pile.getPosition();
            if (pos >= jump) {
                Pile bottom = piles.get(pos - jump);

                if (checkMove(pile.getCard(0), bottom.getCard(0))) {
                    moves[i] = pile.getCard(0);
                    i++;
                    moves[i] = bottom.getCard(0);
                    i++;
                }
            }
        }
    }

    /**
     * This is the checkLength Function
     * It gives the amount of possible moves currently within the array!
     *
     * @param array - The array of moves
     */
    private int checkLength(String[] array){
        int i = 0;

        while (!array[i].equals("")) { i++; }
        return i;
    }

    /**
     * This is the printTopScores Function
     * It displays the top 10 scores!
     */
    private void printTopScores(){
        Collections.sort(scores);
        Collections.reverse(scores);

        for (int i = 0; i < 10; i++){
            Score score = scores.get(i);
            String name = score.getName();
            Integer points = score.getPoints();

            System.out.println((i + 1) + " - " + name + " with " + points + " points.");
        }
    }

    /**
     * This is the addScore Function
     * It adds a valid score to the list.
     */
    private void addScore(Name n) throws IOException, InterruptedException {
        if (deck.getPackLength() == 0 && piles.size() != 0) {
            //activate
            n.nameField();
            System.out.println(n.getName());
            //wait for input
            do {
                sleep(500);
            } while(n.getName().equals(""));

            //save score
            String response = n.getName();

            Integer points = Deck.getMAXDECK() - piles.size();
            Score score = new Score(response, points);
            scores.add(score);

            save();
        }
    }

    /**
     * This is the save Function
     * It saves the list of Scores to file.
     */
    private void save() throws IOException {
        try (FileWriter fw = new FileWriter(filename);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter outfile = new PrintWriter(bw)) {

            for (Score score : scores) {
                outfile.println();

                outfile.println(score.getName());
                outfile.print(score.getPoints());
            }
        }
    }

    /**
     * This is the load Function
     * It loads the Scores from file.
     */
    private void load() throws IOException {
        scores.clear();

        try (FileReader fr = new FileReader(filename);
             BufferedReader br = new BufferedReader(fr);
             Scanner infile = new Scanner(br)) {

            infile.useDelimiter("\r?\n|\r");
            while (infile.hasNext()) {
                String name = infile.next();
                Integer points = infile.nextInt();

                Score score = new Score(name, points);

                scores.add(score);
            }
        }
    }

    /**
     * This is the showGUI Function
     * It displays the current Piles in the GUI window
     */
    private void showGUI(){
        if (cardTable.getCard1() != "a") {
            ArrayList<String> cardStrings = new ArrayList<>();
            for (Pile pile : piles) {
                cardStrings.add(pile.getCard(0) + ".gif");
            }

            cardTable.cardDisplay(cardStrings);
        }
    }

    /**
     * This is the checkGUI Function
     * It reads the cards from the cardTable and acts accordingly.
     */
    private void checkGUI() throws IOException, InterruptedException {
        String card1 = cardTable.getCard1();
        String card2 = cardTable.getCard2();

        switch (card1){
            case "b":
                deal();
                break;
            case "q":
                cardTable.allDone();
                showGUI();
                cardTable.setMoved(false);
                return; //return without re-starting the menu
            case "l":
                printTopScores();
                break;
            case "a":
                auto();
                break;
            default:
                move(card1, card2);
        }

        cardTable.setMoved(false);
        showMenu();
    }
}
