package com.sopra.core.tag;

import java.util.Optional;

public interface TagService {

	Optional<Tag> findTagByName(String name);
	
	void save(Tag tag);
}
