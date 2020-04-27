package Client;

import ComunicationProtocol.CliCommandMsg;
import ComunicationProtocol.ServerMsg;
import View.CLI.Cli;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for managing a new connection
 */
public class ConnectionHandler implements Runnable{
    private Socket server;
    private Client client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private AtomicBoolean active;

    /**
     * Constructor of the class
     * @param server
     * @param client
     */
    public ConnectionHandler(Socket server, Client client){
        this.server = server;
        this.client = client;
        this.active = new AtomicBoolean(true);
        try {
            in = new ObjectInputStream(server.getInputStream());
            out = new ObjectOutputStream(server.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unable to open the Streams (ConnectionHandler)");
        }
    }

    /**
     * Receiving messages...
     */
    @Override
    public void run() {
        if(!client.isLayout()) {
            CliCommandMsg command;
            while (active.get()) {
                command = CliReceiveCommand();
                CliHandleCommand(command, (Cli) client.getView());
            }
        }
    }

    /**
     * Reading an input command...
     * @return
     */
    public CliCommandMsg CliReceiveCommand(){
        CliCommandMsg command = null;
        try {
            command = (CliCommandMsg) in.readObject();
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
     * Handling the read message based on  the type
     * @param command
     * @param cli
     */
    public void CliHandleCommand(CliCommandMsg command, Cli cli){
        if(command != null) {
            switch (command.getCommandType()) {
                case NAME:
                    cli.NameHandler(command, this);
                    break;
                case COORDINATES:
                    cli.CoordinatesHandler(command, this);
                    break;
                case NUMBER:
                    cli.NumberHandler(command, this);
                    break;
                case ANSWER:
                    cli.AnswerHandler(command, this);
                    break;
                case GOD:
                    cli.GodHandler(command, this);
                    break;
                case CLOSE:
                    cli.CloseHandler(command, this);
                    client.CloseClient();
                    break;
                case UPDATE:
                    cli.UpdateHandler(command);
                    break;
                case COMMUNICATION:
                    cli.CommunicationHandler(command);
                    break;
            }
        }
    }

    /**
     * Writing a message in the output stream
     * @param msg
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

    public boolean Layout(){
        return client.isLayout();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }
}
