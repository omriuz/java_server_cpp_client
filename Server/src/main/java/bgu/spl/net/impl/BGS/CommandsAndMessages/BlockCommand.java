package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.*;

public class BlockCommand implements Command {

    private int opCode;
    private String userName;

    public BlockCommand(String userName) {
        this.userName = userName;
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int connectionId) {
        this.opCode = 12;
        boolean valid = dataBase.isValidId(connectionId) && dataBase.isRegistered(userName);
        if(!valid)
            connections.send(connectionId,new ErrorMessage(opCode));
        else{
            dataBase.block(connectionId,userName);
            connections.send(connectionId,new AckMessage(opCode));
        }
    }
}
