package com.sopra.services;

import java.util.List;
import java.util.Optional;

import com.sopra.entities.Tag;

public interface TagService {

	Optional<Tag> findTagByName(String name);

	void save(Tag tag);

	List<String> findAll();
	
	List<Tag> findAllTagsOrderByName();
	
	List<String> findSuggestion(String text);

	void remove(Tag tag);
}
