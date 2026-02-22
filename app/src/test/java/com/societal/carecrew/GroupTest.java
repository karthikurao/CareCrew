package com.societal.carecrew;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for Group model
 */
public class GroupTest {
    
    private Group group;
    
    @Before
    public void setUp() {
        group = new Group("group123", "Community Helpers", "A group for local volunteers", "user123");
    }
    
    @Test
    public void testConstructorSetsCorrectValues() {
        assertEquals("group123", group.getGroupId());
        assertEquals("Community Helpers", group.getName());
        assertEquals("A group for local volunteers", group.getDescription());
        assertEquals("user123", group.getCreatedBy());
    }
    
    @Test
    public void testSettersAndGetters() {
        group.setGroupId("newGroup456");
        assertEquals("newGroup456", group.getGroupId());
        
        group.setName("Disaster Relief Team");
        assertEquals("Disaster Relief Team", group.getName());
        
        group.setDescription("Emergency response volunteers");
        assertEquals("Emergency response volunteers", group.getDescription());
        
        group.setCreatedBy("user456");
        assertEquals("user456", group.getCreatedBy());
    }
    
    @Test
    public void testMembersManagement() {
        Map<String, Object> members = new HashMap<>();
        members.put("user123", true);
        members.put("user456", true);
        members.put("user789", true);
        
        group.setMembers(members);
        assertNotNull(group.getMembers());
        assertEquals(3, group.getMembers().size());
        assertTrue(group.getMembers().containsKey("user123"));
        assertTrue(group.getMembers().containsKey("user456"));
    }
    
    @Test
    public void testNoArgumentConstructor() {
        Group emptyGroup = new Group();
        assertNotNull(emptyGroup);
        // No-arg constructor needed for Firebase deserialization
    }
    
    @Test
    public void testNullMembers() {
        Group newGroup = new Group("g1", "Test Group", "Test Description", "creator1");
        assertNull(newGroup.getMembers());
        
        newGroup.setMembers(null);
        assertNull(newGroup.getMembers());
    }
    
    @Test
    public void testEmptyMembers() {
        Map<String, Object> emptyMembers = new HashMap<>();
        group.setMembers(emptyMembers);
        
        assertNotNull(group.getMembers());
        assertEquals(0, group.getMembers().size());
    }
}
