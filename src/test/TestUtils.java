package test;

import java.io.*;
import java.util.Date;
import java.sql.*;

import gov.va.med.lom.javaUtils.misc.JdbcConnection;
import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.javaUtils.file.TextFile;

public class TestUtils {

  static final String DB_URL = "jdbc:jtds:sqlserver://localhost:1433;DatabaseName=Clinical";
  static final String DB_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
  
  public static void main(String[] args) throws Exception  {
    JdbcConnection jdbcConnection = new JdbcConnection(DB_URL, DB_DRIVER);
    jdbcConnection.connect("appserver", "NxhM4jyEju");
    
    int test = 2;
    
    // TEST 1
    if (test == 1) {
      PreparedStatement ps = jdbcConnection.getPreparedStatement("select value from cfTest where name='key3'");
      
      Date start = new Date();
      for (int i = 0; i < 2000000; i++) {
        ResultSet rs = jdbcConnection.doQuery(ps);
        /*
        while (rs.next()) {
          System.out.print(rs.getString(1));
          System.out.print(" ");
          System.out.println((new Date().getTime() - d1.getTime()));
        }
        */
      }
      System.out.println("Total Time (ms): " + (new Date().getTime() - start.getTime()));
      
    } else if (test == 2) {
      
      // TEST 2
      Date start = new Date();
      TextFile textFile = new TextFile("/wc/jee/ll-copyfind/test/src/test/textfile2.txt");
      String text = StringUtils.escapeQuotes(textFile.getText());
      System.out.println("len=" + text.length());
      System.out.println("File loaded in " + (new Date().getTime() - start.getTime()) + " msec");
      start = new Date();
      ResultSet rs = jdbcConnection.doQuery("select text from cfTest where id = 4");
      System.out.println("Query returned in " + (new Date().getTime() - start.getTime()) + " msec");
      rs.next();
      System.out.println("len=" + rs.getString(1).length());
      
    } else if (test == 3) {
      
      // TEST 3
      Date start = new Date();
      for (int i = 0; i < 1000; i++) {             
        RandomAccessFile raf = new RandomAccessFile("/wc/jee/ll-copyfind/test/src/test/textfile2.txt", "r");
        int offset = StringUtils.getRandomInt(1, 2000000);
        raf.seek(offset);
        final FileInputStream fin = new FileInputStream(raf.getFD());
        byte[] b = new byte[1];
        fin.read(b, 0, 1);
        //System.out.println(b[0]);
      }
      System.out.println("Total Time (ms): " + (new Date().getTime() - start.getTime()));
    }
    
    
  }
  
}
