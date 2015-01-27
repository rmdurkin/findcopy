package fclib;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;

import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.lists.LocationsRpc;
import gov.va.med.lom.javaBroker.rpc.lists.models.Location;
import gov.va.med.lom.javaBroker.rpc.lists.models.LocationsList;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

import gov.va.med.lom.javaUtils.misc.JdbcConnection;

public class TiuNotesGetter {
  
  private static final int MAX_NOTES = 250;
  private static final Pattern cosignedDatePattern = Pattern.compile("^\\s{3}Cosigned Date:\\s{1}([\\w\\s@,:]+)Cosigned By.*$");
  private static final Pattern documentBodyPattern = Pattern.compile("^[\\s\\S]*STATUS: COMPLETED[\\s]*\n([\\s\\S]*?)/es/\\s.*[\\s\\S]*$");
  private static final String DB_CONNECTION = "jdbc:jtds:sqlserver://localhost;DatabaseName=Clinical";
  private static final String DB_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
  private static final String DB_USER = "appserver";
  private static final String DB_PASS = "NxhM4jyEju";
  private static final String DB_INSERT = "insert into cfNote (patientDfn,tiuNoteIen,isAddendum,refDateTime,noteTitle,noteText) values(?,?,?,?,?,?)";
  
  
  public static void main(String[] args) throws Exception {
    // If user didn't pass the patient's ssn and name of the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 1) {
      System.out.println("Usage: java FindCopyDriver AUTH_PROPS");
      System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      server = res.getString("server");
      port = Integer.valueOf(res.getString("port")).intValue();
      access = res.getString("accessCode");
      verify = res.getString("verifyCode");
    }
    try {
      // Call the static signon method and get an instance of the vista signon rpc
      RpcBroker rpcBroker = new RpcBroker();
      rpcBroker.connect(server, port);
      // Do the signon setup 
      VistaSignonSetupRpc vistaSignonSetupRpc = new VistaSignonSetupRpc(rpcBroker);
      vistaSignonSetupRpc.doVistaSignonSetup();
      // Do signon 
      System.out.println("Signing on to VistA: " + server + ":" + port);
      VistaSignonRpc vistaSignonRpc = new VistaSignonRpc(rpcBroker);
      vistaSignonRpc.setReturnRpcResult(true);
      vistaSignonRpc.setReturnXml(true);
      vistaSignonRpc.doVistaSignon(access, verify);
      // Get the vista signon result object
      VistaSignonResult vistaSignonResult = vistaSignonRpc.getVistaSignonResult();  
      if (vistaSignonResult.getSignonSucceeded()) {
        
        LocationsRpc locationsRpc = new LocationsRpc(rpcBroker);
        PatientSelectionRpc patientSelectionRpc = new PatientSelectionRpc(rpcBroker); 
        TiuNoteHeadersRpc tiuNoteHeadersRpc = new TiuNoteHeadersRpc(rpcBroker);
        TiuNoteRpc tiuNoteRpc = new TiuNoteRpc(rpcBroker);
        
        TiuNoteHeadersSelection tiuNoteHeadersSelection = new TiuNoteHeadersSelection();
        tiuNoteHeadersSelection.setNoteClass(TiuNoteHeadersRpc.CLASS_PROGRESS_NOTES);
        tiuNoteHeadersSelection.setNoteStatus(TiuNoteHeadersRpc.SIGNED_DOCUMENTS_ALL);
        tiuNoteHeadersSelection.setLimit(MAX_NOTES);
        
        System.out.println("Connecting to DB");
        JdbcConnection jdbcConnection = 
            new JdbcConnection(DB_CONNECTION, DB_DRIVER);
        jdbcConnection.connect(DB_USER, DB_PASS);
        PreparedStatement pstmt = jdbcConnection.getConnection().prepareStatement(DB_INSERT);
        
        LocationsList locationsList = locationsRpc.listAllWards();
        Location[] locations = locationsList.getLocations();
        System.out.println("# Wards = " + locations.length);
        
        for (int i = 0; i < locations.length; i++) {
          
          System.out.println("----- WARD: " + locations[i].getName() + " -----");
          PatientList patientList = null;
          PatientListItem[] patientListItems = null;
          patientList = patientSelectionRpc.listPtByWard(locations[i].getIen());
          patientListItems = patientList.getPatientListItems();
          System.out.println("    # Patients: " + patientListItems.length);
          for(int j = 0; j < patientListItems.length; j++) {
            System.out.println("    ----- " + patientListItems[j].getName() + " -----");
            TiuNoteHeadersList tiuNoteHeadersList = tiuNoteHeadersRpc.getTiuNoteHeaders(patientListItems[j].getDfn(), tiuNoteHeadersSelection);
            TiuNoteHeader[] tiuNoteHeaders = tiuNoteHeadersList.getTiuNoteHeaders();
            System.out.println("        # Notes: " + tiuNoteHeaders.length);
            
            int numNotes = (tiuNoteHeaders.length < MAX_NOTES) ? tiuNoteHeaders.length : MAX_NOTES;
            
            for (int k = 0; k < numNotes; k++) {
              
              TiuNote tiuNote = tiuNoteRpc.getTiuNote(Long.valueOf(tiuNoteHeaders[k].getIen()));
              
              Matcher m = null;
              String documentBody = null;
              if ((m = documentBodyPattern.matcher(tiuNote.getText())).matches())
                documentBody = m.group(1).trim();
              else 
                documentBody = tiuNote.getText();
              
              java.sql.Date refDate = null;
              if (tiuNoteHeaders[k].getReferenceDatetime() != null) {
                refDate = new java.sql.Date(tiuNoteHeaders[k].getReferenceDatetime().getTime());
              }
              
              try {
                pstmt.setString(1, String.valueOf(tiuNoteHeaders[k].getDfn()));
                pstmt.setString(2, String.valueOf(tiuNoteHeaders[k].getIen()));
                pstmt.setInt(3, tiuNoteHeaders[k].getTitle().startsWith("+") ? 1 : 0);
                pstmt.setDate(4, refDate, Calendar.getInstance());
                pstmt.setString(5, tiuNoteHeaders[k].getTitle());
                pstmt.setString(6, documentBody);
                jdbcConnection.doUpdate(pstmt);
              } catch(SQLException e) {
              }
              
            }
            
          }
          
        }
        
        
      } else {
        System.out.println(vistaSignonResult.getMessage());
      }    
      // Close the connection to the broker
      vistaSignonRpc.disconnect();
    } catch(BrokerException be) {
      System.err.println(be.getMessage());
      System.err.println("Action: " + be.getAction());
      System.err.println("Code: " + be.getCode());
      System.err.println("Mnemonic: " + be.getMnemonic());
    }      
  }

}