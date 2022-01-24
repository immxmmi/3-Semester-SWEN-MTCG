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
                        .minPower(this.tools.convertToDouble(result.getString("card_min_power")))
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

        String genTradeID = this.tools.tokenSupplier.get();
        CardHolder cardHolder = checkCardHolderByID(userID,cardID);
        if(tradeID == ""){
            tradeID = genTradeID;
        }

        tradeID  = "T-" + tradeID;

        if(getItemById(tradeID) != null){return null;}
        if(!checkCardByID(cardID)){return null;}
        if(cardHolder == null){
            return null;
        }

       Trade trade = TradeImpl.builder()
                .tradeID(tradeID)
                .userID(userID)
                .card(cardHandler.getItemById(cardID))
                .cardTyp(cardType)
                .minPower(cardPower)
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
    public Trade insert(Trade trade) {

        this.parameter = new String[]{
                trade.getTradeID(),
                trade.getUserID(),
                trade.getCard().getCardID(),
                "" + trade.getCardTyp(),
                "" + trade.getMinPower()
        };

        this.setStatement(
                "INSERT INTO "+this.tableName+" (trade_id, user_id, card_id, card_typ , card_min_power) values ( ?,?,?,?,?)",
                this.parameter
        );

        return getItemById(trade.getTradeID());
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
                Double minPower = this.tools.convertToDouble(result.getString("card_min_power"));
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
        //System.out.println(this.statement);
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
