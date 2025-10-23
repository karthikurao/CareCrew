# Admin Dashboard Visual Guide

## Navigation Flow

```
┌──────────────────────────────────────────┐
│           Care Crew App                  │
│                                          │
│  [Home] [Maps] [Groups] [Profile]       │
└──────────────────────────────────────────┘
                    ↓
                    ↓ Tap Profile
                    ↓
┌──────────────────────────────────────────┐
│        Profile Screen                    │
│  ┌────────────────────────────────┐     │
│  │   👤 Profile Image              │     │
│  │                                 │     │
│  │   John Doe (Admin)              │     │
│  │   john@example.com              │     │
│  └────────────────────────────────┘     │
│                                          │
│  [Bio Section]                          │
│  [Statistics]                           │
│  [Skills]                               │
│  [Interests]                            │
│                                          │
│  ┌────────────────────────────────┐     │
│  │    [Edit Profile]              │     │
│  └────────────────────────────────┘     │
│                                          │
│  ┌────────────────────────────────┐     │
│  │  🎯 [Admin Dashboard] ← NEW!   │     │
│  └────────────────────────────────┘     │
│                                          │
│  ┌────────────────────────────────┐     │
│  │    [Logout]                    │     │
│  └────────────────────────────────┘     │
│                                          │
│  [Home] [Maps] [Groups] [Profile] ✓    │
└──────────────────────────────────────────┘
                    ↓
                    ↓ Tap Admin Dashboard
                    ↓
┌──────────────────────────────────────────┐
│  ← Admin Dashboard              🔄       │
├──────────────────────────────────────────┤
│                                          │
│  Analytics Overview                      │
│                                          │
│  ┌──────────────┐  ┌──────────────┐    │
│  │ Total Users  │  │ Total Events │    │
│  │              │  │              │    │
│  │    125       │  │     45       │    │
│  │              │  │              │    │
│  └──────────────┘  └──────────────┘    │
│                                          │
│  ┌──────────────┐  ┌──────────────┐    │
│  │Total Groups  │  │ Total Posts  │    │
│  │              │  │              │    │
│  │     32       │  │    789       │    │
│  │              │  │              │    │
│  └──────────────┘  └──────────────┘    │
│                                          │
├──────────────────────────────────────────┤
│  Event Management                        │
│                                          │
│  ┌────────────────────────────────────┐ │
│  │ Blood Donation Drive          [X]  │ │
│  │ Help save lives by donating...     │ │
│  │ 📅 2024-12-15  📍 Community Center│ │
│  │ 👥 Participants: 15                │ │
│  └────────────────────────────────────┘ │
│                                          │
│  ┌────────────────────────────────────┐ │
│  │ Flood Relief Effort           [X]  │ │
│  │ Volunteer to help flood...         │ │
│  │ 📅 2024-12-20  📍 Riverside Dist. │ │
│  │ 👥 Participants: 42                │ │
│  └────────────────────────────────────┘ │
│                                          │
│  ┌────────────────────────────────────┐ │
│  │ Tree Plantation Drive         [X]  │ │
│  │ Help green our city by...          │ │
│  │ 📅 2024-12-25  📍 City Park        │ │
│  │ 👥 Participants: 28                │ │
│  └────────────────────────────────────┘ │
│                                          │
└──────────────────────────────────────────┘
```

## UI Components Breakdown

### Header Section
```
┌──────────────────────────────────────────┐
│  [←]  Admin Dashboard           [🔄]    │
└──────────────────────────────────────────┘
 Back                           Refresh
Button                          Button
```

### Analytics Cards
```
┌────────────────────┐
│   Total Users      │  ← Label (gray text)
│                    │
│       125          │  ← Count (large, bold, blue)
│                    │
└────────────────────┘

Colors:
- Total Users: Blue (#0D47A1)
- Total Events: Green (#2E7D32)
- Total Groups: Orange (#E65100)
- Total Posts: Purple (#6A1B9A)
```

### Event Card
```
┌──────────────────────────────────────────┐
│ Blood Donation Drive            [X]      │ ← Title + Delete
│ Help save lives by donating blood...     │ ← Description
│ [Health]  📅 2024-12-15                  │ ← Category + Date
│ 📍 Community Center                       │ ← Location
│ 👥 Participants: 15                       │ ← Participant count
└──────────────────────────────────────────┘

Card Features:
- White background
- 4dp elevation (shadow)
- 8dp corner radius
- 16dp padding
- Ripple effect on touch
```

### Delete Confirmation Dialog
```
┌──────────────────────────────────────┐
│  Delete Event                        │
│                                      │
│  Are you sure you want to delete     │
│  this event?                         │
│                                      │
│         [Cancel]    [Delete]         │
└──────────────────────────────────────┘
```

## Color Scheme

