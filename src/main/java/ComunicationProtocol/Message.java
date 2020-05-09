package ComunicationProtocol;

import java.io.Serializable;

/**
 * Abstract class for communication messages
 */
public interface Message{
    //private CommandType commandType;

    public CommandType getCommandType();
    /*
    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
    */
}
