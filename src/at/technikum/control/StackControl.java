package at.technikum.control;

import at.technikum.control.repository.Get;
import at.technikum.logger.LoggerStatic;
import at.technikum.model.repository.Player;
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
import at.technikum.utils.TextColor;
import com.google.gson.JsonObject;

public class StackControl implements Get {
    private TextColor textColor;
    private StackRepository stackRepository;
    private StackSerializer stackSerializer;
    private PlayerRepository playerRepository;
    private Printer print;
    private LoggerStatic loggerStatic;

    public StackControl(){
        this.textColor = new TextColor();
        this.print = new PrinterImpl();
        this.playerRepository = new PlayerRepositoryImpl();
        this.stackRepository = new StackRepositoryImpl();
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
        Player currentPlayer = this.playerRepository.getItemById(requestImpl.getAuth());


        this.print.printStack(currentPlayer.getStack());
        loggerStatic.log("\nLOADING FINISHED!\n");
        //System.out.println(this.textColor.ANSI_GREEN + "LOADING FINISHED!" + this.textColor.ANSI_RESET);

        /** --> JSON OBJECT **/
        JsonObject jsonObject = stackSerializer.convertStackToJson(currentPlayer.getStack(),false,true);
        /** --> STATUS OK **/
        return new ResponseBuilderImpl().statusOK(jsonObject.toString());
    }


}
