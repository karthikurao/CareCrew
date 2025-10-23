# Admin Dashboard Architecture

## Component Overview

```
┌─────────────────────────────────────────────────────────────┐
│                      Care Crew App                          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    ProfileActivity                          │
│  ┌────────────────────────────────────────────────────┐    │
│  │  Check if user.isAdmin = true                      │    │
│  │  If true: Show "Admin Dashboard" button            │    │
│  └────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│              AdminDashboardActivity                         │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │  1. Verify Admin Access                            │    │
│  │     - Check Firebase: users/{uid}/isAdmin          │    │
│  │     - If false: Deny access & close activity       │    │
│  └────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │  2. Analytics Dashboard                            │    │
│  │     ┌─────────────┬─────────────┐                  │    │
│  │     │ Total Users │Total Events │                  │    │
│  │     │     ###     │     ###     │                  │    │
│  │     └─────────────┴─────────────┘                  │    │
│  │     ┌─────────────┬─────────────┐                  │    │
│  │     │Total Groups │Total Posts  │                  │    │
│  │     │     ###     │     ###     │                  │    │
│  │     └─────────────┴─────────────┘                  │    │
│  └────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │  3. Event Management                               │    │
│  │     ┌────────────────────────────────────────┐     │    │
│  │     │ Event 1: Blood Donation Drive    [X]  │     │    │
│  │     │ Date: 2024-12-01 | Location: City     │     │    │
│  │     └────────────────────────────────────────┘     │    │
│  │     ┌────────────────────────────────────────┐     │    │
│  │     │ Event 2: Flood Relief            [X]  │     │    │
│  │     │ Date: 2024-12-15 | Location: Town     │     │    │
│  │     └────────────────────────────────────────┘     │    │
│  └────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

## Data Flow

```
┌─────────────────┐
│  Firebase Auth  │
│   (User Auth)   │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│      Firebase Realtime Database         │
│  ┌──────────────────────────────────┐  │
│  │  /users/{uid}/                   │  │
│  │    - isAdmin: Boolean            │  │
│  │    - name: String                │  │
│  │    - email: String               │  │
│  └──────────────────────────────────┘  │
│  ┌──────────────────────────────────┐  │
│  │  /events/{eventId}/              │  │
│  │    - title: String               │  │
│  │    - description: String         │  │
│  │    - date: String                │  │
│  │    - location: String            │  │
│  │    - category: String            │  │
│  │    - participantCount: Number    │  │
│  └──────────────────────────────────┘  │
│  ┌──────────────────────────────────┐  │
│  │  /groups/{groupId}/              │  │
│  └──────────────────────────────────┘  │
│  ┌──────────────────────────────────┐  │
│  │  /posts/{postId}/                │  │
│  └──────────────────────────────────┘  │
└─────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│   AdminDashboardActivity Listeners      │
│  - ValueEventListener for users count   │
│  - ValueEventListener for events count  │
│  - ValueEventListener for groups count  │
│  - ValueEventListener for posts count   │
│  - ValueEventListener for events list   │
└─────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│         UI Components                   │
│  - Analytics Cards (TextViews)          │
│  - RecyclerView (EventAdapter)          │
│  - ProgressBar                          │
└─────────────────────────────────────────┘
```

## Class Relationships

```
┌──────────────────────────┐
│  AdminDashboardActivity  │
│                          │
│  - binding               │
│  - mAuth                 │
│  - eventsRef             │◄──────────┐
│  - usersRef              │           │
│  - groupsRef             │           │
│  - postsRef              │           │
│  - eventList             │           │
│  - eventAdapter          │───────┐   │
│                          │       │   │
│  + onCreate()            │       │   │
│  + checkAdminAccess()    │       │   │
│  + loadAnalytics()       │       │   │
│  + loadEvents()          │       │   │
│  + deleteEvent()         │       │   │
└──────────────────────────┘       │   │
                                   │   │
                                   │   │
                ┌──────────────────▼───▼─────┐
                │     EventAdapter           │
                │                            │
                │  - eventList               │
                │  - context                 │
                │                            │
                │  + onCreateViewHolder()    │
                │  + onBindViewHolder()      │
                │  + getItemCount()          │
                └────────────────────────────┘
                           │
                           │ uses
                           ▼
                ┌──────────────────┐
                │      Event       │
                │                  │
                │  - eventId       │
                │  - title         │
                │  - description   │
                │  - date          │
                │  - location      │
                │  - category      │
                │  - creatorId     │
                │  - timestamp     │
                │  - participantCount │
                │                  │
                │  + getters()     │
                │  + setters()     │
                └──────────────────┘


