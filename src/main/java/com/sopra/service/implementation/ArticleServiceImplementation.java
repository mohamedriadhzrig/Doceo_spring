package com.sopra.service.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.core.article.Article;
import com.sopra.core.article.ArticleService;
import com.sopra.core.rate.RateService;
import com.sopra.core.tag.Tag;
import com.sopra.core.tag.TagService;
import com.sopra.core.user.User;
import com.sopra.data.ArticleData;
import com.sopra.data.ArticleDataList;
import com.sopra.data.ProfileData;
import com.sopra.repositories.ArticleRepository;
import com.sopra.repositories.UserRepository;
@Transactional
@Service
public class ArticleServiceImplementation implements ArticleService {

	@Autowired
	ArticleRepository articleRepository;
	
	

	@Autowired
	UserRepository userRepository;

	@Autowired
	TagService tagService;
	
	@Autowired
	RateService rateService;

	@Override
	public void save(Article article) {

		articleRepository.save(article);

	}

	@Override
	public Optional<Article> findById(Long id) {

		return Optional.ofNullable(articleRepository.findOne(id));
	}

	@Override
	public Optional<Article> findBySlug(String slug, User user) {
		return Optional.ofNullable(articleRepository.findBySlug(slug, user));
	}

	@Override
	public void remove(Article article) {
		articleRepository.delete(article);

	}

	@Override
	public ArticleDataList findArticles(String author, User user) {

		List<Article> list = new ArrayList<Article>();
		list.addAll(articleRepository.findArticles(author));
		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(a.getUser().getUsername() == user.getUsername() || a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating=0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setFollowing(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}

		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;
	}

	@Override
	public Article findArticleBySlug(String slug) {

		return articleRepository.findArticleBySlug(slug);
	}

	@Override
	public ArticleDataList findArticlesByTag(String tag, User user) {
		Optional<Tag> thisTag = tagService.findTagByName(tag);
		List<Article> list = new ArrayList<Article>();
		if (thisTag.isPresent())
			list.addAll(articleRepository.findArticlesByTag(thisTag.get()));

		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(a.getUser().getUsername() == user.getUsername() || a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating=0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setFollowing(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}

		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;
	}

	@Override
	public ArticleDataList findFavoriteArticles(String uername, User user) {
		List<Article> list = new ArrayList<Article>();
		list.addAll(userRepository.findUserByUsername(uername).getFavoriteArticles());
		Collections.sort(list, (o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(a.getUser().getUsername() == user.getUsername() || a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating=0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setFollowing(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}
		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;
	}

	@Override
	public ArticleDataList findAllValide(User user) {
		List<Article> list = new ArrayList<Article>();
		list.addAll(articleRepository.findValideArticle());
		
		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(a.getUser().getUsername() == user.getUsername() || a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating=0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setFollowing(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}
		
		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;

	}

	@Override
	public ArticleDataList findAllInvalide() {
		List<Article> list = new ArrayList<Article>();
		list.addAll(articleRepository.findAllInvalide());
		
		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(false);
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating=0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setFollowing(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}
		System.out.println(list1.size()+"******"+list.size());
		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;

	}

	@Override
	public ArticleDataList findValidArticlesByUser(String author, User user) {
		List<Article> list = new ArrayList<Article>();
		list.addAll(articleRepository.findValidArticlesByUser(author));
		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article a : list) {
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(a.getUser().getUsername() == user.getUsername() || a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating=0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setFollowing(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}

		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return articleDataList;
	}

}
