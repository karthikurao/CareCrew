package com.societal.carecrew;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for HelperClass model
 */
public class HelperClassTest {
    
    private HelperClass user;
    
    @Before
    public void setUp() {
        user = new HelperClass("John Doe", "john@example.com", "johndoe", "password123");
    }
    
    @Test
    public void testConstructorSetsCorrectValues() {
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("johndoe", user.getUsername());
        assertEquals("password123", user.getPassword());
    }
    
    @Test
    public void testConstructorInitializesDefaults() {
        assertEquals("", user.getProfileImageUrl());
        assertEquals("", user.getCoverImageUrl());
        assertEquals("", user.getBio());
        assertEquals(0, user.getHoursVolunteered());
        assertEquals(0, user.getOpportunitiesParticipated());
        assertEquals(0, user.getGroupsJoined());
        assertEquals("", user.getLocation());
        assertEquals("", user.getAboutMe());
    }
    
    @Test
    public void testSettersAndGetters() {
        user.setProfileImageUrl("https://example.com/profile.jpg");
        assertEquals("https://example.com/profile.jpg", user.getProfileImageUrl());
        
        user.setCoverImageUrl("https://example.com/cover.jpg");
        assertEquals("https://example.com/cover.jpg", user.getCoverImageUrl());
        
        user.setBio("Passionate volunteer");
        assertEquals("Passionate volunteer", user.getBio());
        
        user.setHoursVolunteered(100);
        assertEquals(100, user.getHoursVolunteered());
        
        user.setOpportunitiesParticipated(5);
        assertEquals(5, user.getOpportunitiesParticipated());
        
        user.setGroupsJoined(3);
        assertEquals(3, user.getGroupsJoined());
        
        user.setLocation("New York");
        assertEquals("New York", user.getLocation());
        
        user.setAboutMe("I love volunteering");
        assertEquals("I love volunteering", user.getAboutMe());
    }
    
    @Test
    public void testSkillsManagement() {
        List<String> skills = new ArrayList<>();
        skills.add("First Aid");
        skills.add("Teaching");
        
        user.setSkills(skills);
        assertNotNull(user.getSkills());
        assertEquals(2, user.getSkills().size());
        assertTrue(user.getSkills().contains("First Aid"));
        assertTrue(user.getSkills().contains("Teaching"));
    }
    
    @Test
    public void testInterestsManagement() {
        List<String> interests = new ArrayList<>();
        interests.add("Environment");
        interests.add("Education");
        
        user.setInterests(interests);
        assertNotNull(user.getInterests());
        assertEquals(2, user.getInterests().size());
        assertTrue(user.getInterests().contains("Environment"));
    }
    
    @Test
    public void testAvailabilityManagement() {
        Availability availability = new Availability(true, false, true, false, true);
        user.setAvailability(availability);
        
        assertNotNull(user.getAvailability());
        assertTrue(user.getAvailability().isWeekdays());
        assertFalse(user.getAvailability().isWeekends());
        assertTrue(user.getAvailability().isMornings());
    }
    
    @Test
    public void testCausesManagement() {
        List<String> causes = new ArrayList<>();
        causes.add("Disaster Relief");
        causes.add("Food Security");
        
        user.setCauses(causes);
        assertNotNull(user.getCauses());
        assertEquals(2, user.getCauses().size());
        assertTrue(user.getCauses().contains("Disaster Relief"));
    }
    
    @Test
    public void testVolunteerExperienceManagement() {
        List<VolunteerExperience> experiences = new ArrayList<>();
        experiences.add(new VolunteerExperience("Red Cross", "Volunteer", "2020-2021", "Helped with disaster relief"));
        
        user.setVolunteerExperience(experiences);
        assertNotNull(user.getVolunteerExperience());
        assertEquals(1, user.getVolunteerExperience().size());
        assertEquals("Red Cross", user.getVolunteerExperience().get(0).getOrganization());
    }
    
    @Test
    public void testSocialLinksManagement() {
        Map<String, String> socialLinks = new HashMap<>();
        socialLinks.put("twitter", "https://twitter.com/johndoe");
        socialLinks.put("linkedin", "https://linkedin.com/in/johndoe");
        
        user.setSocialLinks(socialLinks);
        assertNotNull(user.getSocialLinks());
        assertEquals(2, user.getSocialLinks().size());
        assertEquals("https://twitter.com/johndoe", user.getSocialLinks().get("twitter"));
    }
    
    @Test
    public void testNoArgumentConstructor() {
        HelperClass emptyUser = new HelperClass();
        assertNotNull(emptyUser);
        // No-arg constructor needed for Firebase deserialization
    }
}