┌──────────────────────────┐
│   ProfileActivity        │
│                          │
│  + onCreate()            │
│  + loadUserData()        │───────┐
└──────────────────────────┘       │
                                   │ checks
                                   ▼
                        ┌──────────────────┐
                        │   HelperClass    │
                        │                  │
                        │  - isAdmin       │◄──── Firebase
                        │  - name          │
                        │  - email         │
                        │  - ...           │
                        │                  │
                        │  + getIsAdmin()  │
                        └──────────────────┘
```

## Sequence Diagram: Admin Dashboard Access

```
User          ProfileActivity    Firebase      AdminDashboard
 │                 │                │                │
 │─── login ─────►│                │                │
 │                │                │                │
 │                │── fetch user ─►│                │
 │                │                │                │
 │                │◄── user data ──│                │
 │                │  (isAdmin=true)│                │
 │                │                │                │
 │◄─ show button ─│                │                │
 │                │                │                │
 │── tap Admin ──►│                │                │
 │   Dashboard    │                │                │
 │                │                │                │
 │                │─── start ─────────────────────►│
 │                │                │                │
 │                │                │                │── verify admin ─►│
 │                │                │                │                  │
 │                │                │                │◄─ isAdmin=true ──│
 │                │                │                │
 │                │                │                │── load analytics ►│
 │                │                │                │
 │                │                │                │◄─ users count ───│
 │                │                │                │◄─ events count ──│
 │                │                │                │◄─ groups count ──│
 │                │                │                │◄─ posts count ───│
 │                │                │                │
 │                │                │                │── load events ───►│
 │                │                │                │
 │                │                │                │◄─ events list ───│
 │                │                │                │
 │◄────────────── display dashboard ───────────────│
 │                │                │                │
```

## Sequence Diagram: Delete Event

```
Admin      AdminDashboard    Firebase
 │               │              │
 │─ tap delete ─►│              │
 │               │              │
 │◄─ confirm ────│              │
 │   dialog      │              │
 │               │              │
 │─ confirm ────►│              │
 │               │              │
 │               │─ delete ────►│
 │               │  event       │
 │               │              │
 │               │◄─ success ───│
 │               │              │
 │               │─ refresh ───►│
 │               │  analytics   │
 │               │              │
 │               │◄─ new count ─│
 │               │              │
 │◄─ update UI ──│              │
 │               │              │
```

## File Structure

```
CareCrew/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/societal/carecrew/
│   │   │   │   ├── AdminDashboardActivity.java (NEW)
│   │   │   │   ├── Event.java (NEW)
│   │   │   │   ├── EventAdapter.java (NEW)
│   │   │   │   ├── HelperClass.java (MODIFIED - added isAdmin)
│   │   │   │   ├── ProfileActivity.java (MODIFIED - added button)
│   │   │   │   └── ...
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_admin_dashboard.xml (NEW)
│   │   │   │   │   ├── item_event_admin.xml (NEW)
│   │   │   │   │   ├── activity_profile.xml (MODIFIED)
│   │   │   │   │   └── ...
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml (MODIFIED)
│   │   │   │   │   └── ...
│   │   │   │   └── ...
│   │   │   └── AndroidManifest.xml (MODIFIED)
│   │   └── test/
│   │       └── java/com/societal/carecrew/
│   │           ├── EventTest.java (NEW)
│   │           └── ...
│   └── build.gradle (MODIFIED)
├── ADMIN_DASHBOARD_DOCUMENTATION.md (NEW)
└── ...
```
