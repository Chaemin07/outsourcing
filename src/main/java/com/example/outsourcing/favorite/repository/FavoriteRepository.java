package com.example.outsourcing.favorite.repository;

import com.example.outsourcing.favorite.entity.Favorite;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserAndStore(User user, Store store);

    Optional<Favorite> findByUserAndStore(User user, Store store);
}