### Primary Colors
- **Background**: White (#FFFFFF)
- **Text Primary**: Black (#000000)
- **Text Secondary**: Dark Gray (#757575)

### Analytics Cards
- **Users Card**: Blue (#0D47A1) / Light Blue Background
- **Events Card**: Green (#2E7D32) / Light Green Background
- **Groups Card**: Orange (#E65100) / Light Orange Background
- **Posts Card**: Purple (#6A1B9A) / Light Purple Background

### Action Colors
- **Delete Button**: Red (#D32F2F)
- **Refresh Button**: Dark Gray (#424242)
- **Category Badge**: Blue (#1976D2)

## Interaction States

### Event Card States
```
Normal:
┌──────────────────────────────────────────┐
│ Blood Donation Drive            [X]      │
│ ...                                      │
└──────────────────────────────────────────┘

Pressed (Ripple Effect):
┌──────────────────────────────────────────┐
│ Blood Donation Drive            [X]      │
│ ... ░░░░░░░░░░░░░░░░░                   │
└──────────────────────────────────────────┘

Delete Button Hover:
┌──────────────────────────────────────────┐
│ Blood Donation Drive            [X]      │
│ ...                             ^^^      │
└──────────────────────────────────────────┘
```

## Loading States

### Analytics Loading
```
┌────────────────────┐
│   Total Users      │
│                    │
│    Loading...      │ ← Shows while fetching
│                    │
└────────────────────┘
```

### Events Loading
```
┌──────────────────────────────────────────┐
│  Event Management                        │
│                                          │
│         ⏳ Loading events...             │ ← Progress indicator
│                                          │
└──────────────────────────────────────────┘
```

### Empty State
```
┌──────────────────────────────────────────┐
│  Event Management                        │
│                                          │
│         📭 No events found               │
│                                          │
└──────────────────────────────────────────┘
```

## Responsive Design

### Phone (Small Screen)
```
Analytics: 2x2 Grid
┌──────┬──────┐
│Users │Events│
├──────┼──────┤
│Groups│Posts │
└──────┴──────┘

Events: Full Width List
┌──────────────┐
│ Event 1      │
├──────────────┤
│ Event 2      │
├──────────────┤
│ Event 3      │
└──────────────┘
```

### Tablet (Large Screen)
```
Analytics: 2x2 Grid (Larger Cards)
┌────────┬────────┐
│ Users  │ Events │
├────────┼────────┤
│ Groups │ Posts  │
└────────┴────────┘

Events: 2-Column Grid
┌──────────┬──────────┐
│ Event 1  │ Event 2  │
├──────────┼──────────┤
│ Event 3  │ Event 4  │
└──────────┴──────────┘
```

## Animation & Transitions

### Screen Transitions
```
Profile → Admin Dashboard:
- Slide from right
- 300ms duration
- Decelerate interpolator

Admin Dashboard → Back:
- Slide to right
- 300ms duration
- Accelerate interpolator
```

### List Animations
```
Events appear:
- Fade in + Slide up
- Staggered (50ms delay between items)
- 200ms duration per item

Event deletion:
- Fade out
- 150ms duration
- Item removed from list
```

### Analytics Updates
```
Count changes:
- Number animates from old to new
- 500ms duration
- CountUp animation
```

## Accessibility

### Content Descriptions
```
✅ Back button: "Navigate back"
✅ Refresh button: "Refresh data"
✅ Delete button: "Delete event"
✅ Analytics cards: "Total users: 125"
```

### Text Sizes
```
- Page Title: 24sp
- Section Headers: 20sp
- Card Titles: 18sp
- Body Text: 14sp
- Labels: 12sp
```

### Touch Targets
```
Minimum touch target: 48dp x 48dp
- Back button: 48dp
- Refresh button: 48dp
- Delete button: 40dp
```

## User Flow Example

```
1. Login as admin
   ↓
2. Navigate to Profile
   ↓
3. See "Admin Dashboard" button
   ↓
4. Tap "Admin Dashboard"
   ↓
5. System verifies admin status
   ↓
6. Load analytics (parallel):
   - Fetch users count
   - Fetch events count
   - Fetch groups count
   - Fetch posts count
   ↓
7. Load events list
   ↓
8. Display dashboard
   ↓
9. Admin can:
   - View all analytics
   - Scroll through events
   - Delete events
   - Refresh data
   - Navigate back
```

## Error States

### Network Error
```
┌──────────────────────────────────────────┐
│  ⚠️ Connection Error                     │
│                                          │
│  Failed to load data. Please check      │
│  your internet connection.              │
│                                          │
│         [Try Again]                      │
└──────────────────────────────────────────┘
```

### Access Denied
```
┌──────────────────────────────────────────┐
│  🚫 Access Denied                        │
│                                          │
│  Admin privileges required to access    │
│  this feature.                          │
│                                          │
│         [Go Back]                        │
└──────────────────────────────────────────┘
```

### Delete Failed
```
Toast message at bottom:
┌──────────────────────────────────────────┐
│  ❌ Failed to delete event. Try again.   │
└──────────────────────────────────────────┘
```

## Success States

### Delete Success
```
Toast message at bottom:
┌──────────────────────────────────────────┐
│  ✅ Event deleted successfully            │
└──────────────────────────────────────────┘
```

### Refresh Success
```
- Analytics numbers update
- Events list refreshes
- Smooth transition
- No toast message (silent success)
```

---

**Note**: This visual guide represents the expected appearance and behavior of the Admin Dashboard. Actual rendering may vary slightly based on device, Android version, and Material Design theme.
