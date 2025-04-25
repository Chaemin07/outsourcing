package com.example.outsourcing.store.repository;

import com.example.outsourcing.store.entity.Store;
import com.example.outsourcing.store.entity.StoreStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByStatusNot(StoreStatus status);

    int countByUserIdAndStatusNot(Long userId, StoreStatus status);

    default Store findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다"));
    }

    List<Store> findByNameContainingAndStatusNot(String keyword, StoreStatus status);
}