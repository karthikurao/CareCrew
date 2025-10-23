# Groups Feature - Architecture Overview

## Component Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Firebase Backend                      │
├─────────────────────────────────────────────────────────┤
│  groups/                                                 │
│    {groupId}/                                           │
│      ├─ groupId: string                                 │
│      ├─ name: string                                    │
│      ├─ description: string                             │
│      ├─ createdBy: string (userId)                      │
│      ├─ groupImageUrl: string (optional)                │
│      └─ members/                                        │
│           ├─ userId1: true                              │
│           ├─ userId2: true                              │
│           └─ userId3: true                              │
│                                                          │
│  users/                                                  │
│    {userId}/                                            │
│      ├─ name: string                                    │
│      └─ profileImageUrl: string                         │
└─────────────────────────────────────────────────────────┘
                          ↑
                          │ Firebase SDK
                          ↓
┌─────────────────────────────────────────────────────────┐
│                 Android Application                      │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  ┌────────────────────────────────────────────────┐    │
│  │         GroupsActivity (Existing)              │    │
│  │  - Shows list of all groups                    │    │
│  │  - Create group FAB button                     │    │
│  │  - Uses GroupAdapter                           │    │
│  └────────────────┬───────────────────────────────┘    │
│                   │ onClick                             │
│                   ↓                                     │
│  ┌────────────────────────────────────────────────┐    │
│  │      GroupDetailsActivity (NEW)                │    │
│  │  ┌──────────────────────────────────────────┐ │    │
│  │  │  Group Information Section               │ │    │
│  │  │  - Group name (TextView)                 │ │    │
│  │  │  - Group description (TextView)          │ │    │
│  │  │  - Created by (TextView)                 │ │    │
│  │  └──────────────────────────────────────────┘ │    │
│  │  ┌──────────────────────────────────────────┐ │    │
│  │  │  Members Section                         │ │    │
│  │  │  - "Members" header (TextView)           │ │    │
│  │  │  - RecyclerView with MemberAdapter       │ │    │
│  │  └──────────────────────────────────────────┘ │    │
│  │  ┌──────────────────────────────────────────┐ │    │
│  │  │  Action Buttons                          │ │    │
│  │  │  - Join/Leave button (MaterialButton)    │ │    │
│  │  │  - Edit button (MaterialButton)*         │ │    │
│  │  │  - Invite button (MaterialButton)        │ │    │
│  │  │    * Only visible to creator             │ │    │
│  │  └──────────────────────────────────────────┘ │    │
│  └────────────────────────────────────────────────┘    │
│                   │ uses                                │
│                   ↓                                     │
│  ┌────────────────────────────────────────────────┐    │
│  │       MemberAdapter (NEW)                      │    │
│  │  - Displays list of group members              │    │
│  │  - Fetches user data from Firebase             │    │
│  │  - Shows profile image + name                  │    │
│  │  - Uses item_member.xml layout                 │    │
│  └────────────────────────────────────────────────┘    │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

## Data Flow

### Joining a Group

```
User                    GroupDetailsActivity         Firebase
  │                              │                       │
  │  Tap "Join Group"           │                       │
  ├────────────────────────────>│                       │
  │                              │                       │
  │                              │  Check authentication │
  │                              │───────────┐           │
  │                              │           │           │
  │                              │<──────────┘           │
  │                              │                       │
  │                              │  Update members/      │
  │                              │  {currentUserId}: true│
  │                              ├──────────────────────>│
  │                              │                       │
  │                              │      Success          │
  │                              │<──────────────────────┤
  │                              │                       │
  │  Show "Joined successfully" │                       │
  │<─────────────────────────────┤                       │
  │                              │                       │
  │  Update UI                   │  Real-time listener   │
  │  - Button → "Leave Group"    │  triggers update      │
  │  - Add to member list        │<──────────────────────┤
  │<─────────────────────────────┤                       │
```

### Leaving a Group

```
User                    GroupDetailsActivity         Firebase
  │                              │                       │
  │  Tap "Leave Group"          │                       │
  ├────────────────────────────>│                       │
  │                              │                       │
  │                              │  Check if creator     │
  │                              │───────────┐           │
  │                              │           │           │
  │                              │<──────────┘           │
  │                              │  (Not creator)        │
  │                              │                       │
  │                              │  Remove members/      │
  │                              │  {currentUserId}      │
  │                              ├──────────────────────>│
  │                              │                       │
  │                              │      Success          │
  │                              │<──────────────────────┤
  │                              │                       │
  │  Show "Left successfully"   │                       │
  │<─────────────────────────────┤                       │
  │                              │                       │
  │  Update UI                   │  Real-time listener   │
  │  - Button → "Join Group"     │  triggers update      │
  │  - Remove from member list   │<──────────────────────┤
  │<─────────────────────────────┤                       │
```

## State Management

### GroupDetailsActivity States

