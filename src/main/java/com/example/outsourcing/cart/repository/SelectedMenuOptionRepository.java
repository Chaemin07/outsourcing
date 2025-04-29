package com.example.outsourcing.cart.repository;

import com.example.outsourcing.cart.entity.SelectedMenuOption;
import com.example.outsourcing.menu.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SelectedMenuOptionRepository extends JpaRepository<SelectedMenuOption, Long> {

    // 특정 menu에 담긴 모든 옵션 조회
    List<MenuOption> findByMenuId(Long menuId);

}
