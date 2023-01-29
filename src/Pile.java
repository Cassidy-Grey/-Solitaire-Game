/**
 * This is the Pile class, a subclass of Deck
 * @author Cassidy
 * @version 2.1
 * */
public class Pile extends Deck implements Comparable<Pile>{
    private Integer position;

    /**
     * Constructor for a Pile
     *
     * @param pos - the position of the Pile
     * @param c - The card at the top of the Pile
     */
    public Pile(int pos, String c) {
        this.position = pos;
        this.pack.add(c);
    }

    /**
     * This is the compareTo Method
     * It enables Piles to be sorted by position!
     *
     * @param other - The Score to be compared to
     * */
    public int compareTo(Pile other){
        return position.compareTo(other.position);
    }

    /**
     * This is the getPosition Method
     * It returns the position of the Pile!
     * */
    public int getPosition() {
        return position;
    }

    /**
     * This is the setPosition Method
     * It sets the position of the Pile!
     * */
    public void setPosition(int position) {
        this.position = position;
    }
}
