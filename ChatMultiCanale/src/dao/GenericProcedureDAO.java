package dao;

import java.sql.SQLException;
import exception.DAOException;
import exception.LoginException;

public interface GenericProcedureDAO<P> {
    P execute(Object... params) throws DAOException, SQLException, LoginException;
}
