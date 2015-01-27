package fclib;

import java.util.*;
import java.sql.*;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.javaUtils.misc.JdbcConnection;

public class TiuNotesStats {
  
  private static final int MAX_NOTES = 250;
  private static final String DB_CONNECTION = "jdbc:jtds:sqlserver://localhost;DatabaseName=Clinical";
  private static final String DB_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
  private static final String DB_USER = "appserver";
  private static final String DB_PASS = "NxhM4jyEju";
  private static final String DB_SELECT = "select noteText from cfNote";
  
  
  public static void main(String[] args) throws Exception {
    
    int totNumNotes = 0;
    int totNumLines = 0;
    
    DescriptiveStatistics stats1 = new DescriptiveStatistics();
    DescriptiveStatistics stats2 = new DescriptiveStatistics();
    
    try {
      System.out.println("Connecting to DB");
      JdbcConnection jdbcConnection = 
          new JdbcConnection(DB_CONNECTION, DB_DRIVER);
      jdbcConnection.connect(DB_USER, DB_PASS);
      PreparedStatement pstmt = jdbcConnection.getConnection().prepareStatement(DB_SELECT);              
      ResultSet rs = jdbcConnection.doQuery(pstmt);
      while (rs.next()) {
        totNumNotes++;
        String text = rs.getString(1);
        int textLen = text.length();
        stats1.addValue((double)textLen);
        String[] lines = StringUtils.pieceList(text, '\n');
        totNumLines += lines.length;
        for (int i = 0; i < lines.length; i++) {
          int len = lines[i].length();
          stats2.addValue((double)len);
        }
      }
      System.out.println("# notes: " + totNumNotes);
      System.out.println("Total # lines: " + totNumLines);
      System.out.println("Avg # lines/note: " + totNumLines / totNumNotes);
      System.out.println("Avg note length: " + stats1.getMean());
      System.out.println("Min note length: " + stats1.getMin());
      System.out.println("Max note length: " + stats1.getMax());
      System.out.println("Median note length: " + stats1.getPercentile(50));
      System.out.println("Std Dev note length: " + stats1.getStandardDeviation());
      System.out.println("Avg length of lines: " + stats2.getMean());
      System.out.println("Min line length: " + stats2.getMin());
      System.out.println("Max line length: " + stats2.getMax());
      System.out.println("Median line length: " + stats2.getPercentile(50));
      System.out.println("Std Dev line length: " + stats2.getStandardDeviation());
        
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}