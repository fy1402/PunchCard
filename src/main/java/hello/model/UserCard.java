package hello.model;


import java.util.List;

/**
 * Created by i-feng on 2018/5/16.
 */

public class UserCard {

    private String name;

    private List<Card> cardList;

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserCard () {};
}
