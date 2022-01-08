#include <socketTask.h>

using namespace std;

socketTask::socketTask(ConnectionHandler & connectionHandler) : handler(connectionHandler), shouldTerminate(false){}


void socketTask::operator()() {
    
    while(!shouldTerminate){
        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        std::string answer;
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!handler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
		int len=answer.length();
		// A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
		// we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
        answer.resize(len-1);
        printMessage(answer);
        if(isAckMessageForLogout(answer)){
            std::cout << "Client exiting..." << std::endl;
            break;
        }
    }
}

short socketTask::bytesToShort(char* bytesArr){
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

bool socketTask::isAckMessageForLogout(std::string answer){
    std::stringstream stream(answer);
    std::string opCode;
    std::getline(stream,opCode,' ');
    if(opCode.compare("10")!=0)
        return false;
    std::getline(stream,opCode,' ');
    if(opCode.compare("3")!=0)
        return false;
    return true;
    
}

void socketTask::printMessage(std::string answer){
    std::stringstream stream(answer);
    std::string opCode;
    std::getline(stream,opCode,' ');
    std::string toPrint = "";
    if(opCode.compare("10")==0)
        toPrint = "ACK ";
    else if(opCode.compare("11")==0)
        toPrint = "ERROR ";
    else 
        toPrint = "NOTFICATION ";
    if(toPrint.compare("NOTFICATION ")==0){
        std::string privateOrPublic;
        std::string PostingUSer;
        std::string content;
        std::getline(stream,privateOrPublic,' ');
        std::getline(stream,PostingUSer,' ');
        std::getline(stream,content,(char)(31));
        if(privateOrPublic.compare("0")==0)
            toPrint += "PM";
        else
            toPrint += "PUBLIC";
        toPrint +=" " + PostingUSer;
        toPrint +=" " + content;
    }
    else{
    std::string sentFor;
    std::string moreInfo;
    std::getline(stream,sentFor,' ');
    std::getline(stream,moreInfo,(char)(31));
    toPrint += sentFor;
    toPrint += " ";
    toPrint += moreInfo;
    }

    std::cout<< toPrint <<endl;
}