package com.raison.gbr.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.raison.gbr.entity.Silo5;


@Repository
public interface Silo5Repository extends JpaRepository<Silo5,Integer> {
    @Query("SELECT s FROM Silo5 s WHERE s.dateTime BETWEEN :startDate AND :endDate")
    List<Silo5> findByDateTimeBetween(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
}
