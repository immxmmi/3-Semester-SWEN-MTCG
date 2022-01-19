package at.technikum.repository;

import at.technikum.model.Trade;
import at.technikum.utils.card.cardTypes.CardType;

import java.util.ArrayList;

public interface TradeRepository {

    Trade addTrade(String tradeID, String userID, String cardID, CardType cardType, double cardPower);

    Trade getTradeByID(String tradeID);

    ArrayList<Trade> getAllTradeByUserID(String userID);

    boolean deleteByID(Trade trade);
}
