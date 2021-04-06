
package code;



import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
public class DataBase {
    DefaultTableModel dm = new DefaultTableModel();
    String devName = null, testerName = null, bugId = null, bugTitle = null, description = null, date = null, path = null, status = null;
    byte [] pht = null;


    private Connection connection(){
    Connection con = null;
    try{
         con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bugreports","root","");
    }catch(SQLException exc){System.out.println(exc);}
    return con;
    }
    
    public void insertion(String bugId, String bugTitle, String description,String date ,String status,String path,String testerName,String devName){
        
        
        String sql = "INSERT INTO bug(bugID,bugName,status,description,files,tester,developer,time) VALUES(?,?,?,?,?,?,?,?)";
    try(Connection con = this.connection();
            PreparedStatement stm = con.prepareStatement(sql)){
        stm.setString(1, bugId);
        stm.setString(2, bugTitle);
        stm.setString(3, status);
        stm.setString(4,description);
        stm.setString(6, testerName);
        stm.setString(7, devName);
        stm.setString(8, date);
        if(path.isEmpty()){
                stm.setNull(5, java.sql.Types.BLOB);
            }else{
                 InputStream in = new FileInputStream(path);
                 stm.setBlob(5, in);}
        stm.execute();
        
        
    }catch(Exception exc){System.out.println(exc);}     
}
    public void excerstion(DefaultTableModel dml){
        String sql = "SELECT * FROM bug";
        try{
            Connection connect = this.connection();
            Statement stm = connect.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
            bugId = rs.getString("bugID");
            bugTitle = rs.getString("bugName");
            description = rs.getString("description");
            status = rs.getString("status");
            byte [] img = rs.getBytes("files");
            pht = img;
            date = rs.getString("time");
            devName = rs.getString("developer");
            testerName = rs.getString("tester");
            dml.addRow(new Object[]{bugId,bugTitle,description,img.toString(),status,testerName,devName,date});
            }
        }
        catch(SQLException exc){System.out.println(exc);}
    }
    public void updateStatus(String bid, String value){

        String sql = "UPDATE bug SET status =? WHERE bugID =?";
        try{
            Connection connect = this.connection();
            PreparedStatement stm = connect.prepareStatement(sql);
            stm.setString(1, value);
            stm.setString(2, bid);
            stm.executeUpdate();
            System.out.println(sql);
        }
        catch(SQLException ex){System.out.println(ex);}
    }
    public byte[] photox(String bid) {
        try(Connection connect = this.connection();
            Statement st = connect.createStatement()){
            System.out.println("here  ");
            String sql = "SELECT files FROM bug WHERE bugID = "+bid+"";
         ResultSet rs = st.executeQuery(sql);
         if(rs.next()){
             byte[] image = rs.getBytes("files");
             
             return image;
         }
         }
        catch(Exception ex){System.out.println(ex);}
        return null;
        }
        
        
}
