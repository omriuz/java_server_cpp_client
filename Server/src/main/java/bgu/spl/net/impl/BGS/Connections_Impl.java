package bgu.spl.net.impl.BGS;

import bgu.spl.net.api.Bidi.Connections;
import bgu.spl.net.srv.ConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;

public class Connections_Impl implements Connections <Message> {

    private ConcurrentHashMap<Integer, ConnectionHandler> connectionHandlerHashMap;
    private Integer nextID;

    public Connections_Impl() {
        this.connectionHandlerHashMap = new ConcurrentHashMap<>();
        nextID = 1;
    }

    @Override
    public boolean send(int connectionId, Message msg) {
        ConnectionHandler connectionHandler = connectionHandlerHashMap.get(connectionId);
        if (connectionHandler == null)
            return false;
        connectionHandler.send(msg);
        return true;
    }

    @Override
    public void broadcast(Message msg) {
        for (ConnectionHandler connectionHandler : connectionHandlerHashMap.values()) {
            connectionHandler.send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        if (connectionHandlerHashMap.get(connectionId) != null)
            connectionHandlerHashMap.remove(connectionId);
    }

    public synchronized int addNewHandler(ConnectionHandler CH) {
        int toRet = nextID;
        this.connectionHandlerHashMap.put(toRet, CH);
        nextID++;
        return toRet;
    }
}

