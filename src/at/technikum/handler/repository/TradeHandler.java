package at.technikum.handler.repository;

import at.technikum.model.card.cardTypes.CardType;
import at.technikum.model.repository.Trade;

import java.util.ArrayList;

public interface TradeHandler {

    boolean checkTradeOwner(String holderID, Trade trade);

    boolean checkTradeRequirement(Trade trade, String cardID);

    Trade addTrade(String tradeID, String userID, String cardID, CardType cardType, double cardPower);

    Trade getItemById(String tradeID);

    ArrayList<Trade> getAllTradeByUserID(String userID);

    boolean delete(Trade trade);

}
