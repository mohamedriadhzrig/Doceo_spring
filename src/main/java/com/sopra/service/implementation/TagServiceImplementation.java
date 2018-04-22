package com.sopra.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopra.core.tag.Tag;
import com.sopra.core.tag.TagService;
import com.sopra.repositories.TagRepository;

@Service
public class TagServiceImplementation implements TagService {

	@Autowired
	TagRepository tagRepository;
	@Override
	public Optional<Tag> findTagByName(String name) {
		return Optional.ofNullable(tagRepository.findTagByName(name));
		 
	}
	@Override
	public void save(Tag tag) {
		tagRepository.save(tag);
		
	}

}
