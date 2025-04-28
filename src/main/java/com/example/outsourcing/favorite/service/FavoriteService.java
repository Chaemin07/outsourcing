package com.example.outsourcing.favorite.service;

import com.example.outsourcing.common.exception.BaseException;
import com.example.outsourcing.common.exception.ErrorCode;
import com.example.outsourcing.favorite.dto.reponse.FavoriteResponseDto;
import com.example.outsourcing.favorite.entity.Favorite;
import com.example.outsourcing.favorite.repository.FavoriteRepository;
import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.entity.User;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final FavoriteRepository favoriteRepository;

    // 즐겨찾기 추가
    public FavoriteResponseDto favoriteStore(Long userId, Long storeId) {

        User user = getUserByUserId(userId);
        Store store = getStoreByStoreId(storeId);

        // 중복 즐겨 찾기 불가
        checkDuplicatedFavorite(user, store);

        favoriteRepository.save(new Favorite(user, store));

        return new FavoriteResponseDto(userId, storeId, "즐겨찾기가 완료되었습니다.");
    }

    // 즐겨찾기 삭제
    public void deleteFavorite(Long userId, Long storeId) {

        User user = getUserByUserId(userId);
        Store store = getStoreByStoreId(storeId);

        Favorite favorite = getFavoriteByUserAndStore(user, store);

        favoriteRepository.delete(favorite);
    }

    public void checkDuplicatedFavorite(User user, Store store) {

        if (favoriteRepository.existsByUserAndStore(user, store)) {
            throw new BaseException(ErrorCode.CONFLICT_STATUS);
        }
    }

    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER_ID));
    }

    public Store getStoreByStoreId(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_STORE_ID));
    }

    public Favorite getFavoriteByUserAndStore(User user, Store store) {
        return favoriteRepository.findByUserAndStore(user, store)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FAVORITE_STORE));
    }
}
