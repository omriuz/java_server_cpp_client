package main.java.bgu.spl.net.impl.BGS;

import main.java.bgu.spl.net.api.Bidi.BidiMessagingProtocol;
import main.java.bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class RemoteCommandInvocationProtocol<T> implements BidiMessagingProtocol<Serializable> {
    private int connectionId;
    private Connections_Impl connections;
    private final DataBase dataBase;

    public RemoteCommandInvocationProtocol(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public void start(int connectionId, Connections_Impl connections) {
        this.connections = connections;
        this.connectionId = connectionId;
    }

    @Override
    public void process(Serializable msg) {
         ((Command) msg).execute(dataBase,connections,connectionId);
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

}
