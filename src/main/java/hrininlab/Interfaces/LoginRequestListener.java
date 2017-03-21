package hrininlab.Interfaces;

import java.sql.SQLException;

/**
 * Created by mrhri on 20.03.2017.
 */
public interface LoginRequestListener {

    void addLoginRequest(LoginRequest request) throws SQLException;
}
