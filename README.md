A server-client implementation.
The server was implemented in java and the client was implemented in cpp.
The information is passed between them as JSON strings.

command line to activate each Server:

Ractor : mvn exec:java =Dexec.mainClass="bgu.spl.net.impl.BGS.ReactorMain" -Dexec.args="<port> <Num of threads>"
  
Thread Per Client :  mvn exec:java =Dexec.mainClass="bgu.spl.net.impl.BGS.TPCMain" -Dexec.args="<port>"

Examples for each Message:

REGISTER Morty a123 00-00-0000
LOGIN Morty 1234 1
LOGOUT
FOLLOW 0 Rick //(follow)
FOLLOW 1 Rick //(unfollow)
POST hello my followers and @Rick
PM Rick hey there grandpa
LOGSTAT
STAT Rick Bird-person
BLOCK Rick

Location of the set of filterd words:
bgu.spl.net.impl.BGS.DataBase - in the private addWordsToFilter method
