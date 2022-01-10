package at.technikum.control;

import at.technikum.model.Player;
import at.technikum.repository.PlayerRepository;
import at.technikum.repository.PlayerRepositoryImpl;
import at.technikum.repository.StackRepository;
import at.technikum.repository.StackRepositoryImpl;
import at.technikum.serializer.StackSerializer;
import at.technikum.server.utils.request.RequestImpl;
import at.technikum.server.utils.response.ResponseBuilderImpl;
import at.technikum.server.utils.response.ResponseImpl;
import at.technikum.utils.Printer;
import at.technikum.utils.PrinterImpl;
import at.technikum.utils.tools.TextColor;
import com.google.gson.JsonObject;

public class StackControl extends TextColor {
    StackRepository stackRepository;
    StackSerializer stackSerializer;
    PlayerRepository playerRepository;
    Printer print;

    public StackControl(){
        this.print = new PrinterImpl();
        this.playerRepository = new PlayerRepositoryImpl();
        this.stackRepository = new StackRepositoryImpl();
        this.stackSerializer = new StackSerializer();
    }

    /**
     * --> LOAD STACK
     **/
    public ResponseImpl get(RequestImpl requestImpl) {
        System.out.println("# STACK ");
        /** --> Wenn REQUEST Leer ist **/
        if (requestImpl == null) {
            System.out.println(ANSI_RED + "BAD REQUEST" + ANSI_RESET);
            return new ResponseBuilderImpl().statusBAD(stackSerializer.message("BAD REQUEST").toString());
        }
        /** --> Wenn AUTH Leer ist **/
        if (requestImpl.getAuth() == null) {
            System.out.println(ANSI_RED + "NO TOKEN" + ANSI_RESET);
            new ResponseBuilderImpl().statusUnAuthorized(stackSerializer.message("NO TOKEN").toString());
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerRepository.getPlayerById(requestImpl.getAuth());


        /** -->  ERROR - MELDUNG USER NICHT GEFUNDEN **/
        if (currentPlayer == null) {
            System.out.println(ANSI_RED + "User NOT FOUND" + ANSI_RESET);
            return new ResponseBuilderImpl().statusMethodNotAllowed(stackSerializer.message("User NOT FOUND").toString());
        }

        this.print.printStack(currentPlayer.getStack());
        System.out.println(ANSI_GREEN + "LOADING FINISHED!" + ANSI_RESET);

        /** --> JSON OBJECT **/
        JsonObject jsonObject = stackSerializer.convertStackToJson(currentPlayer.getStack(),true,false);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    } // TODO: 10.01.2022 Fertig


}
