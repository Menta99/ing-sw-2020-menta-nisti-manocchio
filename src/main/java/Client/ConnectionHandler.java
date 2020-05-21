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
    private final Gui gui;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private AtomicBoolean active;

    /**
     * Constructor of the class (CLI case)
     * Handles connection error during Stream creation
     * @param server Socket of the connection with the Server
     * @param client Instance of the Client of which is responsible for
     */
    public ConnectionHandler(Socket server, ClientCli client){
        this.client = client;
        this.cli = (Cli) client.getView();
        this.gui = null;
        this.active = new AtomicBoolean(true);
        try {
            in = new ObjectInputStream(server.getInputStream());
            out = new ObjectOutputStream(server.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unable to open the Streams (ConnectionHandler)");
        }
    }

    /**
     * Constructor of the class (GUI case)
     * Handles connection error during Stream creation
     * @param server Socket of the connection with the Server
     * @param gui Instance of the Gui of which is responsible for
     */
    public ConnectionHandler(Socket server, Gui gui){
        this.cli=null;
        this.client=null;
        this.gui = gui;
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
        if(cli != null) {
            CommandMsg command;
            while (active.get()) {
                command = CliReceiveCommand();
                CliHandleCommand(command);
            }
        }
        else{
            CommandMsg command;
            while (active.get()) {
                command = CliReceiveCommand();
                GuiHandleCommand(command);
            }
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
            if(client!=null) {
                client.CloseClient();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Invalid Class, not a CliCommandMsg");
        }
        return command;
    }

    /**
     * Handles the message from the server (CLI)
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
     * Handles the message from the server (GUI)
     * Invokes the relative method to act properly
     * @param command the CommandMsg from the Server
     */
    public void GuiHandleCommand(CommandMsg command){
        if(command != null) {
            switch (command.getCommandType()) {
                case NAME:
                    gui.NameHandler(command, this);
                    break;
                case FIRST:
                    gui.FirstHandler(command, this);
                    break;
                case GOD:
                    gui.GodHandler(command, this);
                    break;
                case NUMBER:
                    gui.NumberHandler(command, this);
                    break;
                case POS_INITIAL:
                case POS_WORKER:
                case POS_MOVE:
                case POS_BUILD:
                    gui.PoseHandler(command, this);
                    break;
                case ANS_RESTART:
                case ANS_POWER:
                    gui.AnswerHandler(command, this);
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
                    gui.CommunicationHandler(command,this);
                    break;
                case UPDATE_TURN:
                case UPDATE_ACTION:
                    gui.UpdateHandler(command, this);
                    break;
                case CLOSE_ANOMALOUS:
                case CLOSE_NORMAL:
                case CLOSE_RESTART:
                case CLOSE_SERVER:
                    gui.CloseHandler(command, this);
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
            if(client!=null) {
                client.CloseClient();
            }
        }
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }
}
