package repository.database;
import model.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.w3c.dom.CDATASection;
import repository.JdbcUtils;
import repository.RepoBilet;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

//public class BiletDatabase implements Repo<Long, Bilet> {
public class BiletDatabase implements RepoBilet {

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public BiletDatabase(Properties props) {
        logger.info("Initializing BiletRepo with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Bilet findOne(Long id2) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Bilet")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    int id=result.getInt("id");

                    String client = result.getString("client");
                    String tur = result.getString("turisti");
                    
                    String[] tur2=tur.split("!");
                    List<String> turisti = new ArrayList<>(Arrays.asList(tur2));

                    String adresa = result.getString("adresa");
                    int zborId = result.getInt("zborId");

                    if(id==id2){
                        Bilet bilet = new Bilet(client,turisti,adresa,zborId);
                        bilet.setId(Long.valueOf(id));
                        return bilet;
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
    public Iterable<Bilet> findAll() {

        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Bilet> bilete=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Bilet")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    int id=result.getInt("id");

                    String client = result.getString("client");
                    String tur = result.getString("turisti");

                    String[] tur2=tur.split("!");
                    List<String> turisti = new ArrayList<>(Arrays.asList(tur2));

                    String adresa = result.getString("adresa");
                    int zborId = result.getInt("zborId");

                    Bilet bilet = new Bilet(client,turisti,adresa,zborId);
                    bilet.setId(Long.valueOf(id));
                    bilete.add(bilet);
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB"+e);
        }
        logger.traceExit(bilete);
        return bilete;
    }

    @Override
    public void save(Bilet elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Bilet (client, turisti, adresa, zborId) values (?,?,?,?)")){
            preStmt.setString(1,elem.getClient());
            String turisti= String.join("!",elem.getTuristi());
            preStmt.setString(2,turisti);
            preStmt.setString(3,elem.getAdresa());
            preStmt.setInt(4,elem.getZborId());

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
        try(PreparedStatement preStmt=con.prepareStatement("DELETE FROM Bilet WHERE id=(?)")){
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
    public void update(Long id2, Bilet elem) {

        logger.traceEntry("saving task {}", elem);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Bilet set client=(?), turisti=(?), adresa=(?), zborId=(?) where id=(?)")){
            preStmt.setString(1,elem.getClient());
            String turisti= String.join("!",elem.getTuristi());
            preStmt.setString(2,turisti);
            preStmt.setString(3,elem.getAdresa());
            preStmt.setInt(4,elem.getZborId());
            preStmt.setLong(5, id2);
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }
}
