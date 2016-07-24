/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo;

import com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta.Tratamiento;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Alejandro Ortiz Corro
 */

public class Laboratorial {
    private StringProperty id_lab;
    private BooleanProperty BIOMETRÍAHEMÁTICACOMPLETA ;
    private BooleanProperty FORMULAROJA ;
    private BooleanProperty COOMBS ;
    private BooleanProperty TIEMPODECOAGULACIONTPINRTPTTT ;
    private BooleanProperty GRUPOSANGUÍNEOYRH ;
    private BooleanProperty QUÍMICASANGUINEACOMPLETA ;
    private BooleanProperty QUÍMICASANGUINEA ;
    private BooleanProperty GLUCOSASERICA ;
    private BooleanProperty ACURICO ;
    private BooleanProperty COLESTEROL ;
    private BooleanProperty TRIGLICERIDOS ;
    private BooleanProperty PERFILDELÍPIDOS ;
    private BooleanProperty PRUEBASDEFUNCIONHEPÁTICA ;
    private BooleanProperty ELECTROLITOSSERICOS  ;
    private BooleanProperty ECARDIACAS ;
    private BooleanProperty OTRASENZIMASTROPONINAMIOGLOBINA ;
    private BooleanProperty GENERALDEORINA ;
    private BooleanProperty DEPURACIÓNDECREATININAORINA24HRS ;
    private BooleanProperty PERFILTIROIDE ;
    private BooleanProperty PAPANICOLAOU ;
    private BooleanProperty GONADROTOFINACORIONICAHUM ;
    private BooleanProperty PERFILHORMONAL ;
    private BooleanProperty ANTIGENOESPECIFICODEPRÓSTATA ;
    private BooleanProperty PROTCREACTIVA ;
    private BooleanProperty FACTORREUMATOIDE ;
    private BooleanProperty ANTIESTRESTOLISINAS ;
    private BooleanProperty REACIONESFEBRILES ;
    private BooleanProperty VIHELISAWesternBlot ;
    private BooleanProperty UROCULTIVO ;
    private BooleanProperty COPROCULTIVO ;
    private BooleanProperty COPRLÓGICO ;
    private BooleanProperty COPROPARASITOSCÓPICO ;
    private BooleanProperty CULTIVOFARINGEO ;
    private StringProperty OTROEXAMEN;
    private StringProperty OTROCULTIVO;
    private Date fecha_lab;
    private StringProperty id_paciente;

    
    Auxiliar aux = new Auxiliar();
    
