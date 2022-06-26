package repository;
import model.*;


import java.time.LocalDateTime;

public interface RepoZbor extends Repository<Long, Zbor> {
    public Iterable<Zbor> findByDateTimeDestination(LocalDateTime dataOra2, String destinatie2);
}