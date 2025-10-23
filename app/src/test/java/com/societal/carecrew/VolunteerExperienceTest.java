package com.societal.carecrew;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for VolunteerExperience model
 */
public class VolunteerExperienceTest {
    
    private VolunteerExperience experience;
    
    @Before
    public void setUp() {
        experience = new VolunteerExperience(
            "Red Cross",
            "Volunteer Coordinator",
            "2020-2022",
            "Coordinated disaster relief efforts"
        );
    }
    
    @Test
    public void testConstructorSetsCorrectValues() {
        assertEquals("Red Cross", experience.getOrganization());
        assertEquals("Volunteer Coordinator", experience.getRole());
        assertEquals("2020-2022", experience.getDuration());
        assertEquals("Coordinated disaster relief efforts", experience.getDescription());
    }
    
    @Test
    public void testSettersAndGetters() {
        experience.setOrganization("Habitat for Humanity");
        assertEquals("Habitat for Humanity", experience.getOrganization());
        
        experience.setRole("Builder");
        assertEquals("Builder", experience.getRole());
        
        experience.setDuration("2023-Present");
        assertEquals("2023-Present", experience.getDuration());
        
        experience.setDescription("Building homes for families in need");
        assertEquals("Building homes for families in need", experience.getDescription());
    }
    
    @Test
    public void testExperienceWithEmptyStrings() {
        VolunteerExperience emptyExp = new VolunteerExperience("", "", "", "");
        assertEquals("", emptyExp.getOrganization());
        assertEquals("", emptyExp.getRole());
        assertEquals("", emptyExp.getDuration());
        assertEquals("", emptyExp.getDescription());
    }
    
    @Test
    public void testExperienceUpdate() {
        // Test updating all fields
        experience.setOrganization("United Way");
        experience.setRole("Team Leader");
        experience.setDuration("2023-2024");
        experience.setDescription("Leading community outreach programs");
        
        assertEquals("United Way", experience.getOrganization());
        assertEquals("Team Leader", experience.getRole());
        assertEquals("2023-2024", experience.getDuration());
        assertEquals("Leading community outreach programs", experience.getDescription());
    }
}
