package hrininlab.Interfaces;

/**
 * Created by mrhri on 20.12.2016.
 */
public interface MessageListener {

    void addMessage(Message string);
    void addSystemMessage(SystemMessage message);
    void addSystemRequestMessage(SystemRequestMessage requestMessage);
}