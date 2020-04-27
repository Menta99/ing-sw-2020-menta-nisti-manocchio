package ComunicationProtocol;

import java.io.Serializable;

/**
 * Abstract class for communication messages
 */
public abstract class Message implements Serializable {
    private CommandType commandType = CommandType.COMMUNICATION;

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
}
