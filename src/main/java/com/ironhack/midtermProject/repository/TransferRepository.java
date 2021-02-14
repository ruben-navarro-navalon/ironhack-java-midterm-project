package com.ironhack.midtermProject.repository;

import com.ironhack.midtermProject.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    // Selects a list with all transactions in the last second:
    @Query(value= "SELECT * FROM transfer WHERE (origin_account_id = :accountId OR destination_account_id = :accountId) AND date > NOW() - INTERVAL 1 SECOND", nativeQuery = true)
    public List<Transfer> lastSecondFraud(@Param("accountId") Long accountId);

    // Returns the sum of all transfers in the last 24 hours:
    @Query(value="SELECT SUM(amount) AS sum FROM transfer WHERE origin_account_id = :accountId AND Date(date) >= NOW() - INTERVAL 1 DAY", nativeQuery = true)
    public BigDecimal last24hours(@Param("accountId") Long accountId);

    // Returns the max sum of all transfers within 24 hours.
    @Query(value="SELECT MAX(t.sum) FROM (SELECT DATE(date) AS transfer_date, SUM(amount) AS sum FROM transfer WHERE origin_account_id = :accountId GROUP BY transfer_date) AS t", nativeQuery = true)
    public BigDecimal maxIn24hours(@Param("accountId") Long accountId);
}
