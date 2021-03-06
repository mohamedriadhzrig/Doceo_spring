package com.sopra.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopra.DAO.TagRepository;
import com.sopra.entities.Tag;
import com.sopra.services.TagService;

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

	@Override
	public List<String> findAll() {
		List<Tag> tags = tagRepository.findAllByOrderByNameAsc();
		List<String> listTag = new ArrayList<String>();
		for (Tag t : tags) {
			listTag.add(t.getName());
		}
		return listTag;
	}

	@Override
	public void remove(Tag tag) {
		tagRepository.delete(tag);

	}

	@Override
	public List<Tag> findAllTagsOrderByName() {

		return tagRepository.findAllByOrderByNameAsc();
	}

	@Override
	public List<String> findSuggestion(String text) {
		List<Tag> tags = tagRepository.findAllByNameLikeOrderByNameAsc("%"+text+"%");
		List<String> listTag = new ArrayList<String>();
		for (Tag t : tags) {
			listTag.add(t.getName());
		}
		return listTag;
	}

}
