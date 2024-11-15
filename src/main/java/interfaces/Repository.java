package interfaces;

import java.io.IOException;

public interface Repository extends Connectable{
    String getMessageHistory();
    void saveMessageHistory(String log) throws IOException;
}
