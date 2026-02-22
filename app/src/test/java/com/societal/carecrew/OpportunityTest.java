package com.societal.carecrew;

import android.os.Parcel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Unit tests for Opportunity model
 * Uses Robolectric for Android Parcelable testing
 */
@RunWith(RobolectricTestRunner.class)
public class OpportunityTest {
    
    private Opportunity opportunity;
    
    @Before
    public void setUp() {
        opportunity = new Opportunity(
            "Beach Cleanup",
            "Help clean up the local beach",
            "2024-11-15",
            "Santa Monica Beach"
        );
    }
    
    @Test
    public void testConstructorSetsCorrectValues() {
        assertEquals("Beach Cleanup", opportunity.getTitle());
        assertEquals("Help clean up the local beach", opportunity.getDescription());
        assertEquals("2024-11-15", opportunity.getDate());
        assertEquals("Santa Monica Beach", opportunity.getLocation());
    }
    
    @Test
    public void testGetters() {
        assertNotNull(opportunity.getTitle());
        assertNotNull(opportunity.getDescription());
        assertNotNull(opportunity.getDate());
        assertNotNull(opportunity.getLocation());
    }
    
    @Test
    public void testParcelable() {
        // Write the opportunity to a Parcel
        Parcel parcel = Parcel.obtain();
        opportunity.writeToParcel(parcel, 0);
        
        // Reset the parcel for reading
        parcel.setDataPosition(0);
        
        // Create opportunity from the parcel
        Opportunity fromParcel = Opportunity.CREATOR.createFromParcel(parcel);
        
        // Verify the data is the same
        assertEquals(opportunity.getTitle(), fromParcel.getTitle());
        assertEquals(opportunity.getDescription(), fromParcel.getDescription());
        assertEquals(opportunity.getDate(), fromParcel.getDate());
        assertEquals(opportunity.getLocation(), fromParcel.getLocation());
        
        parcel.recycle();
    }
    
    @Test
    public void testDescribeContents() {
        assertEquals(0, opportunity.describeContents());
    }
    
    @Test
    public void testCreatorNewArray() {
        Opportunity[] opportunities = Opportunity.CREATOR.newArray(5);
        assertNotNull(opportunities);
        assertEquals(5, opportunities.length);
    }
    
    @Test
    public void testCategoryGetter() {
        // Test that getCategory returns the category field
        // Since category is not set in the constructor, it should be null
        assertNull(opportunity.getCategory());
    }
    
    @Test
    public void testOpportunityWithEmptyStrings() {
        Opportunity emptyOpp = new Opportunity("", "", "", "");
        assertEquals("", emptyOpp.getTitle());
        assertEquals("", emptyOpp.getDescription());
        assertEquals("", emptyOpp.getDate());
        assertEquals("", emptyOpp.getLocation());
    }
}
