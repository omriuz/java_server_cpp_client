package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.Connections_Impl;
import bgu.spl.net.impl.BGS.DataBase;
import bgu.spl.net.impl.rci.Command;

public class RegisterCommand implements Command {
    private final String userName;
    private final String password;
    private final String birthday;
    private int opCode;

    public RegisterCommand(String userName, String password, String birthday) {
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int connectionId) {
        this.opCode = 1;
        boolean toReturn = dataBase.register(this,connectionId);
        connections.send(connectionId,toReturn ? new AckMessage(opCode) : new ErrorMessage(opCode));
    }


    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }
    public String toString(){
        return "userName: " + userName + "\npassword: " + password +"\nbirthday: " + birthday; 
    }
}
