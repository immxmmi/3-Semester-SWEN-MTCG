package at.technikum.control;

import at.technikum.control.repository.Get;
import at.technikum.handler.PlayerHandlerImpl;
import at.technikum.handler.StackHandlerImpl;
import at.technikum.handler.repository.PlayerHandler;
import at.technikum.handler.repository.StackHandler;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Player;
import at.technikum.serializer.StackSerializer;
import at.technikum.server.request.RequestImpl;
import at.technikum.server.response.ResponseBuilderImpl;
import at.technikum.server.response.ResponseImpl;
import at.technikum.utils.Printer;
import at.technikum.utils.TextColor;
import com.google.gson.JsonObject;

public class StackControl implements Get {
    private TextColor textColor;
    private StackHandler stackHandler;
    private StackSerializer stackSerializer;
    private PlayerHandler playerHandler;
    private Printer print;
    private LoggerStatic loggerStatic;

    public StackControl(){
        this.textColor = new TextColor();
        this.print = new Printer();
        this.playerHandler = new PlayerHandlerImpl();
        this.stackHandler = new StackHandlerImpl();
        this.stackSerializer = new StackSerializer();
        this.loggerStatic = LoggerStatic.getInstance();
    }

    /*** --> LOAD STACK - **/
    public ResponseImpl get(RequestImpl requestImpl) {
        //System.out.println("# STACK ");
        loggerStatic.log("\n# STACK\n");
        /** --> WENN REQUEST LEER IST --> WENN AUTH LEER IST --> WENN USER NICHT EXISTIERT **/
        ResponseImpl responseImpl = new ResponseBuilderImpl().requestErrorHandler(requestImpl, true, false, true);
        if (responseImpl != null) {
            return responseImpl;
        }

        /** --> INSTANCE **/
        Player currentPlayer = this.playerHandler.getItemById(requestImpl.getAuth());


        this.print.printStack(currentPlayer.getStack());
        loggerStatic.log("\nLOADING FINISHED!\n");
        //System.out.println(this.textColor.ANSI_GREEN + "LOADING FINISHED!" + this.textColor.ANSI_RESET);

        /** --> JSON OBJECT **/
        JsonObject jsonObject = stackSerializer.convertStackToJson(currentPlayer.getStack(),false,true);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }


}
