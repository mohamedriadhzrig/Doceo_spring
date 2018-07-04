package com.sopra.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.DAO.HistoryRepository;
import com.sopra.entities.History;
import com.sopra.services.HistoryService;
@Transactional
@Service
public class HistoryServiceImplementation implements HistoryService {

	@Autowired
	private HistoryRepository historyRepository;

	@Override
	public History save(History history) {

		return historyRepository.save(history);
	}

}
