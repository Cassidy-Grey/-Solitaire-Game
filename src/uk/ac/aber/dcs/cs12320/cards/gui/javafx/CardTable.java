/**
 * Displays the card images on a table (the Javafx stage)
 * @author Faisal Rezwan, Chris Loftus, Lynda Thomas and Cassidy
 * @version 4.0
 */
package uk.ac.aber.dcs.cs12320.cards.gui.javafx;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import javafx.scene.input.*;

public class CardTable {
    private final Stage stage;
    private String[] cards;
    private boolean done;
    private FlowPane box;

    private volatile boolean moved = false;
    private String card1;
    private String card2;

    public CardTable(Stage stage) {
        this.stage = stage;
        stage.setTitle("The Cards");
    }

    /**
     * Called when the user quits the game. It results
     * in the face-down pack of cards not being displayed.
     */
    public void allDone() { done = true;}

    /**
     * Displays all the face-up cards (just the top showing cards)
     * and if the game is not over then also displays the face-down deck.
     * @param c the list of face-up cards
     */
    public void cardDisplay(ArrayList<String> c) {

        // We need to do this within the GUI thread. We assume
        // that the method is called by a non-GUI thread
        Platform.runLater(() -> {
            cards = new String[c.size()];
            cards = c.toArray(cards);

            ScrollPane sp = new ScrollPane();
            Scene scene = new Scene(sp, 725, 290);
            scene.setFill(Color.BLACK);
            box = new FlowPane();
            box.setPrefWrapLength(700);
            Image image;

            if (cards != null) {
                for (String card : cards) {
                    String file = "cards/" + card;
                    image = new Image(getClass().getClassLoader().getResource(file).toString(), true);

                    char num = card.charAt(0);
                    char suit = card.charAt(1);
                    card = "" + num + suit;
                    drawCards(box, image, card);
                }
            }

            if (!done) {
                // Draws the face-down top card of our pack of cards
                String file = "cards/b.gif";
                image = new Image(getClass().getClassLoader().getResource(file).toString(), true);
                drawCards(box, image, "b");

                //Draws the auto-move card
                file = "cards/a.gif";
                image = new Image(getClass().getClassLoader().getResource(file).toString(), true);
                drawCards(box, image, "a");

                //Draws the leaderboard card
                file = "cards/l.gif";
                image = new Image(getClass().getClassLoader().getResource(file).toString(), true);
                drawCards(box, image, "l");

                //Draws the quit card
                file = "cards/q.gif";
                image = new Image(getClass().getClassLoader().getResource(file).toString(), true);
                drawCards(box, image, "q");
            }

            sp.setContent(box);
            sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            stage.setScene(scene);
            stage.show();
        });
    }

    private void drawCards(FlowPane box, Image image, String card) {
        ImageView iv;
        iv = new ImageView();
        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        iv.setImage(image);
        iv.setFitWidth(100);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setId(card);

        dragging(iv);
        target(iv);

        iv.setOnMousePressed(me -> {
            if (iv.getId() == "b" || iv.getId() == "q" || iv.getId() == "l" || iv.getId() == "a") {
                card1 = iv.getId();
                moved = true;
            }
        });
        box.getChildren().add(iv);
    }

    /**
     * This is the hintHighlight Function
     * It highlights the cards which can currently be moved!
     */
    public void hintHighlight(String[] moves) {
        ObservableList<Node> list = box.getChildren();

        for (Node j: list) {
            j.setEffect(new GaussianBlur());
        }

        for (String i: moves){
            for (Node j: list) {
                ImageView card = (ImageView) j;
                if (card.getId().equals(i)){
                    j.setEffect(null);
                }
            }
        }
    }

    public boolean getMoved(){
        return this.moved;
    }

    public void setMoved(boolean move){
        this.moved = move;
    }

    public String getCard1(){
        return this.card1;
    }

    public void setCard1(String c){
        this.card1 = c;
    }

    public String getCard2(){
        return this.card2;
    }



    //Clicking and Dragging
    /**
     * This is the dragging Function
     * It holds events related to dragging.
     */
    private void dragging(ImageView iv) {
        iv.setOnDragDetected(event -> {
            Dragboard db = iv.startDragAndDrop(TransferMode.ANY);

            ClipboardContent content = new ClipboardContent();
            content.putString(" ");
            db.setContent(content);

            event.consume();
        });

        iv.setOnDragDone(Event::consume);
    }

    private void target(ImageView target){
        target.setOnDragOver(event -> {
            if (event.getGestureSource() != target && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        //highlight when hovering
        target.setOnDragEntered(event -> {
            if (event.getGestureSource() != target && event.getDragboard().hasString()) {
                target.setEffect(new GaussianBlur());
                target.setEffect(new Glow(0.5));
            }
            event.consume();
        });

        target.setOnDragExited(event -> {
            target.setEffect(null);
            if (card1 == "a") {
                target.setEffect(new GaussianBlur());
            }
            event.consume();
        });

        //save selected cards to attributes
        target.setOnDragDropped(event -> {
            ImageView source = (ImageView) event.getGestureSource();

            ImageView dest = (ImageView) event.getGestureTarget();

            card1 = source.getId();
            card2 = dest.getId();
            moved = true; //let Game function know a move is made

            event.consume();
        });
    }
}
