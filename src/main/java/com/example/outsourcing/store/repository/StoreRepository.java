package com.example.outsourcing.store.repository;

import com.example.outsourcing.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByUserId(Long userId);

    default Store findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다"));
    }
}
