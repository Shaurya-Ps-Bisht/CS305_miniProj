package org.example;

import java.sql.Connection;
import java.sql.SQLException;

abstract class Person {
//    private string name;
    abstract public String login(Connection connection) throws SQLException;
}
