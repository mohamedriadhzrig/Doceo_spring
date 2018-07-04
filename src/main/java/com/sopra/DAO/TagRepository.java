package com.sopra.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopra.entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	Tag findTagByName(String name);

	List<Tag> findAllByOrderByNameAsc();

	List<Tag> findAllByNameLikeOrderByNameAsc(String text);
}
