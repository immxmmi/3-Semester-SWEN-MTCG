package at.technikum.handler;

import at.technikum.handler.repository.CardHolderHandler;
import at.technikum.handler.repository.TradeHandler;
import at.technikum.model.repository.CardHolder;
import at.technikum.model.repository.Trade;
import at.technikum.database.AbstractDBTable;
import at.technikum.model.*;
import at.technikum.model.card.ICard;
import at.technikum.model.card.cardTypes.CardType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TradeHandlerImpl extends AbstractDBTable implements TradeHandler {



    private CardServices cardServices = new CardServices();
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
                        .card(cardServices.getCardById(result.getString("card_id")))
                        .cardTyp(CardType.valueOf(result.getString("card_typ")))
                        .minPower(convertToDouble(result.getString("card_min_power")))
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
        if(cardServices.getCardById(cardID) == null){
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
        ICard card = cardServices.getCardById(cardID);
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

        String transactionID = this.tokenSupplier.get();
        CardHolder cardHolder = checkCardHolderByID(userID,cardID);
        if(tradeID == ""){
            tradeID = transactionID;
        }

        tradeID  = "T-" + tradeID;

        if(getTradeByID(tradeID) != null){return null;}
        if(!checkCardByID(cardID)){return null;}
        if(cardHolder == null){
            return null;
        }

        this.parameter = new String[]{
                tradeID,
                userID,
                cardID,
                "" + cardType,
                "" + cardPower
        };

        this.setStatement(
                "INSERT INTO "+this.tableName+" (trade_id, user_id, card_id, card_typ , card_min_power) values ( ?,?,?,?,?)",
                this.parameter
        );

        cardHolderHandler.updateLocked(cardHolder,true);

        return getTradeByID(tradeID);
    }
    @Override
    public Trade getTradeByID(String tradeID){
        this.parameter = new String[]{tradeID};
        this.setStatement(
                "SELECT * FROM " + this.tableName + " WHERE trade_id = ? " + ";",
                this.parameter
        );

        return tradeBuilder(this.result);
    }
    @Override
    public ArrayList<Trade> getAllTradeByUserID(String userID) {
        ArrayList<Trade> trades = new ArrayList<>();
        this.parameter = new String[]{userID};
        this.setStatement("SELECT * FROM " + this.tableName + " WHERE user_id = ? ;", this.parameter);

        try {
            while (this.result.next()) {
               String tradeID = result.getString("trade_id");
               ICard card = cardServices.getCardById(result.getString("card_id"));
               CardType cardType = CardType.valueOf(result.getString("card_typ"));
               Double minPower = convertToDouble(result.getString("card_min_power"));
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
    public boolean deleteByID(Trade trade) {
        // System.out.println("#DELETE ITEM");

        CardHolder cardHolder = checkCardHolderByID(trade.getUserID(),trade.getCard().getCardID());
        if(cardHolder == null){return false;}
        this.parameter = new String[]{trade.getTradeID()};
        this.setStatement("DELETE FROM " + this.tableName + " WHERE trade_id = ? ;", this.parameter);
        this.closeStatement();
        cardHolderHandler.updateLocked(cardHolder,false);
        return true;
    }
    private void updatePrice(String transactionID, int newPrice) {
        this.parameter = new String[]{"" + newPrice, transactionID};
        setStatement("UPDATE  \"" + this.tableName + "\" SET  price = ? WHERE  transaction_id= ?;", this.parameter);
    }
    /*******************************************************************/




}
