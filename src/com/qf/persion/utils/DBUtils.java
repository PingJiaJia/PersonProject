package com.qf.persion.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 连接数据库
 * 事务的控制
 * 释放资源
 */
public class DBUtils {
    private static ThreadLocal<Connection> threadLocal=new ThreadLocal<>();
    private static final Properties properties=new Properties();

    static{
        try {
            InputStream inputStream=DBUtils.class.getResourceAsStream("/db.properties");
            properties.load(inputStream);
            Class.forName(properties.getProperty("driver"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //开启事务
    public static void begin(){
        Connection connection=getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //提交事务
    public static void commit(){
        Connection connection=getConnection();
        try {
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //回滚事务
    public static void rollback(){
        Connection connection=null;
        try {
            connection=getConnection();
            connection.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }




    public static Connection getConnection(){
        Connection connection = threadLocal.get();
        try {
            if (connection==null){
                connection= DriverManager.getConnection(properties.getProperty("url"),properties.getProperty("username"),properties.getProperty("password"));
                threadLocal.set(connection);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public static void closeAll(Connection connection, Statement statement, ResultSet resultSet){
        try {
            if (resultSet!=null){
                resultSet.close();
            }
            if (statement!=null){
                statement.close();
            }
            if (connection!=null){
                connection.close();
                threadLocal.remove();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
