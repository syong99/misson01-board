package org.ohgiraffers.misson01board.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostResponse {

    private Long postId;
    private String title;
    private String content;
}
