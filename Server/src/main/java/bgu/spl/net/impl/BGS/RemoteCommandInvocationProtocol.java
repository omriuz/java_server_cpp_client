package bgu.spl.net.impl.BGS;

import bgu.spl.net.api.Bidi.BidiMessagingProtocol;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.impl.rci.Communication;

public class RemoteCommandInvocationProtocol implements BidiMessagingProtocol<Communication> {
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
    public void process(Communication msg) {
        ((Command)msg).execute(dataBase,connections,connectionId);
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

}
