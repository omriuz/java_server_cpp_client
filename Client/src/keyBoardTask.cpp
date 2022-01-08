
#include <keyBoardTask.h>

using namespace std;
using boost::property_tree::ptree;

keyBoardTask::keyBoardTask(ConnectionHandler & connectionHandler) : handler(connectionHandler), shouldTerminate(false),opCode(0){};


void keyBoardTask::operator()() {

    while(!shouldTerminate){
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
		std::string line(buf);
        line = editMessage(line);
        // std::cout << line; 
		short length=line.length();
        if (!handler.sendLine(line,opCode,length)) {
            // std::cout << "keyBoardTask: Disconnected. Exiting...\n" << std::endl;
            break;
        }
		// connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
        // std::cout << "keyBoardTask: Sent " << length+1 << " bytes to server" << std::endl;
    }
    }
std::string keyBoardTask::editMessage(std::string rawMsg){
    std::stringstream stream(rawMsg);
    std::string command;
    //BUG: what if the rawMsg is LOGOUT or LOGSTAT witout white space
    std::getline(stream,command,' ');
    std::string jsonMessage = "";
    if(command.compare("REGISTER") == 0){
        jsonMessage = toJsonRegister(rawMsg);
        opCode = 1;
    }else if(command.compare("LOGIN") == 0){
        jsonMessage = toJsonLogin(rawMsg);
        opCode = 2;
    }else if(command.compare("LOGOUT") == 0){
        jsonMessage = toJsonLogout("3");
        opCode = 3;
    }else if(command.compare("FOLLOW") == 0){
        jsonMessage = toJsonFollow(rawMsg);
        opCode = 4;
    }else if(command.compare("POST") == 0){
        jsonMessage = toJsonPost(rawMsg);
        opCode = 5;
    }else if(command.compare("PM") == 0){
        jsonMessage = toJsonPM(rawMsg);
        opCode = 6;
    }else if(command.compare("LOGSTAT") == 0){
        jsonMessage = toJsonLogStat("7");
        opCode = 7;
    }else if(command.compare("STAT") == 0){
        jsonMessage = toJsonStat(rawMsg);
        opCode = 8;
    }else if(command.compare("BLOCK") == 0){
        jsonMessage = toJsonBlock(rawMsg);
        opCode = 12;
    }

    return jsonMessage;
}
std::string keyBoardTask::toJsonRegister(std::string rawMessage){
    ptree root;
    std::size_t index = rawMessage.find(' ');
    std::string editMessage = rawMessage.substr(index + 1);
    std::stringstream stream(editMessage);
    std::stringstream json;
    std::string userName;
    std::string password;
    std::string birthday;

    std::getline(stream ,userName , ' ');
    std::getline(stream ,password , ' ');
    std::getline(stream ,birthday , ' ');

    root.put("userName", userName);
    root.put("password", password);
    root.put("birthday", birthday);

    boost::property_tree::write_json(json, root);
    return json.str();

}
std::string keyBoardTask::toJsonLogin(std::string rawMessage){
    ptree root;
    std::size_t index = rawMessage.find(' ');
    std::string editMessage = rawMessage.substr(index + 1);
    std::stringstream stream(editMessage);
    std::stringstream json;
    std::string userName;
    std::string password;
    std::string captcha;

    std::cout << captcha;
    std::getline(stream ,userName , ' ');
    std::getline(stream ,password , ' ');
    std::getline(stream ,captcha , ' ');

    if(captcha.compare("1") == 0){
        captcha = "true";
    }else{
        captcha = "false";
    }
    
    root.put("userName", userName);
    root.put("password", password);
    root.put("captcha", captcha);

    boost::property_tree::write_json(json, root);
    return json.str();
}
std::string keyBoardTask::toJsonLogout(std::string rawMessage){
    ptree root;
    std::stringstream json;

    root.put("opCode", rawMessage);

    boost::property_tree::write_json(json, root);
    return json.str();
    
}
std::string keyBoardTask::toJsonFollow(std::string rawMessage){
    ptree root;
    std::size_t index = rawMessage.find(' ');
    std::string editMessage = rawMessage.substr(index + 1);
    std::stringstream stream(editMessage);
    std::stringstream json;
    std::string followUnFollow;
    std::string userName;

    std::getline(stream ,followUnFollow , ' ');
    std::getline(stream ,userName , ' ');

    if(followUnFollow.compare("0") == 0){
        root.put("followUnfollow", "true");}
    else
        root.put("followUnfollow", "false");
    root.put("userName", userName);

    boost::property_tree::write_json(json, root);
    return json.str();
    
}
std::string keyBoardTask::toJsonPost(std::string rawMessage){
    ptree root;
    std::size_t index = rawMessage.find(' ');
    std::string editMessage = rawMessage.substr(index + 1);
    std::stringstream json;

    root.put("content", editMessage);

    boost::property_tree::write_json(json, root);
    return json.str();

}
std::string keyBoardTask::toJsonPM(std::string rawMessage){
    ptree root;
    std::size_t index = rawMessage.find(' ');
    std::string editMessage = rawMessage.substr(index + 1);
    index = editMessage.find(' ');
    std::string userName = editMessage.substr(0,index);
    std::string content = editMessage.substr(index+1);
    std::stringstream json;
    root.put("receiveUserName",userName);
    root.put("content",content);

    boost::property_tree::write_json(json, root);
    return json.str();

}
std::string keyBoardTask::toJsonLogStat(std::string rawMessage){
    ptree root;
    root.put("opCode", rawMessage);
    std::stringstream json;

    boost::property_tree::write_json(json, root);
    return json.str();
    
}
std::string keyBoardTask::toJsonStat(std::string rawMessage){
    std::size_t index = rawMessage.find(' ');
    std::string editMessage = rawMessage.substr(index + 1);
    std::stringstream json;
    std::stringstream namesStream(editMessage);
    ptree root;
    ptree users;
    ptree name;
    std::string currentName = "";
    std::string prevName = "";
    std::getline(namesStream, currentName, '|');

    do{
        name.put_value(currentName);
        users.push_back(std::make_pair("",name));
        prevName = currentName;
        std::getline(namesStream, currentName, '|');
    }
    while (currentName.compare(prevName) !=0);

    root.add_child("userNames", users);
    boost::property_tree::write_json(json, root);
    return json.str();
}
std::string keyBoardTask::toJsonBlock(std::string rawMessage){
    ptree root;
    std::size_t index = rawMessage.find(' ');
    std::string editMessage = rawMessage.substr(index + 1);
    std::stringstream json;

    root.put("userName", editMessage);

    boost::property_tree::write_json(json, root);
    return json.str();
    
}