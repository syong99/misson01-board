package org.ohgiraffers.misson01board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ohgiraffers.misson01board.domain.dto.*;
import org.ohgiraffers.misson01board.domain.entity.Post;
import org.ohgiraffers.misson01board.repository.PostRepositoy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service 를 인터페이스와 구현체로 나누는 이유
 * 1.다향성과 OCP 원칙을 지키기 위해서
 * 인터페이스와 구현체가 나누어지면, 구현체는 외부로부터 독립되어, 구현체의 수정이나 확장이 자유로워진다.
 * 2. 관습적인 추상화 방식
 * 과거, Spring에서 AOP를 구현 할때 JDK Dynamic Proxy 를 사용했을데, 이때 인터페이스가 필수였다.
 * 지금은, CGLB를 기본적으로 포함하여 클래스 기반을 프록시 객체를 생성 할 수 있게 되었다.
 */

/** @Transactional
 * 선언적으로 트랜젝션 관리를 가능하게 해준다.
 * 메소드가 실행되는 동안 모든 데이터베이스 연산을 하나의 트랜잭션으로 묶어 처리한다.
 * 이를통해, 메소드 내에서 데이터베이스 상태를 변경하는 작업들이 성공적으로 완료되면 그 변경사항을 commit 하고
 * 하나라도 실패하면 모든 변경사항을 rollback 시켜 관리한다.
 *
 * Transaction
 * 데이터베이스의 상태를 변화시키기 위해 수행하는 작업의 단위
 * (readonly=true)는 없는거랑 다름없다. 해놓는 이유는 죄회를 하면 필요가 없는데 변경하는 작동은 트랜잭션을 이용해
 * 관리해야한다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private  final PostRepositoy postRepositoy;//의존
    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request) {

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        Post savedPost = postRepositoy.save(post);

        return new CreatePostResponse(savedPost.getPostId(), savedPost.getTitle(), savedPost.getContent());
    }

    public ReadPostResponse readPostById(Long postId) {

        Post foundPost = postRepositoy.findById(postId)/** 있을지 없을지 모르는 아이디때문에 예외처리함**/
                .orElseThrow(() -> new EntityNotFoundException("해당 postId로 죄회딘 게시글이 없습니다."));

        return new ReadPostResponse(foundPost.getPostId(), foundPost.getTitle(), foundPost.getContent());
    }

    @Transactional
    public UpdatePostResponse updatePost(Long postId, UpdatePostRequest request) {
        Post foundPost = postRepositoy.findById(postId)/** 있을지 없을지 모르는 아이디때문에 예외처리함**/
                .orElseThrow(() -> new EntityNotFoundException("해당 postId로 죄회딘 게시글이 없습니다."));

        //Dirty Checking
        //JPA가 알아서 감지하고 수정 저장하는 곳은 없다.(자동)
        foundPost.update(request.getTitle(), request.getContent());

        return new UpdatePostResponse(foundPost.getPostId(),foundPost.getTitle(),foundPost.getContent());
    }

    @Transactional
    public DeletePostResponse deletePost(Long postId) {

        Post foundPost = postRepositoy.findById(postId)/** 있을지 없을지 모르는 아이디때문에 예외처리함**/
                .orElseThrow(() -> new EntityNotFoundException("해당 postId로 죄회딘 게시글이 없습니다."));

        postRepositoy.delete(foundPost);

        return new DeletePostResponse(foundPost.getPostId());
    }

    public Page<ReadPostResponse> readAllPost(Pageable pageable) {

        Page<Post> postPage = postRepositoy.findAll(pageable);

        return postPage.map(post -> new ReadPostResponse(post.getPostId(), post.getTitle(), post.getContent()));
    }
}
