package test;

import java.sql.ResultSet;

import gov.va.med.lom.javaUtils.misc.JdbcConnection;

public class TestPerf {

  //static final String DB_URL = "jdbc:bea:sqlserver://vhalomsql01.v22.med.va.gov:1433;DatabaseName=VHASDS";
  //static final String DB_DRIVER = "weblogic.jdbc.sqlserver.SQLServerDriver";
  static final String DB_URL = "jdbc:jtds:sqlserver://localhost:1433;DatabaseName=kcmaEforms";
  static final String DB_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
  
  public static void main(String[] args) throws Exception  {
    JdbcConnection jdbcConnection = new JdbcConnection(DB_URL, DB_DRIVER);
    jdbcConnection.connect("EformsUser", "NxhM4jyEju");
    
    StringBuffer qryStr = new StringBuffer();
    qryStr.append("select datepart(year, pah.PreAdmitDate), datepart(week, pah.PreAdmitDate), ");
    qryStr.append("min(dateadd(week, datediff(week, 0, pah.PreAdmitDate), 0)), count(pah.ProspectID) ");
    qryStr.append("from CAMS_Enterprise.dbo.dashboardPreAdmissionHistory pah, ");
    qryStr.append("CAMS_Enterprise.dbo.dashboardPreAdmissionUpdates pau ");
    qryStr.append("where pah.PreAdmitDate is not null ");
    qryStr.append("and (pah.StatusID = 21 ");
    qryStr.append("or pah.StatusID = 21) ");
    qryStr.append("and pah.AnticipatedEntryTermID = 605 ");
    qryStr.append("and pah.ProspectID = pau.ProspectId ");
    qryStr.append("group by datepart(year, pah.PreAdmitDate), datepart(week, pah.PreAdmitDate) ");
    qryStr.append("order by 1,2");
    
    ResultSet rs = jdbcConnection.doQuery(qryStr.toString()); 
    while (rs.next()) {
      System.out.println(rs.getInt(1));
      System.out.println(rs.getInt(2));
      System.out.println(rs.getDate(3));
      System.out.println(rs.getInt(4));
      System.out.println("-----------------");
    }
  }
  
}
