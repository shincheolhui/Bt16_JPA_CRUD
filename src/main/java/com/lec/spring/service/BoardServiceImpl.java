package com.lec.spring.service;

import com.lec.spring.domain.Post;
import com.lec.spring.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private PostRepository postRepository;

    Optional<Post> optionalPost;

    @Override
    public int write(Post post) {
        System.out.println("BoardServiceImpl write() post 값 = " + post); // id 와 regDate 는 null 이 출력되는군

        // TODO
        int result = 0;
        System.out.println("BoardServiceImpl write() post.getId() 값 = " + post.getId()); // id 만 뽑을 수 있군

        if (post.getId() == null) {
            post.setRegDate(LocalDateTime.now()); // 작성일시를 현재시간으로 설정함.
            System.out.println("BoardServiceImpl write() save() 전 post 값 = " + post); // regDate 가 잘 들어갔군, 이제 저장하자.
            postRepository.save(post); // 저장해서 영속적으로 만들었당.
            result = 1;
        }
        return result;
    }

    @Override
    @Transactional
    public Post detail(Long id) {
        Post post = postRepository.findById(id).orElse(null);  // SELECT
        if (post != null) {
            // 조회수 증가
            post.setViewCnt(post.getViewCnt() + 1);
            postRepository.save(post);  // UPDATE
        }
        return post;
    }

    @Override
    public List<Post> list() {
        // TODO
        return postRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    @Override
    public Post selectById(Long id) {
        // TODO
//        optionalPost = postRepository.findById(id); // Optional<Post> 타입
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public int update(Post post) {
        // TODO
        Post data = postRepository.findById(post.getId()).orElse(null);
        if (data == null) return 0;

        data.setSubject(post.getSubject());
        data.setContent(post.getContent());

        postRepository.save(data);  // UPDATE
        return 1;
    }

    @Override
    public int deleteById(Long id) {
        // TODO
        boolean exists = postRepository.existsById(id);
        if (!exists) return 0;

        postRepository.deleteById(id);
        return 1;
    }
}