    /**
     * constructor lleno
     * @param id_lab
     * @param BIOMETRÍAHEMÁTICACOMPLETA
     * @param FORMULAROJA
     * @param COOMBS
     * @param TIEMPODECOAGULACIONTPINRTPTTT
     * @param GRUPOSANGUÍNEOYRH
     * @param QUÍMICASANGUINEACOMPLETA
     * @param QUÍMICASANGUINEA
     * @param GLUCOSASERICA
     * @param ACURICO
     * @param COLESTEROL
     * @param TRIGLICERIDOS
     * @param PERFILDELÍPIDOS
     * @param PRUEBASDEFUNCIONHEPÁTICA
     * @param ELECTROLITOSSERICOS
     * @param ECARDIACAS
     * @param OTRASENZIMASTROPONINAMIOGLOBINA
     * @param GENERALDEORINA
     * @param DEPURACIÓNDECREATININAORINA24HRS
     * @param PERFILTIROIDE
     * @param PAPANICOLAOU
     * @param GONADROTOFINACORIONICAHUM
     * @param PERFILHORMONAL
     * @param ANTIGENOESPECIFICODEPRÓSTATA
     * @param PROTCREACTIVA
     * @param FACTORREUMATOIDE
     * @param ANTIESTRESTOLISINAS
     * @param REACIONESFEBRILES
     * @param VIHELISAWesternBlot
     * @param UROCULTIVO
     * @param COPROCULTIVO
     * @param COPRLÓGICO
     * @param COPROPARASITOSCÓPICO
     * @param CULTIVOFARINGEO
     * @param OTROEXAMEN
     * @param OTROCULTIVO
     * @param fecha_lab
     * @param id_paciente 
     */
    public Laboratorial(String id_lab, boolean BIOMETRÍAHEMÁTICACOMPLETA, 
            boolean FORMULAROJA, boolean COOMBS, boolean TIEMPODECOAGULACIONTPINRTPTTT, 
            boolean GRUPOSANGUÍNEOYRH, boolean QUÍMICASANGUINEACOMPLETA, 
            boolean QUÍMICASANGUINEA, boolean GLUCOSASERICA, boolean ACURICO, 
            boolean COLESTEROL, boolean TRIGLICERIDOS, boolean PERFILDELÍPIDOS, 
            boolean PRUEBASDEFUNCIONHEPÁTICA, boolean ELECTROLITOSSERICOS, boolean ECARDIACAS, 
            boolean OTRASENZIMASTROPONINAMIOGLOBINA, boolean GENERALDEORINA, 
            boolean DEPURACIÓNDECREATININAORINA24HRS, boolean PERFILTIROIDE, 
            boolean PAPANICOLAOU, boolean GONADROTOFINACORIONICAHUM, boolean PERFILHORMONAL, 
            boolean ANTIGENOESPECIFICODEPRÓSTATA, boolean PROTCREACTIVA, 
            boolean FACTORREUMATOIDE, boolean ANTIESTRESTOLISINAS, boolean REACIONESFEBRILES, 
            boolean VIHELISAWesternBlot, boolean UROCULTIVO, boolean COPROCULTIVO, 
            boolean COPRLÓGICO, boolean COPROPARASITOSCÓPICO, boolean CULTIVOFARINGEO, 
            String OTROEXAMEN, String OTROCULTIVO, Date fecha_lab, String id_paciente) {
        this.id_lab = new SimpleStringProperty(id_lab);
        this.BIOMETRÍAHEMÁTICACOMPLETA = new SimpleBooleanProperty(BIOMETRÍAHEMÁTICACOMPLETA);
        this.FORMULAROJA = new SimpleBooleanProperty(FORMULAROJA);
        this.COOMBS = new SimpleBooleanProperty(COOMBS);
        this.TIEMPODECOAGULACIONTPINRTPTTT = new SimpleBooleanProperty(TIEMPODECOAGULACIONTPINRTPTTT);
        this.GRUPOSANGUÍNEOYRH = new SimpleBooleanProperty(GRUPOSANGUÍNEOYRH);
        this.QUÍMICASANGUINEACOMPLETA = new SimpleBooleanProperty(QUÍMICASANGUINEACOMPLETA);
        this.QUÍMICASANGUINEA = new SimpleBooleanProperty(QUÍMICASANGUINEA);
        this.GLUCOSASERICA = new SimpleBooleanProperty(GLUCOSASERICA);
        this.ACURICO = new SimpleBooleanProperty(ACURICO);
        this.COLESTEROL = new SimpleBooleanProperty(COLESTEROL);
        this.TRIGLICERIDOS = new SimpleBooleanProperty(TRIGLICERIDOS);
        this.PERFILDELÍPIDOS = new SimpleBooleanProperty(PERFILDELÍPIDOS);
        this.PRUEBASDEFUNCIONHEPÁTICA = new SimpleBooleanProperty(PRUEBASDEFUNCIONHEPÁTICA);
        this.ELECTROLITOSSERICOS = new SimpleBooleanProperty(ELECTROLITOSSERICOS);
        this.ECARDIACAS = new SimpleBooleanProperty(ECARDIACAS);
        this.OTRASENZIMASTROPONINAMIOGLOBINA = new SimpleBooleanProperty(OTRASENZIMASTROPONINAMIOGLOBINA);
        this.GENERALDEORINA = new SimpleBooleanProperty(GENERALDEORINA);
        this.DEPURACIÓNDECREATININAORINA24HRS = new SimpleBooleanProperty(DEPURACIÓNDECREATININAORINA24HRS);
        this.PERFILTIROIDE = new SimpleBooleanProperty(PERFILTIROIDE);
        this.PAPANICOLAOU = new SimpleBooleanProperty(PAPANICOLAOU);
        this.GONADROTOFINACORIONICAHUM = new SimpleBooleanProperty(GONADROTOFINACORIONICAHUM);
        this.PERFILHORMONAL = new SimpleBooleanProperty(PERFILHORMONAL);
        this.ANTIGENOESPECIFICODEPRÓSTATA = new SimpleBooleanProperty(ANTIGENOESPECIFICODEPRÓSTATA);
        this.PROTCREACTIVA = new SimpleBooleanProperty(PROTCREACTIVA);
        this.FACTORREUMATOIDE = new SimpleBooleanProperty(FACTORREUMATOIDE);
        this.ANTIESTRESTOLISINAS = new SimpleBooleanProperty(ANTIESTRESTOLISINAS);
        this.REACIONESFEBRILES = new SimpleBooleanProperty(REACIONESFEBRILES);
        this.VIHELISAWesternBlot = new SimpleBooleanProperty(VIHELISAWesternBlot);
        this.UROCULTIVO = new SimpleBooleanProperty(UROCULTIVO);
        this.COPROCULTIVO = new SimpleBooleanProperty(COPROCULTIVO);
        this.COPRLÓGICO = new SimpleBooleanProperty(COPRLÓGICO);
        this.COPROPARASITOSCÓPICO = new SimpleBooleanProperty(COPROPARASITOSCÓPICO);
        this.CULTIVOFARINGEO = new SimpleBooleanProperty(CULTIVOFARINGEO);
        this.OTROEXAMEN = new SimpleStringProperty(OTROEXAMEN);
        this.OTROCULTIVO = new SimpleStringProperty(OTROCULTIVO);
        this.fecha_lab = fecha_lab;
        this.id_paciente = new SimpleStringProperty(id_paciente);
    }

