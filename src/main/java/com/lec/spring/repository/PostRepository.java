package com.lec.spring.repository;

import com.lec.spring.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

// repository layer (aka. Data layer)
// Datasource 에 직접 접근
public interface PostRepository extends JpaRepository<Post, Long> {

    // TODO
    // 특정 id 글 조회수 +1 증가 (UPDATE)
//    int incViewCnt(Long id);


}
