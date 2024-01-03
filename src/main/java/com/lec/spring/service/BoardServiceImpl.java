package com.lec.spring.service;

import com.lec.spring.domain.Post;
import com.lec.spring.repository.PostRepository;
import org.apache.tomcat.jni.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public Post detail(Long id) {
        // TODO
        optionalPost = postRepository.findById(id);
        Post post = optionalPost.get();
        System.out.println("BoardServiceImpl, detail(Long id), Post post = optionalPost.get() 는 " + post); // post 값을 확인했다. 이제 조회수만 뽑아서 더하기 1을 하고 다시 설정해보자.

        System.out.println("BoardServiceImpl, detail(Long id), post.getViewCnt() 는 " + post.getViewCnt()); // 현재 조회수도 확인했다.
        long incViewCnt = post.getViewCnt() + 1; // 현재 조회수에 1을 더했다.
        System.out.println("현재 조회수에 더하기 1 은 " + incViewCnt); // 더하기가 아주 잘되는군, 이제 더한 값을 저장해서 조회수를 올려보자.
        post.setViewCnt(incViewCnt);
        postRepository.save(post);
        // 저장까지 잘되네, 목록화면에서 특정 글을 클릭해서 조회하면으로 이동하면 목록화면의 조회수보다 조회화면의 조회수가 1이 크고, 다시 목록화면으로 이동하면 조회화면의 조회수가 목록화면에 똑같이 나온당.

        return post;
    }

    @Override
    public List<Post> list() {
        // TODO
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Post selectById(Long id) {
        // TODO
        optionalPost = postRepository.findById(id); // Optional<Post> 타입
        return optionalPost.get();
    }

    @Override
    public int update(Post post) {
        // TODO
        int result = 0;
        if (post.getId() != null) {
            postRepository.save(post);
            result = 1;
        }
        return result;
    }

    @Override
    public int deleteById(Long id) {
        // TODO
        int result = 0;
        optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            postRepository.deleteById(id);
            result = 1;
        }
        return result;
    }
}
