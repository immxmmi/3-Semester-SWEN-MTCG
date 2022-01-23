package at.technikum.handler;

import at.technikum.database.AbstractDBTable;
import at.technikum.handler.repository.CardHandler;
import at.technikum.handler.repository.CardHolderHandler;
import at.technikum.handler.repository.Repository;
import at.technikum.handler.repository.TradeHandler;
import at.technikum.model.TradeImpl;
import at.technikum.model.card.Card;
import at.technikum.model.card.cardTypes.CardType;
import at.technikum.model.repository.CardHolder;
import at.technikum.model.repository.Trade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TradeHandlerImpl extends AbstractDBTable implements TradeHandler, Repository<Trade> {



    private CardHandler cardHandler = new CardHandlerImpl();
    private CardHolderHandler cardHolderHandler = new CardHolderHandlerImpl();

    /*******************************************************************/
    /**                          Constructor                          **/
    /*******************************************************************/
    public TradeHandlerImpl() {
        this.tableName = "trade";
    }
    /*******************************************************************/



    /*******************************************************************/
    /**                            BUILDER                            **/
    /*******************************************************************/
    private Trade tradeBuilder(ResultSet result) {

        try {
            if (result.next()) {
                Trade trade = (Trade) TradeImpl.builder()
                        .tradeID(result.getString("trade_id"))
                        .userID(result.getString("user_id"))
                        .card(cardHandler.getItemById(result.getString("card_id")))
                        .cardTyp(CardType.valueOf(result.getString("card_typ")))
                        .minPower( this.tools.convertToDouble(result.getString("card_min_power")))
                        .build();
                this.closeStatement();

                return trade;
            }
        } catch (SQLException e) {

            System.out.println("GETOBJECT -ERRROR: " + e);
            e.printStackTrace();
        }
        this.closeStatement();
        return null;
    }
    /*******************************************************************/


    private boolean checkCardByID(String cardID){
        if(cardHandler.getItemById(cardID) == null){
            return false;
        }
        return true;
    }
    private CardHolder checkCardHolderByID(String holderID, String cardID){
        return cardHolderHandler.getCardHolder(holderID,cardID);
    }

    @Override
    public boolean checkTradeOwner(String holderID, Trade trade){
        return holderID.equals(trade.getUserID());
    }

    @Override
    public boolean checkTradeRequirement(Trade trade, String cardID){
        Card card = cardHandler.getItemById(cardID);
        if(trade.getMinPower() > card.getCardPower()){
            return false;
        }
        if(trade.getCardTyp() != card.getCardType()){
            return false;
        }

        return true;
    }

    /*******************************************************************/
    /**                     Datenbank - Operatoren                    **/
    /*******************************************************************/
    @Override
    public Trade addTrade(String tradeID, String userID, String cardID, CardType cardType, double cardPower) {

        if(getItemById(tradeID) != null){return null;}
        if(!checkCardByID(cardID)){return null;}

        CardHolder cardHolder = checkCardHolderByID(userID,cardID);
        if(cardHolder == null){return null;}

        if(tradeID == ""){
            tradeID = "T-" + tools.tokenSupplier.get();
        }else{
            tradeID  = "T-" + tradeID;
        }

        Trade trade = TradeImpl.builder()
                .tradeID( this.tools.tokenSupplier.get())
                .userID(userID)
                .card(cardHandler.getItemById(cardID))
                .cardTyp(cardType)
                .build();

        insert(trade);
        cardHolderHandler.updateLocked(cardHolder,true);

        return getItemById(tradeID);
    }
    @Override
    public Trade getItemById(String tradeID){
        this.parameter = new String[]{tradeID};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE trade_id = ? " + ";",
                this.parameter
        );

        return tradeBuilder(this.result);
    }

    @Override
    public Trade insert(Trade item) {
        //System.out.println(ANSI_BLUE + "#INSERT:" + ANSI_RESET);
        if (item == null) {
            return null;
        }
        this.parameter = new String[]{
                "" + item.getTradeID(),
                "" + item.getUserID(),
                "" + item.getCard().getCardID(),
                "" + item.getCardTyp(),
                "" + item.getMinPower()
        };
        this.setStatement(
                "INSERT INTO "+this.tableName+" (trade_id, user_id, card_id, card_typ , card_min_power) values ( ?,?,?,?,?)",
                this.parameter
        );
        return getItemById(item.getUserID());
    }

    @Override
    public Trade update(Trade item) {
        return null;
    }

    @Override
    public ArrayList<Trade> getAllTradeByUserID(String userID) {
        ArrayList<Trade> trades = new ArrayList<>();
        this.parameter = new String[]{userID};
        this.setStatement("SELECT * FROM " + this.tableName + " WHERE user_id = ? ;", this.parameter);

        try {
            while (this.result.next()) {
               String tradeID = result.getString("trade_id");
               Card card = cardHandler.getItemById(result.getString("card_id"));
               CardType cardType = CardType.valueOf(result.getString("card_typ"));
               Double minPower =  this.tools.convertToDouble(result.getString("card_min_power"));
               Trade trade = TradeImpl.builder()
                       .tradeID(tradeID)
                       .card(card)
                       .cardTyp(cardType)
                       .minPower(minPower)
                       .build();
               trades.add(trade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.closeStatement();
        return trades;
    }

    @Override
    public boolean delete(Trade trade) {
        // System.out.println("#DELETE ITEM");

        CardHolder cardHolder = checkCardHolderByID(trade.getUserID(),trade.getCard().getCardID());
        if(cardHolder == null){return false;}
        this.parameter = new String[]{trade.getTradeID()};
        this.setStatement("DELETE FROM " + this.tableName + " WHERE trade_id = ? ;", this.parameter);
        this.closeStatement();
        cardHolderHandler.updateLocked(cardHolder,false);
        return true;
    }

    /*******************************************************************/




}
