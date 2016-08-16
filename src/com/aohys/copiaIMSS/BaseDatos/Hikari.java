/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.BaseDatos;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * @author Alejandro Ortiz Corro
 */

public class Hikari {
    private static final DataSource DATA_SOURCE;
    private static final String DRIVER   =  "com.mysql.jdbc.Driver";
    private static final String JDBC_URL =  "jdbc:mysql://copia-cluster.cluster-cyxilzamih2q.us-east-1.rds.amazonaws.com:3306/copia?&useSSL=false";
    Connection coneccion;
    
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername("corrortiz");
        config.setPassword("calixio2106");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HikariDataSource ds = new HikariDataSource(config);
        DATA_SOURCE = ds;
    }
    
    /**
     * Method to ask for connectio to de pool 
     * @return 
     */
    public Connection conectarBD(){
        try {
            this.coneccion = DATA_SOURCE.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
       return coneccion;
    }
}
