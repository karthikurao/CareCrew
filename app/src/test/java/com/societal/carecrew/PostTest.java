package com.societal.carecrew;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for Post model
 */
public class PostTest {
    
    private Post post;
    
    @Before
    public void setUp() {
        post = new Post("user123", "johndoe", "Great volunteering event today!", 
                       "https://example.com/image.jpg", "post123");
    }
    
    @Test
    public void testConstructorSetsCorrectValues() {
        assertEquals("user123", post.getUid());
        assertEquals("johndoe", post.getUsername());
        assertEquals("Great volunteering event today!", post.getCaption());
        assertEquals("https://example.com/image.jpg", post.getImageUrl());
        assertEquals("post123", post.getPostId());
    }
    
    @Test
    public void testSettersAndGetters() {
        post.setUid("user456");
        assertEquals("user456", post.getUid());
        
        post.setUsername("janedoe");
        assertEquals("janedoe", post.getUsername());
        
        post.setCaption("Helping the community");
        assertEquals("Helping the community", post.getCaption());
        
        post.setImageUrl("https://example.com/newimage.jpg");
        assertEquals("https://example.com/newimage.jpg", post.getImageUrl());
    }
    
    @Test
    public void testNoArgumentConstructor() {
        Post emptyPost = new Post();
        assertNotNull(emptyPost);
        // No-arg constructor needed for Firebase deserialization
    }
    
    @Test
    public void testPostWithEmptyCaption() {
        Post postWithoutCaption = new Post("user1", "user1name", "", 
                                           "https://example.com/img.jpg", "post1");
        assertEquals("", postWithoutCaption.getCaption());
    }
    
    @Test
    public void testPostWithoutImage() {
        Post postWithoutImage = new Post("user1", "user1name", "Just a text post", 
                                         "", "post1");
        assertEquals("", postWithoutImage.getImageUrl());
    }
    
    @Test
    public void testPostIdRetrieval() {
        assertEquals("post123", post.getPostId());
    }
}
