package at.technikum.handler;

import at.technikum.database.AbstractDBTable;
import at.technikum.handler.repository.CardHandler;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CardHandlerImpl extends AbstractDBTable implements CardHandler {

    private static CardHandlerImpl instance;
    private LoggerStatic loggerStatic;

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    /** Constructor **
     *  tableName --> Name der Tabelle in der Datenbank
     */
    public CardHandlerImpl(){
        this.tableName = "card";
        this.loggerStatic = LoggerStatic.getInstance();
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                            TOOL                               **/
    /*******************************************************************/
    /** --> Filtert aus dem Namen den CardType heraus **/
    @Override
    public CardType filterCardType(CardName cardName){
        String s = ""+cardName;
        if(s.contains("Spell")){
            return CardType.SPELL;
        }
        return CardType.MONSTER;
    }
    /** --> Filtert aus dem Namen das Element heraus **/
    @Override
    public CardElement filterCardElement(CardName cardName){

        String s = ""+cardName;

        if(s.contains("Water")){
            return CardElement.WATER;
        }

        if(s.contains("Fire")){
            return CardElement.FIRE;
        }

        return CardElement.NORMAL;
    }
    /** --> erstellt Default Karten **/
    @Override
    public void DefaultCards(){
        CardHandlerImpl card = new CardHandlerImpl();
        // SPELL
        card.addCardByData("",CardName.Spell, CardType.SPELL, CardElement.NORMAL,10);
        card.addCardByData("",CardName.RegularSpell, CardType.SPELL, CardElement.NORMAL,10);
        card.addCardByData("",CardName.WaterSpell, CardType.SPELL, CardElement.WATER,10);
        card.addCardByData("",CardName.FireSpell, CardType.SPELL, CardElement.FIRE,10);

        // DRAGONS
        card.addCardByData("",CardName.Dragon, CardType.MONSTER, CardElement.NORMAL,10);
        card.addCardByData("",CardName.WaterDragon, CardType.MONSTER, CardElement.WATER,10);
        card.addCardByData("",CardName.FireDragon, CardType.MONSTER, CardElement.FIRE,10);

        //
        card.addCardByData("",CardName.Goblin, CardType.MONSTER, CardElement.NORMAL,10);
        card.addCardByData("",CardName.WaterGoblin, CardType.MONSTER, CardElement.WATER,10);
        card.addCardByData("",CardName.FireGoblin, CardType.MONSTER, CardElement.FIRE,10);

        //
        card.addCardByData("",CardName.Knight, CardType.MONSTER, CardElement.NORMAL,10);
        card.addCardByData("",CardName.WaterKnight, CardType.MONSTER, CardElement.WATER,10);
        card.addCardByData("",CardName.FireKnight, CardType.MONSTER, CardElement.FIRE,10);

        //
        card.addCardByData("",CardName.Kraken, CardType.MONSTER, CardElement.NORMAL,10);
        card.addCardByData("",CardName.WaterKraken, CardType.MONSTER, CardElement.WATER,10);
        card.addCardByData("",CardName.FireKraken, CardType.MONSTER, CardElement.FIRE,10);

        //
        card.addCardByData("",CardName.Elf, CardType.MONSTER, CardElement.NORMAL,10);
        card.addCardByData("",CardName.WaterElf, CardType.MONSTER, CardElement.WATER,10);
        card.addCardByData("",CardName.FireElf, CardType.MONSTER, CardElement.FIRE,10);

        //
        card.addCardByData("",CardName.Wizzard, CardType.MONSTER, CardElement.NORMAL,10);
        card.addCardByData("",CardName.WaterWizzard, CardType.MONSTER, CardElement.WATER,10);
        card.addCardByData("",CardName.FireWizzard, CardType.MONSTER, CardElement.FIRE,10);

        //
        card.addCardByData("",CardName.Ork, CardType.MONSTER, CardElement.NORMAL,10);
        card.addCardByData("",CardName.WaterOrk, CardType.MONSTER, CardElement.WATER,10);
        card.addCardByData("",CardName.FireOrks, CardType.MONSTER, CardElement.FIRE,10);
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                            Builder                            **/
    /*******************************************************************/
    /** builderCard **/
    @Override
    public Card cardBuilder(String cardID, String cardName, CardType cardTyp, String cardElement, String cardPower){

        Card card;

        switch (cardTyp) {
            case MONSTER:
                card = MonsterCardImpl.builder()
                        .cardID(cardID)
                        .cardTyp(CardType.MONSTER)
                        .cardName(CardName.valueOf(cardName))
                        .cardElement(CardElement.valueOf(cardElement))
                        .cardPower(convertToDouble(cardPower))
                        .build();
                return card;
            case SPELL:
                card = SpellCardImpl.builder()
                        .cardID(cardID)
                        .cardTyp(CardType.SPELL)
                        .cardName(CardName.valueOf(cardName))
                        .cardElement(CardElement.valueOf(cardElement))
                        .cardPower(convertToDouble(cardPower))
                        .build();
                return card;
        }

        return null;
    }
    /** Liefert eine Karte aus der Datenbank **/
    private Card cardBuilder(ResultSet result) {

        if (result == null){
            return null;
        }

        Card card = null;

        try {
            if(result.next()) {
                card = this.cardBuilder(this.result.getString("card_id"),
                        this.result.getString("card_name"),
                        CardType.valueOf(this.result.getString("card_typ")),
                        this.result.getString("card_element"),
                        this.result.getString("card_power")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.closeStatement();

        return card;
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                             GETTER                            **/
    /*******************************************************************/
    @Override
    /** --> wandelt getAllCards in ArrayListen um **/
    public ArrayList<Card> getAllCardsList(){
        ArrayList<Card> arrayListCards = new ArrayList<>();
        for (Card i : this.getAllCards(null,null,false).keySet()) {
            arrayListCards.add(i);
        }
        return arrayListCards;
    }
    @Override
    /** Liefert eine Karte mithilfe --> ID aus der Datenbank **/
    public Card getCardById(String cardID) {
        this.parameter = new String[] {cardID};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE card_id = ? "+";",
                this.parameter
        );
        return cardBuilder(this.result);
    }
    @Override
    /** Liefert eine Karte mithilfe --> Name aus der Datenbank **/
    public Card getCardByName(String cardName) {
        this.parameter = new String[] {cardName};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE card_name = ? "+";",
                this.parameter
        );
        return cardBuilder(this.result);
    }
    @Override
    /** --> Listet alle Karten in einer HashMap <Karten, KartenName> **/
    public HashMap<Card, CardName>  getAllCards(String attribute, String value, boolean filter) {
        HashMap<Card, CardName>  cards = new HashMap<>();

        if(filter) {
            this.parameter = new String[] {value};
            if(this.setStatement("SELECT * FROM " + this.tableName + " WHERE " + attribute + "= ? ;", this.parameter)== false){
                return null;
            };
            this.setStatement("SELECT * FROM " + this.tableName + " WHERE " + attribute + "= ? ;", this.parameter);

        }else{
            this.parameter = new String[] {};
            this.setStatement("SELECT * FROM " + this.tableName + ";", this.parameter);
        }
        Card card;

        try {
            while (this.result.next()) {

                card = this.cardBuilder(this.result.getString("card_id"),
                        this.result.getString("card_name"),
                        CardType.valueOf(this.result.getString("card_typ")),
                        this.result.getString("card_element"),
                        this.result.getString("card_power")
                );

                cards.put(card , card.getCardName());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cards;
    }

    @Override
    public CardHandlerImpl getInstance() {
        if (CardHandlerImpl.instance == null) {
            CardHandlerImpl.instance = new CardHandlerImpl();
        }
        return CardHandlerImpl.instance;
    }

    /*******************************************************************/


    /*******************************************************************/
    /**                             ADD                               **/
    /*******************************************************************/
    @Override
    /** --> Funkiton fügt Karte mit Daten zur Datenbank hinzugefügt **/
    public Card addCardByData(String cardID, CardName cardName, CardType cardTyp, CardElement cardElement, double cardPower){
        Card card;

        if(cardID == ""){
            cardID = "C-"+this.tokenSupplier.get();
        }
        switch (cardTyp){
            case MONSTER -> {
                card = MonsterCardImpl.builder()
                        .cardID(cardID)
                        .cardName(cardName)
                        .cardTyp(CardType.MONSTER)
                        .cardElement(cardElement)
                        .cardPower(cardPower)
                        .build();
                insert(card);
                return card;
            }
            case SPELL -> {
                card = SpellCardImpl.builder()
                        .cardID(cardID)
                        .cardName(cardName)
                        .cardTyp(CardType.SPELL)
                        .cardElement(cardElement)
                        .cardPower(cardPower)
                        .build();
                insert(card);
                return card;
            }

            default -> {
                return null;
            }
        }
    }
    /*******************************************************************/


    /*******************************************************************/
    /**                     Datenbank - Operatoren                    **/
    /*******************************************************************/
    @Override
    /** Fügt Karte zur Datenbank hinzu **/
    public Card insert(Card newCard) {

       // System.out.println("#INSERT:");
        if (newCard == null){
            return null;
        }

        if(getCardById(""+newCard.getCardID()) == null) {
           // System.out.println("NewCard --> add to Database");
            this.parameter = new String[]{
                    newCard.getCardID(),
                    String.valueOf(newCard.getCardType()),
                    String.valueOf(newCard.getCardName()),
                    String.valueOf(newCard.getCardElement()),
                    String.valueOf(newCard.getCardPower()),
            };
            this.setStatement("INSERT INTO " + this.tableName + "(card_id,card_typ,card_name,card_element,card_power)VALUES(?,?,?,?,?);", this.parameter);
            return getCardById(newCard.getCardID());
        }
        loggerStatic.log("Card already exist");
        //System.out.println(TextColor.ANSI_RED +"Card already exist" + TextColor.ANSI_RESET);

        return getCardByName((""+newCard.getCardName()));
    }
    @Override
    /** Verändert die Karte **/
    public Card update(Card currentCard) {
       // System.out.println("#UPDATE:");
        if(currentCard == null){
            return null;
        }
        this.parameter = new String[] {
                currentCard.getCardID(),
                String.valueOf(currentCard.getCardType()),
                String.valueOf(currentCard.getCardName()),
                String.valueOf(currentCard.getCardElement()),
                String.valueOf(currentCard.getCardPower())
        };

        this.setStatement(
                "UPDATE "+this.tableName +
                        "SET card_typ = ?,card_name = ?,card_element = ?,card_power = ?" +
                        "WHERE card_id = "+ currentCard.getCardID()+";"
                ,this.parameter
        );

        return getCardById(currentCard.getCardID());
    }
    @Override
    /** Löscht die Karte **/
    public boolean delete(Card currentCard) {
      //  System.out.println("#DELETE:");
        this.parameter = new String[]{};
        if(getCardById(currentCard.getCardID()) == null){
            return false;
        }
        Scanner myObj = new Scanner(System.in);
        System.out.println("Are you sure you want to delete (Y/N)?");
        System.out.println("ID: '"+currentCard.getCardID()+"'");
        System.out.println("cardName: '"+currentCard.getCardName()+"'");
        System.out.println("cardTyp: '"+currentCard.getCardType()+"'");
        System.out.println("cardElement: '"+currentCard.getCardElement()+"'");
        System.out.println("cardPower: '"+currentCard.getCardPower()+"'");
        String input = myObj.nextLine();
        if(input.equals("y") || input.equals("Y")) {
            this.setStatement("DELETE FROM " + this.tableName +
                            " WHERE card_id = '" + currentCard.getCardID() + "';"
                    , this.parameter);
        }else{
            return false;
        }
        return true;
    }
    /*******************************************************************/


}
