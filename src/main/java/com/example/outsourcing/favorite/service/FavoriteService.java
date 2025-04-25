package com.example.outsourcing.favorite.service;

import com.example.outsourcing.favorite.dto.reponse.FavoriteResponseDto;
import com.example.outsourcing.favorite.entity.Favorite;
import com.example.outsourcing.favorite.repository.FavoriteRepository;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteResponseDto favoriteStore(Long userId, Long storeId) {

        User user = getValidUser(userId);
        Store store = getValidStore(storeId);

        if (checkDuplicatedFavorite(user, store)) {
            throw new RuntimeException("중복 즐겨찾기 불가");
        }

        favoriteRepository.save(new Favorite(user, store));

        return new FavoriteResponseDto(userId, storeId, "즐겨찾기가 완료되었습니다.");
    }

    public void deleteFavorite(Long userId, Long storeId) {

        User user = getValidUser(userId);
        Store store = getValidStore(storeId);

        Favorite favorite = favoriteRepository.findByUserAndStore(user, store)
                .orElseThrow(() -> new RuntimeException("즐겨찾기가 안된 가게입니다."));

        favoriteRepository.delete(favorite);
    }

    public boolean checkDuplicatedFavorite(User user, Store store) {

        return favoriteRepository.existsByUserAndStore(user, store);
    }

    public User getValidUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
    }

    public Store getValidStore(Long storeId) {

        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 가게입니다."));
    }
}
