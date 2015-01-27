package fclib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import gov.va.med.lom.javaUtils.misc.JdbcConnection;
import gov.va.med.lom.javaUtils.misc.StringUtils;

// interface to a database based on documents in a RDBMS

public class DbmsSql extends AbstractDbmsInt {

  // DB connection info
  private static final String DB_URL = "jdbc:jtds:sqlserver://localhost:1433;DatabaseName=Clinical";
  private static final String DB_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
  private static final String DB_USER = "appserver";
  private static final String DB_PASS = "NxhM4jyEju";

  // Prepared statements
  private PreparedStatement getOwnerIdsPS;
  private PreparedStatement getOwnerIdForDocPS;
  private PreparedStatement getDocumentIdsForOwnerPS;
  private PreparedStatement getDocumentIdsPS;
  private PreparedStatement getTimestampPS; 
  private PreparedStatement getNoteTextPS; 
  private PreparedStatement getDocsForOwnersPS;
  
  // JDBC Connection
  private JdbcConnection jdbcConnection;
  
  // Constructor
  public DbmsSql() {
    
    try {
      jdbcConnection = new JdbcConnection(DB_URL, DB_DRIVER);
      jdbcConnection.connect(DB_USER, DB_PASS);
    } catch(Exception e) {
      System.err.println("Exception connecting to database: " + e.getMessage());
    }
    
    try {
      getOwnerIdsPS = jdbcConnection.getPreparedStatement("SELECT DISTINCT patientDfn FROM cfNote WHERE active=1");
      getOwnerIdForDocPS = jdbcConnection.getPreparedStatement("SELECT patientDfn FROM cfNote WHERE tiuNoteIen=?");
      getDocumentIdsPS = jdbcConnection.getPreparedStatement("SELECT DISTINCT tiuNoteIen FROM cfNote WHERE active=1");
      getDocumentIdsForOwnerPS = jdbcConnection.getPreparedStatement("SELECT tiuNoteIen FROM cfNote WHERE patientDfn=?");
      getTimestampPS = jdbcConnection.getPreparedStatement("SELECT refDateTime FROM cfNote WHERE tiuNoteIen=?");
      getNoteTextPS = jdbcConnection.getPreparedStatement("SELECT noteText FROM cfNote WHERE tiuNoteIen=?");
      getDocsForOwnersPS = jdbcConnection.getPreparedStatement("SELECT tiuNoteIen, noteText, refDateTime FROM cfNote WHERE patientDfn=?");
      
    } catch(Exception e) {
      System.err.println("Exception creating prepared statements: " + e.getMessage());
    }
  }
  
  
  // get a work list of owner IDs whose documents
  // need to be analyzed for copy/paste instances

  public ArrayList<String> getWorkOwnerIds() {
    
    Date start = new Date();
    
    ArrayList<String> out = new ArrayList<String>();
    
    try {
      ResultSet rs = getOwnerIdsPS.executeQuery();
      while (rs.next()) {
        out.add(rs.getString(1));
      }
    } catch(Exception e) {
      System.err.println("Exception getting owner IDs: " + e.getMessage());
    }
    
    return out;
    
  }

  // get a large list of random owner IDs for purposes
  // of analyzing a random sample of documents

  public ArrayList<String> getAllOwnerIds() {
   
    // return all owner ids for now
    return getWorkOwnerIds();
    
  }

  // get a list of document IDs for a specific owner

  public ArrayList<String> getDocumentIds(String ownerid) {

    ArrayList<String> out = new ArrayList<String>();
    
    try {
      getDocumentIdsForOwnerPS.setString(1, ownerid);
      ResultSet rs = getDocumentIdsForOwnerPS.executeQuery();
      while (rs.next()) {
        out.add(rs.getString(1));
      }
    } catch(Exception e) {
      System.err.println("Exception getting document IDs for owner  " + ownerid + ": " + e.getMessage());
    } 
    
    return out;  
    
  }
  
  // get a list of document IDs 

  public ArrayList<String> getDocumentIds() {

    Date start = new Date();
    
    ArrayList<String> out = new ArrayList<String>();
    
    try {
      ResultSet rs = getDocumentIdsPS.executeQuery();
      while (rs.next()) {
        out.add(rs.getString(1));
      }
    } catch(Exception e) {
      System.err.println("Exception getting document IDs: " + e.getMessage());
    } 
    
    return out;  
    
  }  

  // get the owner for a specific document

  public String getOwner(String docid) {

    try {
      getOwnerIdForDocPS.setString(1, docid);
      ResultSet rs = getOwnerIdForDocPS.executeQuery();
      if (rs.next()) {
        return rs.getString(1);
      }
    } catch(Exception e) {
      System.err.println("Exception getting owner for document "  + docid + ": " + e.getMessage());
    }    
    
    return null;
    
  }

  // get the text for a specific document

  public ArrayList<String> getLines(String docid) {

    ArrayList<String> out = new ArrayList<String>();
    
    try {
      getNoteTextPS.setString(1, docid);
      ResultSet rs = getNoteTextPS.executeQuery();
      if (rs.next()) {
        String s = rs.getString(1);
        String[] arr = StringUtils.pieceList(s, '\n');
        Collections.addAll(out, arr);
      }
    } catch(Exception e) {
      System.err.println("Exception getting lines for document "  + docid + ": " + e.getMessage());
    }
    
    return out;    
    
  }

  // get a timestamp for a specific document

  public Date getTimeStamp(String docid) {

    try {
      getTimestampPS.setString(1, docid);
      ResultSet rs = getTimestampPS.executeQuery();
      if (rs.next()) {
        return rs.getDate(1);
      }
    } catch(Exception e) {
      System.err.println("Exception getting timestamp for document " + docid + ": " + e.getMessage());
    }      
    
    return null;
    
  }
  
  public ArrayList<RawDoc> getAllForOwner(String ownerid) {
    
    ArrayList<RawDoc> out = new ArrayList<RawDoc>();
    
    try {
      getDocsForOwnersPS.setString(1, ownerid);
      ResultSet rs = getDocsForOwnersPS.executeQuery();
      while (rs.next()) {
        out.add(new RawDoc(rs.getString(1), rs.getString(2), rs.getDate(3)));
      }
    } catch(Exception e) {
      System.err.println("Exception getting documents for owner: " + e.getMessage());
    } 
    
    return out; 
    
  }
  
  public static void main(String[] args) {
    
    Date start = new Date();
    DbmsSql dbmsSql = new DbmsSql();
    ArrayList<String> docIds = dbmsSql.getDocumentIds();
    System.out.println("Retrieving text for " + docIds.size() + " notes");
    for (String docId : docIds) {
      dbmsSql.getLines(docId);
    }
    System.out.println(new Date().getTime() - start.getTime());
    
  }
  
  
}
