package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.Connections_Impl;
import bgu.spl.net.impl.BGS.DataBase;
import bgu.spl.net.impl.rci.Command;

public class BlockCommand implements Command {

    private int opCode;
    private String userName;

    public BlockCommand(String userName) {
        this.userName = userName;
        this.opCode = 12;
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int connectionId) {
        boolean valid = dataBase.isValid(connectionId) && dataBase.isRegistered(userName);
        if(!valid)
            connections.send(connectionId,new ErrorMessage(opCode));
        else{
            dataBase.block(connectionId,userName);
            connections.send(connectionId,new AckMessage(opCode));
        }
    }
}
