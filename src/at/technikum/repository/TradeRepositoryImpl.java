package at.technikum.repository;

import at.technikum.database.AbstractDBTable;
import at.technikum.model.Store;
import at.technikum.model.StoreImpl;
import at.technikum.model.Trade;
import at.technikum.model.TradeImpl;
import at.technikum.utils.card.cardTypes.CardType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TradeRepositoryImpl extends AbstractDBTable implements TradeRepository{

    /*******************************************************************/
    /**                            BUILDER                            **/
    /*******************************************************************/
    private Trade tradeBuilder(ResultSet result) {

        try {
            if (result.next()) {
                Trade trade = (Trade) TradeImpl.builder()
                        .tradeID(result.getString("trade_id"))
                        .cardID(result.getString("card_id"))
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





}
