package org.ohgiraffers.misson01board.repository;

import org.ohgiraffers.misson01board.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepositoy extends JpaRepository<Post, Long> {
}
