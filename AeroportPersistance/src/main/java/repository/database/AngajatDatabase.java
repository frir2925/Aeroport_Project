package repository.database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.CDATASection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import model.*;
import repository.JdbcUtils;
import repository.RepoAngajat;

//public class AngajatDatabase implements Repo<Long, Angajat> {
public class AngajatDatabase implements RepoAngajat {


    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public AngajatDatabase(Properties props) {
        logger.info("Initializing AngajatRepo with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Angajat findOne(Long id2) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Angajat")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    int id=result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");

                    if(id==id2){
                        Angajat angajat = new Angajat(username,password);
                        angajat.setId(Long.valueOf(id));
                        return angajat;
                    }
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB"+e);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Angajat> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Angajat> angajati=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Angajat")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    int id=result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");

                    Angajat angajat = new Angajat(username,password);
                    angajat.setId(Long.valueOf(id));
                    angajati.add(angajat);
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB"+e);
        }
        logger.traceExit(angajati);
        return angajati;
    }

    @Override
    public void save(Angajat elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Angajat (username,password) values (?,?)")){
            preStmt.setString(1,elem.getUsername());
            preStmt.setString(2,elem.getPassword());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("DELETE FROM Angajat WHERE id=(?)")){
            preStmt.setLong(1, id);
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long id, Angajat elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Angajat set username=(?),password=(?) where id=(?)")){
            preStmt.setString(1,elem.getUsername());
            preStmt.setString(2,elem.getPassword());
            preStmt.setLong(3, id);
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

     public Angajat findOneByUsername(String name) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        System.out.println(con);
        try(PreparedStatement preStmt=con.prepareStatement("select * from Angajat")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    int id=result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");


                    if(username.equals(name)){
                        Angajat angajat = new Angajat(username,password);
                        angajat.setId(Long.valueOf(id));
                        return angajat;
                    }
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB"+e);
        }
        logger.traceExit();
        return null;
    }


}