    /**
     * constructor vacio
     */
    public Laboratorial() {
    }

    
    /**
     * guarda solicitud de laboratorio
     * @param id_lab
     * @param BIOMETRÍAHEMÁTICACOMPLETA
     * @param FORMULAROJA
     * @param COOMBS
     * @param TIEMPODECOAGULACIONTPINRTPTTT
     * @param GRUPOSANGUÍNEOYRH
     * @param QUÍMICASANGUINEACOMPLETA
     * @param QUÍMICASANGUINEA
     * @param GLUCOSASERICA
     * @param ACURICO
     * @param COLESTEROL
     * @param TRIGLICERIDOS
     * @param PERFILDELÍPIDOS
     * @param PRUEBASDEFUNCIONHEPÁTICA
     * @param ELECTROLITOSSERICOS
     * @param ECARDIACAS
     * @param OTRASENZIMASTROPONINAMIOGLOBINA
     * @param GENERALDEORINA
     * @param DEPURACIÓNDECREATININAORINA24HRS
     * @param PERFILTIROIDE
     * @param PAPANICOLAOU
     * @param GONADROTOFINACORIONICAHUM
     * @param PERFILHORMONAL
     * @param ANTIGENOESPECIFICODEPRÓSTATA
     * @param PROTCREACTIVA
     * @param FACTORREUMATOIDE
     * @param ANTIESTRESTOLISINAS
     * @param REACIONESFEBRILES
     * @param VIHELISAWesternBlot
     * @param UROCULTIVO
     * @param COPROCULTIVO
     * @param COPRLÓGICO
     * @param COPROPARASITOSCÓPICO
     * @param CULTIVOFARINGEO
     * @param OTROEXAMEN
     * @param OTROCULTIVO
     * @param fecha_lab
     * @param id_paciente
     * @param conex 
     */
    public void agregaLaboratorial(String id_lab, boolean BIOMETRÍAHEMÁTICACOMPLETA, 
            boolean FORMULAROJA, boolean COOMBS, boolean TIEMPODECOAGULACIONTPINRTPTTT, 
            boolean GRUPOSANGUÍNEOYRH, boolean QUÍMICASANGUINEACOMPLETA, 
            boolean QUÍMICASANGUINEA, boolean GLUCOSASERICA, boolean ACURICO, 
            boolean COLESTEROL, boolean TRIGLICERIDOS, boolean PERFILDELÍPIDOS, 
            boolean PRUEBASDEFUNCIONHEPÁTICA, boolean ELECTROLITOSSERICOS, boolean ECARDIACAS, 
            boolean OTRASENZIMASTROPONINAMIOGLOBINA, boolean GENERALDEORINA, 
            boolean DEPURACIÓNDECREATININAORINA24HRS, boolean PERFILTIROIDE, 
            boolean PAPANICOLAOU, boolean GONADROTOFINACORIONICAHUM, boolean PERFILHORMONAL, 
            boolean ANTIGENOESPECIFICODEPRÓSTATA, boolean PROTCREACTIVA, 
            boolean FACTORREUMATOIDE, boolean ANTIESTRESTOLISINAS, boolean REACIONESFEBRILES, 
            boolean VIHELISAWesternBlot, boolean UROCULTIVO, boolean COPROCULTIVO, 
            boolean COPRLÓGICO, boolean COPROPARASITOSCÓPICO, boolean CULTIVOFARINGEO, 
            String OTROEXAMEN, String OTROCULTIVO, Date fecha_lab, String id_paciente, Connection conex){
        String sqlst =  "INSERT INTO `laboratorial`\n" +
                                "(`id_lab`,\n" +
                                "`BIOMETRÍAHEMÁTICACOMPLETA`,\n" +
                                "`FORMULAROJA`,\n" +
                                "`COOMBS`,\n" +
                                "`TIEMPODECOAGULACIONTPINRTPTTT`,\n" +
                                "`GRUPOSANGUÍNEOYRH`,\n" +
                                "`QUÍMICASANGUINEACOMPLETA`,\n" +
                                "`QUÍMICASANGUINEA`,\n" +
                                "`GLUCOSASERICA`,\n" +
                                "`ACURICO`,\n" +
                                "`COLESTEROL`,\n" +
                                "`TRIGLICERIDOS`,\n" +
                                "`PERFILDELÍPIDOS`,\n" +
                                "`PRUEBASDEFUNCIONHEPÁTICA`,\n" +
                                "`ELECTROLITOSSERICOS`,\n" +
                                "`ECARDIACAS`,\n" +
                                "`OTRASENZIMASTROPONINAMIOGLOBINA`,\n" +
                                "`GENERALDEORINA`,\n" +
                                "`DEPURACIÓNDECREATININAORINA24HRS`,\n" +
                                "`PERFILTIROIDE`,\n" +
                                "`PAPANICOLAOU`,\n" +
                                "`GONADROTOFINACORIONICAHUM`,\n" +
                                "`PERFILHORMONAL`,\n" +
                                "`ANTIGENOESPECIFICODEPRÓSTATA`,\n" +
                                "`PROTCREACTIVA`,\n" +
                                "`FACTORREUMATOIDE`,\n" +
                                "`ANTIESTRESTOLISINAS`,\n" +
                                "`REACIONESFEBRILES`,\n" +
                                "`VIHELISAWesternBlot`,\n" +
                                "`UROCULTIVO`,\n" +
                                "`COPROCULTIVO`,\n" +
                                "`COPRLÓGICO`,\n" +
                                "`COPROPARASITOSCÓPICO`,\n" +
                                "`CULTIVOFARINGEO`,\n" +
                                "`OTROEXAMEN`,\n" +
                                "`OTROCULTIVO`,\n" +
                                "`fecha_lab`,\n" +
                                "`id_paciente`)"+
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString(1,id_lab);
            sttm.setBoolean(2,BIOMETRÍAHEMÁTICACOMPLETA);
            sttm.setBoolean(3,FORMULAROJA);
            sttm.setBoolean(4,COOMBS);
            sttm.setBoolean(5,TIEMPODECOAGULACIONTPINRTPTTT);
            sttm.setBoolean(6,GRUPOSANGUÍNEOYRH);
            sttm.setBoolean(7,QUÍMICASANGUINEACOMPLETA);
            sttm.setBoolean(8,QUÍMICASANGUINEA);
            sttm.setBoolean(9,GLUCOSASERICA);
            sttm.setBoolean(10,ACURICO);
            sttm.setBoolean(11,COLESTEROL);
            sttm.setBoolean(12,TRIGLICERIDOS);
            sttm.setBoolean(13,PERFILDELÍPIDOS);
            sttm.setBoolean(14,PRUEBASDEFUNCIONHEPÁTICA);
            sttm.setBoolean(15,ELECTROLITOSSERICOS);
            sttm.setBoolean(16,ECARDIACAS);
            sttm.setBoolean(17,OTRASENZIMASTROPONINAMIOGLOBINA);
            sttm.setBoolean(18,GENERALDEORINA);
            sttm.setBoolean(19,DEPURACIÓNDECREATININAORINA24HRS);
            sttm.setBoolean(20,PERFILTIROIDE);
            sttm.setBoolean(21,PAPANICOLAOU);
            sttm.setBoolean(22,GONADROTOFINACORIONICAHUM);
            sttm.setBoolean(23,PERFILHORMONAL);
            sttm.setBoolean(24,ANTIGENOESPECIFICODEPRÓSTATA);
            sttm.setBoolean(25,PROTCREACTIVA);
            sttm.setBoolean(26,FACTORREUMATOIDE);
            sttm.setBoolean(27,ANTIESTRESTOLISINAS);
            sttm.setBoolean(28,REACIONESFEBRILES);
            sttm.setBoolean(29,VIHELISAWesternBlot);
            sttm.setBoolean(30,UROCULTIVO);
            sttm.setBoolean(31,COPROCULTIVO);
            sttm.setBoolean(32,COPRLÓGICO);
            sttm.setBoolean(33,COPROPARASITOSCÓPICO);
            sttm.setBoolean(34,CULTIVOFARINGEO);
            sttm.setString(35,OTROEXAMEN);
            sttm.setString(36,OTROCULTIVO);
            sttm.setDate(37,fecha_lab);
            sttm.setString(38,id_paciente);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("La solicitud de laboratorio se ha guardado", 
                    "La solicitud de laboratorio se ha guardado", 
                    "La solicitud de laboratorio ha sido agregada a la base de datos exitosamente");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public Laboratorial cargaSoloUno(String idLabo, Connection conex){
        Laboratorial cit = null;
        String sttm = "SELECT `id_lab`,\n" +
                                "`BIOMETRÍAHEMÁTICACOMPLETA`,\n" +
                                "`FORMULAROJA`,\n" +
                                "`COOMBS`,\n" +
                                "`TIEMPODECOAGULACIONTPINRTPTTT`,\n" +
                                "`GRUPOSANGUÍNEOYRH`,\n" +
                                "`QUÍMICASANGUINEACOMPLETA`,\n" +
                                "`QUÍMICASANGUINEA`,\n" +
                                "`GLUCOSASERICA`,\n" +
                                "`ACURICO`,\n" +
                                "`COLESTEROL`,\n" +
                                "`TRIGLICERIDOS`,\n" +
                                "`PERFILDELÍPIDOS`,\n" +
                                "`PRUEBASDEFUNCIONHEPÁTICA`,\n" +
                                "`ELECTROLITOSSERICOS`,\n" +
                                "`ECARDIACAS`,\n" +
                                "`OTRASENZIMASTROPONINAMIOGLOBINA`,\n" +
                                "`GENERALDEORINA`,\n" +
                                "`DEPURACIÓNDECREATININAORINA24HRS`,\n" +
                                "`PERFILTIROIDE`,\n" +
                                "`PAPANICOLAOU`,\n" +
                                "`GONADROTOFINACORIONICAHUM`,\n" +
                                "`PERFILHORMONAL`,\n" +
                                "`ANTIGENOESPECIFICODEPRÓSTATA`,\n" +
                                "`PROTCREACTIVA`,\n" +
                                "`FACTORREUMATOIDE`,\n" +
                                "`ANTIESTRESTOLISINAS`,\n" +
                                "`REACIONESFEBRILES`,\n" +
                                "`VIHELISAWesternBlot`,\n" +
                                "`UROCULTIVO`,\n" +
                                "`COPROCULTIVO`,\n" +
                                "`COPRLÓGICO`,\n" +
                                "`COPROPARASITOSCÓPICO`,\n" +
                                "`CULTIVOFARINGEO`,\n" +
                                "`OTROEXAMEN`,\n" +
                                "`OTROCULTIVO`,\n" +
                                "`fecha_lab`,\n" +
                                "`id_paciente`"+
                      "FROM laboratorial WHERE id_lab = '"+idLabo+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                cit = new Laboratorial( res.getString("id_lab"),
                                        res.getBoolean("BIOMETRÍAHEMÁTICACOMPLETA"),
                                        res.getBoolean("FORMULAROJA"),
                                        res.getBoolean("COOMBS"),
                                        res.getBoolean("TIEMPODECOAGULACIONTPINRTPTTT"),
                                        res.getBoolean("GRUPOSANGUÍNEOYRH"),
                                        res.getBoolean("QUÍMICASANGUINEACOMPLETA"),
                                        res.getBoolean("QUÍMICASANGUINEA"),
                                        res.getBoolean("GLUCOSASERICA"),
                                        res.getBoolean("ACURICO"),
                                        res.getBoolean("COLESTEROL"),
                                        res.getBoolean("TRIGLICERIDOS"),
                                        res.getBoolean("PERFILDELÍPIDOS"),
                                        res.getBoolean("PRUEBASDEFUNCIONHEPÁTICA"),
                                        res.getBoolean("ELECTROLITOSSERICOS"),
                                        res.getBoolean("ECARDIACAS"),
                                        res.getBoolean("OTRASENZIMASTROPONINAMIOGLOBINA"),
                                        res.getBoolean("GENERALDEORINA"),
                                        res.getBoolean("DEPURACIÓNDECREATININAORINA24HRS"),
                                        res.getBoolean("PERFILTIROIDE"),
                                        res.getBoolean("PAPANICOLAOU"),
                                        res.getBoolean("GONADROTOFINACORIONICAHUM"),
                                        res.getBoolean("PERFILHORMONAL"),
                                        res.getBoolean("ANTIGENOESPECIFICODEPRÓSTATA"),
                                        res.getBoolean("PROTCREACTIVA"),
                                        res.getBoolean("FACTORREUMATOIDE"),
                                        res.getBoolean("ANTIESTRESTOLISINAS"),
                                        res.getBoolean("REACIONESFEBRILES"),
                                        res.getBoolean("VIHELISAWesternBlot"),
                                        res.getBoolean("UROCULTIVO"),
                                        res.getBoolean("COPROCULTIVO"),
                                        res.getBoolean("COPRLÓGICO"),
                                        res.getBoolean("COPROPARASITOSCÓPICO"),
                                        res.getBoolean("CULTIVOFARINGEO"),
                                        res.getString("OTROEXAMEN"),
                                        res.getString("OTROCULTIVO"),
                                        res.getDate  ("fecha_lab"),
                                        res.getString("id_paciente"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cit;
    }    

    /**
     * crea el laboratorial 
     * @param conex
     * @param idPaciente
     * @param dia
     * @return 
     */
    public ObservableList<Laboratorial> listaLaboratorialDia(Connection conex, String idPaciente, Date dia){
        ObservableList<Laboratorial> listaLaboratorial = FXCollections.observableArrayList();
        String sql = "SELECT `id_lab`,\n" +
                        "`BIOMETRÍAHEMÁTICACOMPLETA`,\n" +
                        "`FORMULAROJA`,\n" +
                        "`COOMBS`,\n" +
                        "`TIEMPODECOAGULACIONTPINRTPTTT`,\n" +
                        "`GRUPOSANGUÍNEOYRH`,\n" +
                        "`QUÍMICASANGUINEACOMPLETA`,\n" +
                        "`QUÍMICASANGUINEA`,\n" +
                        "`GLUCOSASERICA`,\n" +
                        "`ACURICO`,\n" +
                        "`COLESTEROL`,\n" +
                        "`TRIGLICERIDOS`,\n" +
                        "`PERFILDELÍPIDOS`,\n" +
                        "`PRUEBASDEFUNCIONHEPÁTICA`,\n" +
                        "`ELECTROLITOSSERICOS`,\n" +
                        "`ECARDIACAS`,\n" +
                        "`OTRASENZIMASTROPONINAMIOGLOBINA`,\n" +
                        "`GENERALDEORINA`,\n" +
                        "`DEPURACIÓNDECREATININAORINA24HRS`,\n" +
                        "`PERFILTIROIDE`,\n" +
                        "`PAPANICOLAOU`,\n" +
                        "`GONADROTOFINACORIONICAHUM`,\n" +
                        "`PERFILHORMONAL`,\n" +
                        "`ANTIGENOESPECIFICODEPRÓSTATA`,\n" +
                        "`PROTCREACTIVA`,\n" +
                        "`FACTORREUMATOIDE`,\n" +
                        "`ANTIESTRESTOLISINAS`,\n" +
                        "`REACIONESFEBRILES`,\n" +
                        "`VIHELISAWesternBlot`,\n" +
                        "`UROCULTIVO`,\n" +
                        "`COPROCULTIVO`,\n" +
                        "`COPRLÓGICO`,\n" +
                        "`COPROPARASITOSCÓPICO`,\n" +
                        "`CULTIVOFARINGEO`,\n" +
                        "`OTROEXAMEN`,\n" +
                        "`OTROCULTIVO`,\n" +
                        "`fecha_lab`,\n" +
                        "`id_paciente`"+
                      "FROM laboratorial WHERE id_paciente = '"+idPaciente+"'"
                +     "AND fecha_lab = '"+dia+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaLaboratorial.add(new Laboratorial( res.getString("id_lab"),
                                        res.getBoolean("BIOMETRÍAHEMÁTICACOMPLETA"),
                                        res.getBoolean("FORMULAROJA"),
                                        res.getBoolean("COOMBS"),
                                        res.getBoolean("TIEMPODECOAGULACIONTPINRTPTTT"),
                                        res.getBoolean("GRUPOSANGUÍNEOYRH"),
                                        res.getBoolean("QUÍMICASANGUINEACOMPLETA"),
                                        res.getBoolean("QUÍMICASANGUINEA"),
                                        res.getBoolean("GLUCOSASERICA"),
                                        res.getBoolean("ACURICO"),
                                        res.getBoolean("COLESTEROL"),
                                        res.getBoolean("TRIGLICERIDOS"),
                                        res.getBoolean("PERFILDELÍPIDOS"),
                                        res.getBoolean("PRUEBASDEFUNCIONHEPÁTICA"),
                                        res.getBoolean("ELECTROLITOSSERICOS"),
                                        res.getBoolean("ECARDIACAS"),
                                        res.getBoolean("OTRASENZIMASTROPONINAMIOGLOBINA"),
                                        res.getBoolean("GENERALDEORINA"),
                                        res.getBoolean("DEPURACIÓNDECREATININAORINA24HRS"),
                                        res.getBoolean("PERFILTIROIDE"),
                                        res.getBoolean("PAPANICOLAOU"),
                                        res.getBoolean("GONADROTOFINACORIONICAHUM"),
                                        res.getBoolean("PERFILHORMONAL"),
                                        res.getBoolean("ANTIGENOESPECIFICODEPRÓSTATA"),
                                        res.getBoolean("PROTCREACTIVA"),
                                        res.getBoolean("FACTORREUMATOIDE"),
                                        res.getBoolean("ANTIESTRESTOLISINAS"),
                                        res.getBoolean("REACIONESFEBRILES"),
                                        res.getBoolean("VIHELISAWesternBlot"),
                                        res.getBoolean("UROCULTIVO"),
                                        res.getBoolean("COPROCULTIVO"),
                                        res.getBoolean("COPRLÓGICO"),
                                        res.getBoolean("COPROPARASITOSCÓPICO"),
                                        res.getBoolean("CULTIVOFARINGEO"),
                                        res.getString("OTROEXAMEN"),
                                        res.getString("OTROCULTIVO"),
                                        res.getDate  ("fecha_lab"),
                                        res.getString("id_paciente")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaLaboratorial;
    }
    
    
    
    
    
    
    
    
    
    
    
    
/**********************************************************************************/
    //Setters and Getters
    public final void setId_lab(String value) {
        id_lab.set(value);
    }

    public final String getId_lab() {
        return id_lab.get();
    }

    public final StringProperty id_labProperty() {
        return id_lab;
    }

    public final void setBIOMETRÍAHEMÁTICACOMPLETA(Boolean value) {
        BIOMETRÍAHEMÁTICACOMPLETA.set(value);
    }

    public final Boolean getBIOMETRÍAHEMÁTICACOMPLETA() {
        return BIOMETRÍAHEMÁTICACOMPLETA.get();
    }

    public final BooleanProperty BIOMETRÍAHEMÁTICACOMPLETAProperty() {
        return BIOMETRÍAHEMÁTICACOMPLETA;
    }

    public final void setFORMULAROJA(Boolean value) {
        FORMULAROJA.set(value);
    }

    public final Boolean getFORMULAROJA() {
        return FORMULAROJA.get();
    }

    public final BooleanProperty FORMULAROJAProperty() {
        return FORMULAROJA;
    }

    public final void setCOOMBS(Boolean value) {
        COOMBS.set(value);
    }

    public final Boolean getCOOMBS() {
        return COOMBS.get();
    }

    public final BooleanProperty COOMBSProperty() {
        return COOMBS;
    }

    public final void setTIEMPODECOAGULACIONTPINRTPTTT(Boolean value) {
        TIEMPODECOAGULACIONTPINRTPTTT.set(value);
    }

    public final Boolean getTIEMPODECOAGULACIONTPINRTPTTT() {
        return TIEMPODECOAGULACIONTPINRTPTTT.get();
    }

    public final BooleanProperty TIEMPODECOAGULACIONTPINRTPTTTProperty() {
        return TIEMPODECOAGULACIONTPINRTPTTT;
    }

    public final void setGRUPOSANGUÍNEOYRH(Boolean value) {
        GRUPOSANGUÍNEOYRH.set(value);
    }

    public final Boolean getGRUPOSANGUÍNEOYRH() {
        return GRUPOSANGUÍNEOYRH.get();
    }

    public final BooleanProperty GRUPOSANGUÍNEOYRHProperty() {
        return GRUPOSANGUÍNEOYRH;
    }

    public final void setQUÍMICASANGUINEACOMPLETA(Boolean value) {
        QUÍMICASANGUINEACOMPLETA.set(value);
    }

    public final Boolean getQUÍMICASANGUINEACOMPLETA() {
        return QUÍMICASANGUINEACOMPLETA.get();
    }

    public final BooleanProperty QUÍMICASANGUINEACOMPLETAProperty() {
        return QUÍMICASANGUINEACOMPLETA;
    }

    public final void setQUÍMICASANGUINEA(Boolean value) {
        QUÍMICASANGUINEA.set(value);
    }

    public final Boolean getQUÍMICASANGUINEA() {
        return QUÍMICASANGUINEA.get();
    }

    public final BooleanProperty QUÍMICASANGUINEAProperty() {
        return QUÍMICASANGUINEA;
    }

    public final void setGLUCOSASERICA(Boolean value) {
        GLUCOSASERICA.set(value);
    }

    public final Boolean getGLUCOSASERICA() {
        return GLUCOSASERICA.get();
    }

    public final BooleanProperty GLUCOSASERICAProperty() {
        return GLUCOSASERICA;
    }

    public final void setACURICO(Boolean value) {
        ACURICO.set(value);
    }

    public final Boolean getACURICO() {
        return ACURICO.get();
    }

    public final BooleanProperty ACURICOProperty() {
        return ACURICO;
    }

    public final void setCOLESTEROL(Boolean value) {
        COLESTEROL.set(value);
    }

    public final Boolean getCOLESTEROL() {
        return COLESTEROL.get();
    }

    public final BooleanProperty COLESTEROLProperty() {
        return COLESTEROL;
    }

    public final void setTRIGLICERIDOS(Boolean value) {
        TRIGLICERIDOS.set(value);
    }

    public final Boolean getTRIGLICERIDOS() {
        return TRIGLICERIDOS.get();
    }

    public final BooleanProperty TRIGLICERIDOSProperty() {
        return TRIGLICERIDOS;
    }

    public final void setPERFILDELÍPIDOS(Boolean value) {
        PERFILDELÍPIDOS.set(value);
    }

    public final Boolean getPERFILDELÍPIDOS() {
        return PERFILDELÍPIDOS.get();
    }

    public final BooleanProperty PERFILDELÍPIDOSProperty() {
        return PERFILDELÍPIDOS;
    }

    public final void setPRUEBASDEFUNCIONHEPÁTICA(Boolean value) {
        PRUEBASDEFUNCIONHEPÁTICA.set(value);
    }

    public final Boolean getPRUEBASDEFUNCIONHEPÁTICA() {
        return PRUEBASDEFUNCIONHEPÁTICA.get();
    }

    public final BooleanProperty PRUEBASDEFUNCIONHEPÁTICAProperty() {
        return PRUEBASDEFUNCIONHEPÁTICA;
    }

    public final void setELECTROLITOSSERICOS(Boolean value) {
        ELECTROLITOSSERICOS.set(value);
    }

    public final Boolean getELECTROLITOSSERICOS() {
        return ELECTROLITOSSERICOS.get();
    }

    public final BooleanProperty ELECTROLITOSSERICOSProperty() {
        return ELECTROLITOSSERICOS;
    }

    public final void setECARDIACAS(Boolean value) {
        ECARDIACAS.set(value);
    }

    public final Boolean getECARDIACAS() {
        return ECARDIACAS.get();
    }

    public final BooleanProperty ECARDIACASProperty() {
        return ECARDIACAS;
    }

    public final void setOTRASENZIMASTROPONINAMIOGLOBINA(Boolean value) {
        OTRASENZIMASTROPONINAMIOGLOBINA.set(value);
    }

    public final Boolean getOTRASENZIMASTROPONINAMIOGLOBINA() {
        return OTRASENZIMASTROPONINAMIOGLOBINA.get();
    }

    public final BooleanProperty OTRASENZIMASTROPONINAMIOGLOBINAProperty() {
        return OTRASENZIMASTROPONINAMIOGLOBINA;
    }

    public final void setGENERALDEORINA(Boolean value) {
        GENERALDEORINA.set(value);
    }

    public final Boolean getGENERALDEORINA() {
        return GENERALDEORINA.get();
    }

    public final BooleanProperty GENERALDEORINAProperty() {
        return GENERALDEORINA;
    }

    public final void setDEPURACIÓNDECREATININAORINA24HRS(Boolean value) {
        DEPURACIÓNDECREATININAORINA24HRS.set(value);
    }

    public final Boolean getDEPURACIÓNDECREATININAORINA24HRS() {
        return DEPURACIÓNDECREATININAORINA24HRS.get();
    }

    public final BooleanProperty DEPURACIÓNDECREATININAORINA24HRSProperty() {
        return DEPURACIÓNDECREATININAORINA24HRS;
    }

    public final void setPERFILTIROIDE(Boolean value) {
        PERFILTIROIDE.set(value);
    }

    public final Boolean getPERFILTIROIDE() {
        return PERFILTIROIDE.get();
    }

    public final BooleanProperty PERFILTIROIDEProperty() {
        return PERFILTIROIDE;
    }

    public final void setPAPANICOLAOU(Boolean value) {
        PAPANICOLAOU.set(value);
    }

    public final Boolean getPAPANICOLAOU() {
        return PAPANICOLAOU.get();
    }

    public final BooleanProperty PAPANICOLAOUProperty() {
        return PAPANICOLAOU;
    }

    public final void setGONADROTOFINACORIONICAHUM(Boolean value) {
        GONADROTOFINACORIONICAHUM.set(value);
    }

    public final Boolean getGONADROTOFINACORIONICAHUM() {
        return GONADROTOFINACORIONICAHUM.get();
    }

    public final BooleanProperty GONADROTOFINACORIONICAHUMProperty() {
        return GONADROTOFINACORIONICAHUM;
    }

    public final void setPERFILHORMONAL(Boolean value) {
        PERFILHORMONAL.set(value);
    }

    public final Boolean getPERFILHORMONAL() {
        return PERFILHORMONAL.get();
    }

    public final BooleanProperty PERFILHORMONALProperty() {
        return PERFILHORMONAL;
    }

    public final void setANTIGENOESPECIFICODEPRÓSTATA(Boolean value) {
        ANTIGENOESPECIFICODEPRÓSTATA.set(value);
    }

    public final Boolean getANTIGENOESPECIFICODEPRÓSTATA() {
        return ANTIGENOESPECIFICODEPRÓSTATA.get();
    }

    public final BooleanProperty ANTIGENOESPECIFICODEPRÓSTATAProperty() {
        return ANTIGENOESPECIFICODEPRÓSTATA;
    }

    public final void setPROTCREACTIVA(Boolean value) {
        PROTCREACTIVA.set(value);
    }

    public final Boolean getPROTCREACTIVA() {
        return PROTCREACTIVA.get();
    }

    public final BooleanProperty PROTCREACTIVAProperty() {
        return PROTCREACTIVA;
    }

    public final void setFACTORREUMATOIDE(Boolean value) {
        FACTORREUMATOIDE.set(value);
    }

    public final Boolean getFACTORREUMATOIDE() {
        return FACTORREUMATOIDE.get();
    }

    public final BooleanProperty FACTORREUMATOIDEProperty() {
        return FACTORREUMATOIDE;
    }

    public final void setANTIESTRESTOLISINAS(Boolean value) {
        ANTIESTRESTOLISINAS.set(value);
    }

    public final Boolean getANTIESTRESTOLISINAS() {
        return ANTIESTRESTOLISINAS.get();
    }

    public final BooleanProperty ANTIESTRESTOLISINASProperty() {
        return ANTIESTRESTOLISINAS;
    }

    public final void setREACIONESFEBRILES(Boolean value) {
        REACIONESFEBRILES.set(value);
    }

    public final Boolean getREACIONESFEBRILES() {
        return REACIONESFEBRILES.get();
    }

    public final BooleanProperty REACIONESFEBRILESProperty() {
        return REACIONESFEBRILES;
    }

    public final void setVIHELISAWesternBlot(Boolean value) {
        VIHELISAWesternBlot.set(value);
    }

    public final Boolean getVIHELISAWesternBlot() {
        return VIHELISAWesternBlot.get();
    }

    public final BooleanProperty VIHELISAWesternBlotProperty() {
        return VIHELISAWesternBlot;
    }

    public final void setUROCULTIVO(Boolean value) {
        UROCULTIVO.set(value);
    }

    public final Boolean getUROCULTIVO() {
        return UROCULTIVO.get();
    }

    public final BooleanProperty UROCULTIVOProperty() {
        return UROCULTIVO;
    }

    public final void setCOPROCULTIVO(Boolean value) {
        COPROCULTIVO.set(value);
    }

    public final Boolean getCOPROCULTIVO() {
        return COPROCULTIVO.get();
    }

    public final BooleanProperty COPROCULTIVOProperty() {
        return COPROCULTIVO;
    }

    public final void setCOPRLÓGICO(Boolean value) {
        COPRLÓGICO.set(value);
    }

    public final Boolean getCOPRLÓGICO() {
        return COPRLÓGICO.get();
    }

    public final BooleanProperty COPRLÓGICOProperty() {
        return COPRLÓGICO;
    }

    public final void setCOPROPARASITOSCÓPICO(Boolean value) {
        COPROPARASITOSCÓPICO.set(value);
    }

    public final Boolean getCOPROPARASITOSCÓPICO() {
        return COPROPARASITOSCÓPICO.get();
    }

    public final BooleanProperty COPROPARASITOSCÓPICOProperty() {
        return COPROPARASITOSCÓPICO;
    }

    public final void setCULTIVOFARINGEO(Boolean value) {
        CULTIVOFARINGEO.set(value);
    }

    public final Boolean getCULTIVOFARINGEO() {
        return CULTIVOFARINGEO.get();
    }

    public final BooleanProperty CULTIVOFARINGEOProperty() {
        return CULTIVOFARINGEO;
    }

    public final void setOTROEXAMEN(String value) {
        OTROEXAMEN.set(value);
    }

    public final String getOTROEXAMEN() {
        return OTROEXAMEN.get();
    }

    public final StringProperty OTROEXAMENProperty() {
        return OTROEXAMEN;
    }

    public final void setOTROCULTIVO(String value) {
        OTROCULTIVO.set(value);
    }

    public final String getOTROCULTIVO() {
        return OTROCULTIVO.get();
    }

    public final StringProperty OTROCULTIVOProperty() {
        return OTROCULTIVO;
    }

    public final void setId_paciente(String value) {
        id_paciente.set(value);
    }

    public final String getId_paciente() {
        return id_paciente.get();
    }

    public final StringProperty id_pacienteProperty() {
        return id_paciente;
    }

    public Date getFecha_lab() {
        return fecha_lab;
    }

    public void setFecha_lab(Date fecha_lab) {
        this.fecha_lab = fecha_lab;
    }
    
    
    
    
    

}
