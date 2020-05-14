package CommunicationProtocol;

/**
 * Enumeration of different types of messages
 */
public enum CommandType {
    DEFAULT, //client answer
    NAME, //player name
    FIRST, //player number
    GOD, //god to draw
    NUMBER, //god to chose
    POS_INITIAL, //worker initial pose
    POS_WORKER, //worker to use
    POS_MOVE, //box where to move
    POS_BUILD, //box where to build
    ANS_RESTART, //to restart game
    ANS_POWER, //to use god power
    COM_WELCOME, //welcome packet
    COM_RESTART, //restart packet
    COM_GODS, //god drawn packet
    COM_CHOSEN, //god chosen packet
    COM_WAIT_CHOICE, //wait others' move
    COM_WAIT_LOBBY, //wait others join the lobby
    COM_INVALID_WORKER, //invalid worker
    COM_INVALID_POS, //invalid box
    COM_LOSE, //lose game
    UPDATE_TURN, //map update beginning
    UPDATE_ACTION, //map update middle
    CLOSE_ANOMALOUS, //player disconnected
    CLOSE_NORMAL, //game finished
    CLOSE_RESTART, //game already in act
    CLOSE_SERVER //server is down
}