```
┌─────────────────────────────────────┐
│  User Authentication State          │
├─────────────────────────────────────┤
│  - Not Logged In                    │
│    → Show login prompt              │
│  - Logged In                        │
│    → Check membership status        │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│  Membership State                   │
├─────────────────────────────────────┤
│  1. Non-Member                      │
│     → Show "Join Group" button      │
│                                     │
│  2. Member (Not Creator)            │
│     → Show "Leave Group" button     │
│     → Hide "Edit" button            │
│                                     │
│  3. Member (Creator)                │
│     → Hide "Join/Leave" button      │
│     → Show "Edit" button            │
└─────────────────────────────────────┘
```

## Firebase Security Rules

```
groups/
  .read: "auth != null"
  $groupId/
    .write: "!data.exists() || data.child('createdBy').val() == auth.uid"
    members/
      $memberId/
        .write: "auth != null && 
                ($memberId == auth.uid || 
                 root.child('groups').child($groupId).child('createdBy').val() == auth.uid)"
```

## Error Handling

```
┌─────────────────────────────────────────────────────┐
│  Error Scenarios                                    │
├─────────────────────────────────────────────────────┤
│  1. Group Deleted                                   │
│     → Show toast: "Group no longer exists"          │
│     → Close activity                                │
│                                                     │
│  2. Network Error                                   │
│     → Show toast: "Failed to load group details"    │
│     → Log error                                     │
│                                                     │
│  3. User Not Found                                  │
│     → Display: "Unknown User"                       │
│     → Show placeholder image                        │
│                                                     │
│  4. Creator Tries to Leave                          │
│     → Show toast: "Creator cannot leave group"      │
│     → No database operation                         │
│                                                     │
│  5. Join/Leave Operation Fails                      │
│     → Show toast: "Failed to join/leave group"      │
│     → Log error                                     │
│     → Don't update UI                               │
└─────────────────────────────────────────────────────┘
```

## UI Components Hierarchy

```
GroupDetailsActivity
│
├── ConstraintLayout
│   ├── TextView (groupName)
│   ├── TextView (groupDescription)
│   ├── TextView (createdBy)
│   ├── TextView (membersHeader) "Members"
│   ├── RecyclerView (membersList)
│   │   └── MemberAdapter
│   │       └── item_member.xml (repeated for each member)
│   │           ├── MaterialCardView
│   │           │   └── LinearLayout
│   │           │       ├── ImageView (memberImage)
│   │           │       └── TextView (memberName)
│   ├── MaterialButton (joinLeaveButton)
│   ├── MaterialButton (editGroupButton) [Conditional]
│   └── MaterialButton (inviteMembersButton)
```

## Threading Model

```
Main Thread (UI)
  │
  ├─ onCreate()
  │   ├─ Initialize UI components
  │   └─ Set up Firebase listeners
  │
  ├─ Firebase Callbacks (run on main thread)
  │   ├─ onDataChange()
  │   │   ├─ Update UI elements
  │   │   └─ Notify adapters
  │   │
  │   └─ onCancelled()
  │       └─ Show error messages
  │
  ├─ Button Click Listeners
  │   ├─ handleJoinLeave()
  │   │   └─ Firebase write operation
  │   │
  │   ├─ Edit button (placeholder)
  │   └─ Invite button (placeholder)
  │
  └─ Glide Image Loading (background thread)
      └─ Load profile images
```

## Dependencies

```
Firebase
├── firebase-auth (Authentication)
└── firebase-database (Realtime Database)

Android Jetpack
├── androidx.appcompat (AppCompatActivity)
├── androidx.recyclerview (RecyclerView)
└── androidx.constraintlayout (ConstraintLayout)

Material Components
├── MaterialCardView
└── MaterialButton

Image Loading
└── Glide (Profile images)

Build System
└── ViewBinding (Type-safe view access)
```

## File Organization

```
app/src/main/
├── java/com/societal/carecrew/
│   ├── GroupsActivity.java (List view)
│   ├── GroupDetailsActivity.java (Detail view) ← NEW
│   ├── CreateGroupActivity.java (Create form)
│   ├── Group.java (Model)
│   ├── GroupAdapter.java (List adapter)
│   └── MemberAdapter.java (Member list adapter) ← NEW
│
├── res/layout/
│   ├── activity_groups.xml (List screen)
│   ├── activity_group_details.xml (Detail screen)
│   ├── activity_create_group.xml (Create form)
│   ├── item_group.xml (Group list item)
│   └── item_member.xml (Member list item) ← NEW
│
└── AndroidManifest.xml
    └── GroupDetailsActivity registered ✓
```

## Summary

- **Architecture**: MVVM-like with Firebase as backend
- **Data Flow**: Reactive with real-time Firebase listeners
- **State**: Managed locally, synced with Firebase
- **UI**: Material Design with ViewBinding
- **Threading**: Main thread with Firebase callbacks
- **Error Handling**: Comprehensive with user feedback
- **Security**: Firebase rules + client-side checks

---

Total Implementation: **346 lines of code** across 3 new/modified files.
