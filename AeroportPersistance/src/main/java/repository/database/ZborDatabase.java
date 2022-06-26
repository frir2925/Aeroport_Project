package repository.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.*;

import org.w3c.dom.CDATASection;
import repository.JdbcUtils;
import repository.RepoZbor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

//public class ZborDatabase implements Repo<Long, Zbor> {
public class ZborDatabase implements RepoZbor {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public ZborDatabase(Properties props) {
        logger.info("Initializing ZborRepo with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }


    @Override
    public Zbor findOne(Long id2) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Zbor")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String destinatie = result.getString("destinatie");
                    LocalDateTime dataOra = result.getTimestamp("dataOra").toLocalDateTime();
                    String aeroport = result.getString("aeroport");
                    int locuri = result.getInt("locuri");

                    if (id == id2) {
                        Zbor zbor = new Zbor(destinatie, dataOra, aeroport, locuri);
                        zbor.setId(Long.valueOf(id));
                        return zbor;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Zbor> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Zbor> zboruri = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Zbor")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String destinatie = result.getString("destinatie");
                    LocalDateTime dataOra = result.getTimestamp("dataOra").toLocalDateTime();
                    String aeroport = result.getString("aeroport");
                    int locuri = result.getInt("locuri");
                    Zbor zbor = new Zbor(destinatie, dataOra, aeroport, locuri);
                    zbor.setId(Long.valueOf(id));
                    zboruri.add(zbor);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(zboruri);
        return zboruri;
    }

    public Iterable<Zbor> findByDateTimeDestination(LocalDateTime dataOra2, String destinatie2) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Zbor> zboruri = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Zbor")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String destinatie = result.getString("destinatie");
                    LocalDateTime dataOra = result.getTimestamp("dataOra").toLocalDateTime();
                    String aeroport = result.getString("aeroport");
                    int locuri = result.getInt("locuri");

                    if (destinatie.equals(destinatie2)) {
                        if (dataOra.getYear() == dataOra2.getYear() && dataOra.getMonth() == dataOra2.getMonth() && dataOra.getDayOfMonth() == dataOra2.getDayOfMonth()) {
                            Zbor zbor = new Zbor(destinatie, dataOra, aeroport, locuri);
                            zbor.setId(Long.valueOf(id));
                            zboruri.add(zbor);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(zboruri);
        return zboruri;

    }

    @Override
    public void save(Zbor elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Zbor (destinatie, dataOra, aeroport, locuri) values (?,?,?,?)")) {
            preStmt.setString(1, elem.getDestinatie());
            preStmt.setTimestamp(2, Timestamp.valueOf(elem.getDataOra()));
            preStmt.setString(3, elem.getAeroport());
            preStmt.setInt(4, elem.getLocuri());

            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM Zbor WHERE id=(?)")) {
            preStmt.setLong(1, id);
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long id, Zbor elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Zbor set destinatie=(?), dataOra=(?), aeroport=(?), locuri=(?) where id=(?)")) {
            preStmt.setString(1, elem.getDestinatie());
            preStmt.setTimestamp(2, Timestamp.valueOf(elem.getDataOra()));
            preStmt.setString(3, elem.getAeroport());
            preStmt.setInt(4, elem.getLocuri());
            preStmt.setLong(5, id);
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();

    }

}