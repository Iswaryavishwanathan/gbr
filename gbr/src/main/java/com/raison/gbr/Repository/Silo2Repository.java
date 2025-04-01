package com.raison.gbr.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.raison.gbr.entity.Silo2;


@Repository
public interface Silo2Repository extends JpaRepository<Silo2,Integer> {
    @Query("SELECT s FROM Silo2 s WHERE s.dateTime BETWEEN :startDate AND :endDate")
    List<Silo2> findByDateTimeBetween(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
}
