package bgu.spl.net.impl.BGS;


import bgu.spl.net.srv.Server;


public class ReactorMain {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase(); 
        Server.reactor(Integer.valueOf(args[1]),
         Integer.valueOf(args[0]),
         () -> new RemoteCommandInvocationProtocol (dataBase),
          () -> new ObjectEncoderDecoder()).serve();
    }
}
