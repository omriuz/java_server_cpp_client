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
        std::cout << answer << std::endl;
        if(isAckMessageForLogout(answer)){
            std::cout << "Exiting...\n" << std::endl;
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