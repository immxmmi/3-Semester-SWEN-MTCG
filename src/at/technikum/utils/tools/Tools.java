package at.technikum.utils.tools;

import at.technikum.utils.card.ICard;
import at.technikum.utils.card.cardTypes.CardElement;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

public class Tools {

    // COLORS
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    /**
     * HASH STRING
     ***********************************************/
    protected static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    protected static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    protected String hashString(String text) {

        try {
            return toHexString(getSHA(text));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        }

        return text;
    }
    /**************************************************************/


    /**
     * DATE
     ******************************************************/
    // DATUM + UHRZEIT
    protected String formatDateTime(int format) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter df = null;

        if (format < 1 || format > 4) {
            format = 2;
        }

        switch (format) {
            case 1:
                df = DateTimeFormatter.BASIC_ISO_DATE;
                break;    // 20160131
            case 2:
                df = DateTimeFormatter.ISO_LOCAL_DATE;
                break;   // 2016-18-31
            case 3:
                df = DateTimeFormatter.ISO_DATE_TIME;
                break;   // 2016-01-31T20:07:07.095
            case 4:
                df = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm");
                break;     // 31.01.2016 20:07
        }

        return df.toString();
    }

    // DATUM
    protected String formatDate(int format) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter df = null;

        switch (format) {
            case 2:
                df = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
                break;      // Sonntag, 31. Januar 2016
            case 3:
                df = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
                break;     // 31. Januar 2016
            case 4:
                df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
                break;    // 31.01.2016
            case 5:
                df = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
                break;   // 31.01.16
            default:
                return date.toString();   // 2016-01-31
        }

        return df.format(date);
    }
    /*************************************************************/


    /**
     * RANDOM
     ***************************************************/
    // TOKEN GENERATOR --> erstellt einzigartige IDs
    protected Supplier<String> tokenSupplier = () -> {
        StringBuilder token = new StringBuilder();
        long currentTimeInMilisecond = Instant.now().toEpochMilli();
        return token.append(currentTimeInMilisecond).append("-")
                .append(UUID.randomUUID().toString()).toString();
    };

    // Gibt eine Zahl zwischen MIN und MAX
    protected int randomNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt((max) - min) + min;
    }
    /*************************************************************/


    /**
     * CONVERTING
     ***********************************************/
    // CONVERT TO NUMBER - Double
    protected Double convertToDouble(String text) {
        return Double.parseDouble(text);
    }

    // CONVERT TO NUMBER - Integer
    protected Integer convertToInt(String text) {
        return Integer.parseInt(text);
    }

    // CONVERT TO BOOLEAN
    protected boolean convertToBoolean(String text) {
        return Boolean.parseBoolean(text);
    }

    /*************************************************************/

    protected void printCards(ArrayList<ICard> stack, String name) {
        int maxNumber = 2;
        int maxType = 8;
        int maxName = 12;
        int maxElement = 8;
        int maxPower = 4;
        int maxH1 = 5;
        int number = 0;
        if (name.equals("")) {
            name = "STACK";
        }
        System.out.println("|#################################################|");
        System.out.println("|#                     " + name.toUpperCase() + checkSpace(name, maxH1) + "                    #|");
        System.out.println("|#################################################|");
        System.out.println("| # " + checkSpace("# ", maxNumber) + "| TYPE " + checkSpace("TYPE ", maxType) + "| NAME " + checkSpace("NAME ", maxName) + "| ELEMENT " + checkSpace("ELEMENT ", maxElement) + "| POWER |");


        for (ICard card : stack) {

            if (card.getCardElement() == CardElement.WATER) {
                System.out.print(ANSI_CYAN);
            }
            if (card.getCardElement() == CardElement.FIRE) {
                System.out.print(ANSI_RED);
            }
            if (card.getCardElement() == CardElement.NORMAL) {
                System.out.print(ANSI_RESET);
            }

            System.out.println(
                    "| " + number + checkSpace("" + number, maxNumber) +
                            "| " + card.getCardType() + checkSpace(card.getCardType().toString(), maxType) +
                            "| " + card.getCardName() + checkSpace(card.getCardName().toString(), maxName) +
                            "| " + card.getCardElement() + checkSpace(card.getCardElement().toString(), maxElement) +
                            "| " + card.getCardPower() + checkSpace("" + card.getCardPower(), maxPower) +
                            " |"
            );
            System.out.print(ANSI_RESET);
            number++;
        }

        System.out.println("|#################################################|");

    }

    protected String addSpace(int spaceSize) {
        String space = "";

        for (int i = 0; i <= spaceSize; i++) {
            space = space + " ";
        }
        return space;
    }

    protected String checkSpace(String word, int size) {
        int a = size - word.length();
        if (a < 0) {
            return "word too long";
        }
        return addSpace(a);
    }
}
