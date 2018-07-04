package com.sopra.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopra.entities.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>{

}
