package com.ScalableTeam.models.reddit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookmarkPostForm {

        private String userId;
        private String postId;
}
