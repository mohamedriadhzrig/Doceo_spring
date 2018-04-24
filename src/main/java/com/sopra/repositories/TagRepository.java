package com.sopra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopra.core.tag.Tag;



@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{

	Tag findTagByName(String name);
}