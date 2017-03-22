package hrininlab.Interfaces;

import hrininlab.Messengers.Message;
import hrininlab.Messengers.SystemMessage;
import hrininlab.Messengers.SystemRequestMessage;

/**
 * Created by mrhri on 20.12.2016.
 */
public interface MessageListener {

    void addMessage(Message string);
    void addSystemMessage(SystemMessage message);
    void addSystemRequestMessage(SystemRequestMessage requestMessage);
}
