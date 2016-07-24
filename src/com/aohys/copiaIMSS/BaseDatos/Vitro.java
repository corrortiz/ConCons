/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.BaseDatos;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.vibur.dbcp.ViburDBCPDataSource;

/**
 * @author Alejandro Ortiz Corro
 */

public class Vitro {
    private static final DataSource DATA_SOURCE;
    private static final String DRIVER   =  "com.mysql.jdbc.Driver";
    private static final String JDBC_URL =  "jdbc:mysql://localhost:3306/copia?&useSSL=false";
    Connection coneccion;
    
    static {
        ViburDBCPDataSource ds = new ViburDBCPDataSource();

        ds.setJdbcUrl(JDBC_URL);
        ds.setUsername("corrortiz");
        ds.setPassword("calixio2106");
        
        
        ds.setPoolInitialSize(10);
        ds.setPoolMaxSize(60);

        ds.setConnectionIdleLimitInSeconds(100);
        ds.setTestConnectionQuery("isValid");

        ds.setLogQueryExecutionLongerThanMs(500);
        ds.setLogStackTraceForLongQueryExecution(true);

        ds.setStatementCacheMaxSize(200);

        ds.start();
        
        DATA_SOURCE = ds; 
    }
    
    /**
     * Conecta a la base de datos
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
