package Client;

import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.ServerMsg;
import View.CLI.Cli;
import View.Graphic.Gui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handler Class for managing connection with a Server
 */
public class ConnectionHandler implements Runnable{
    private final ClientCli client;
    private final Cli cli;
    private final ClientGui clientGui;
    private final Gui gui;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private AtomicBoolean active;

    /**
     * Constructor of the class
     * Handles connection error during Stream creation
     * @param server Socket of the connection with the Server
     * @param client Instance of the Client of which is responsible for
     */
    public ConnectionHandler(Socket server, ClientCli client){
        this.client = client;
        this.cli = (Cli) client.getView();
        this.clientGui=null;
        this.gui=null;
        this.active = new AtomicBoolean(true);
        try {
            in = new ObjectInputStream(server.getInputStream());
            out = new ObjectOutputStream(server.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unable to open the Streams (ConnectionHandler)");
        }
    }


    public ConnectionHandler(Socket server,ClientGui client){
        this.clientGui = client;
        this.gui=(Gui) client.getView();
        this.cli=null;
        this.client=null;
        this.active = new AtomicBoolean(true);
        try {
            in = new ObjectInputStream(server.getInputStream());
            out = new ObjectOutputStream(server.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unable to open the Streams (ConnectionHandler)");
        }
    }






    /**
     * Handles the communication with the Server, receiving and processing the messages
     */
    @Override
    public void run() {
        if(client.getView() instanceof Cli) {
            CommandMsg command;
            while (active.get()) {
                command = CliReceiveCommand();
                CliHandleCommand(command);
            }
        }
        else{ //gui

        }

    }

    /**
     * Reads a message from the Server
     * Handles the connection problem and the invalid class scenario
     * @return the CommandMsg sent by the Server
     */
    public CommandMsg CliReceiveCommand(){
        CommandMsg command = null;
        try {
            command = (CommandMsg) in.readObject();
        } catch (IOException e) {
            System.err.println("Problems with the Stream\nThe Server is probably down");
            active.set(false);
            client.CloseClient();
        } catch (ClassNotFoundException e) {
            System.err.println("Invalid Class, not a CliCommandMsg");
        }
        return command;
    }

    /**
     * Handles the message from the server
     * Invokes the relative method to act properly
     * @param command the CommandMsg from the Server
     */
    public void CliHandleCommand(CommandMsg command){
        if(command != null) {
            switch (command.getCommandType()) {
                case NAME:
                    cli.NameHandler(command, this);
                    break;
                case FIRST:
                    cli.FirstHandler(command, this);
                    break;
                case GOD:
                    cli.GodHandler(command, this);
                    break;
                case NUMBER:
                    cli.NumberHandler(command, this);
                    break;
                case POS_INITIAL:
                case POS_WORKER:
                case POS_MOVE:
                case POS_BUILD:
                    cli.PoseHandler(command, this);
                    break;
                case ANS_RESTART:
                case ANS_POWER:
                    cli.AnswerHandler(command, this);
                    break;
                case COM_WELCOME:
                case COM_RESTART:
                case COM_GODS:
                case COM_CHOSEN:
                case COM_WAIT_CHOICE:
                case COM_WAIT_LOBBY:
                case COM_INVALID_WORKER:
                case COM_INVALID_POS:
                case COM_LOSE:
                    cli.CommunicationHandler(command);
                    break;
                case UPDATE_TURN:
                case UPDATE_ACTION:
                    cli.UpdateHandler(command);
                    break;
                case CLOSE_ANOMALOUS:
                case CLOSE_NORMAL:
                case CLOSE_RESTART:
                case CLOSE_SERVER:
                    cli.CloseHandler(command, this);
                    client.CloseClient();
                    break;
                case DEFAULT:
                    break;
            }
        }
    }

    /**
     * Writes a message to the Server
     * Handles the connection problem scenario
     * @param msg the ServerMsg to send
     */
    public void WriteMessage(ServerMsg msg){
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            System.err.println("Unable to write message\nThe Server is probably down");
            active.set(false);
            client.CloseClient();
        }
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }
}
