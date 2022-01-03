package main.java.bgu.spl.net.impl.BGS.Commands;

import main.java.bgu.spl.net.impl.BGS.Connections_Impl;
import main.java.bgu.spl.net.impl.BGS.DataBase;
import main.java.bgu.spl.net.impl.rci.Command;

public class RegisterCommand implements Command {
    private final String userName;
    private final String password;
    private final String birthday;

    public RegisterCommand(String userName, String password, String birthday) {
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int connectionId) {
        boolean toReturn = dataBase.register(this,connectionId);
        connections.send(connectionId,toReturn ? new AckMessage(): new ErrorMessage());
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
}
