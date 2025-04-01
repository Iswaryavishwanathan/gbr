package com.raison.gbr.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.raison.gbr.entity.Silo4;


@Repository
public interface Silo4Repository extends JpaRepository<Silo4,Integer> {
    @Query("SELECT s FROM Silo4 s WHERE s.dateTime BETWEEN :startDate AND :endDate")
    List<Silo4> findByDateTimeBetween(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
}
