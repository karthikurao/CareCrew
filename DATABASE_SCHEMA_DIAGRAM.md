# Firebase Realtime Database Schema Diagram

This diagram provides a visual representation of the database structure and relationships.

## Database Tree Structure

```
firebase-realtime-database/
│
├── users/
│   └── {userId}/
│       ├── name: string
│       ├── email: string
│       ├── username: string
│       ├── profileImageUrl: string
│       ├── coverImageUrl: string
│       ├── bio: string
│       ├── aboutMe: string
│       ├── location: string
│       ├── hoursVolunteered: number
│       ├── opportunitiesParticipated: number
│       ├── groupsJoined: number
│       ├── skills: [string]
│       ├── interests: [string]
│       ├── causes: [string]
│       ├── availability: object
│       ├── volunteerExperience: [object]
│       └── socialLinks: object
│
├── groups/
│   └── {groupId}/
│       ├── groupId: string
│       ├── name: string
│       ├── description: string
│       ├── createdBy: userId
│       ├── createdAt: timestamp
│       ├── members/
│       │   ├── {userId}: true
│       │   └── {userId}: true
│       ├── memberCount: number
│       ├── category: string
│       ├── location: string
│       └── imageUrl: string
│
├── posts/
│   └── {postId}/
│       ├── postId: string
│       ├── uid: userId
│       ├── username: string
│       ├── caption: string
│       ├── imageUrl: string
│       ├── timestamp: number
│       ├── likesCount: number
│       ├── commentsCount: number
│       ├── groupId: string (optional)
│       └── likes/
│           ├── {userId}: true
│           └── {userId}: true
│
├── comments/
│   └── {postId}/
│       └── {commentId}/
│           ├── commentId: string
│           ├── postId: string
│           ├── userId: userId
│           ├── username: string
│           ├── profileImageUrl: string
│           ├── commentText: string
│           └── timestamp: number
│
├── opportunities/
│   └── {opportunityId}/
│       ├── opportunityId: string
│       ├── title: string
│       ├── description: string
│       ├── date: string
│       ├── startTime: string
│       ├── endTime: string
│       ├── location: string
│       ├── category: string
│       ├── createdBy: userId
│       ├── groupId: string (optional)
│       ├── maxVolunteers: number
│       ├── currentVolunteers: number
│       ├── participants/
│       │   ├── {userId}: true
│       │   └── {userId}: true
│       ├── status: string
│       ├── imageUrl: string
│       ├── contactPerson: string
│       ├── contactEmail: string
│       ├── contactPhone: string
│       └── createdAt: timestamp
│
├── chat/
│   ├── conversations/
│   │   └── {conversationId}/
│   │       ├── conversationId: string
│   │       ├── type: "group" | "direct"
│   │       ├── groupId: string (if type=group)
│   │       ├── name: string
│   │       ├── participants/
│   │       │   ├── {userId}: true
│   │       │   └── {userId}: true
│   │       ├── lastMessage: object
│   │       ├── createdAt: timestamp
│   │       └── updatedAt: timestamp
│   │
│   ├── messages/
│   │   └── {conversationId}/
│   │       └── {messageId}/
│   │           ├── messageId: string
│   │           ├── conversationId: string
│   │           ├── senderId: userId
│   │           ├── senderName: string
│   │           ├── senderImageUrl: string
│   │           ├── text: string
│   │           ├── timestamp: number
│   │           ├── type: "text" | "image"
│   │           ├── imageUrl: string (optional)
│   │           └── readBy/
│   │               ├── {userId}: timestamp
│   │               └── {userId}: timestamp
│   │
│   └── userConversations/
│       └── {userId}/
│           └── {conversationId}/
│               ├── lastRead: timestamp
│               └── unreadCount: number
│
├── leaderboard/
│   ├── allTime/
│   │   └── {userId}/
│   │       ├── userId: string
│   │       ├── username: string
│   │       ├── name: string
│   │       ├── profileImageUrl: string
│   │       ├── hoursVolunteered: number
│   │       ├── opportunitiesParticipated: number
│   │       ├── groupsJoined: number
│   │       ├── score: number
│   │       ├── rank: number
│   │       ├── badges: [string]
│   │       └── updatedAt: timestamp
│   │
│   ├── monthly/
│   │   └── {year-month}/  (e.g., "2024-03")
│   │       └── {userId}/
│   │           ├── userId: string
│   │           ├── username: string
│   │           ├── name: string
│   │           ├── profileImageUrl: string
│   │           ├── hoursVolunteered: number
│   │           ├── opportunitiesParticipated: number
│   │           ├── score: number
│   │           └── rank: number
│   │
│   └── weekly/
│       └── {year-week}/  (e.g., "2024-W11")
│           └── {userId}/
│               ├── userId: string
│               ├── username: string
│               ├── name: string
│               ├── profileImageUrl: string
│               ├── hoursVolunteered: number
│               ├── opportunitiesParticipated: number
│               ├── score: number
│               └── rank: number
│
└── userGroups/
    └── {userId}/
        └── {groupId}/
            ├── groupId: string
            ├── name: string
            ├── role: "admin" | "member"
            └── joinedAt: timestamp
```

## Entity Relationship Diagram

```
┌─────────────────┐
│     Users       │
│  (users node)   │
└────────┬────────┘
         │
         │ 1:N
         │
    ┌────┴─────┬─────────────┬──────────────┬──────────────┐
    │          │             │              │              │
    │          │             │              │              │
    ▼          ▼             ▼              ▼              ▼
┌───────┐  ┌───────┐    ┌──────────┐  ┌──────────┐  ┌──────────┐
│ Posts │  │Groups │    │Opportunities│ │Comments│  │Leaderboard│
└───┬───┘  └───┬───┘    └─────┬────┘  └────┬─────┘  └──────────┘
    │          │              │             │
    │          │ N:M          │ N:M         │
    │          │              │             │
    │     ┌────┴───┐     ┌────┴───┐        │
    │     │ User-  │     │ User-  │        │
    │     │ Groups │     │Opps    │        │
    │     └────────┘     └────────┘        │
    │                                       │
    │ 1:N                                  │ N:1
    │                                       │
    └───────────────────┬───────────────────┘
                        ▼
                   ┌─────────┐
                   │Comments │
                   └─────────┘

┌─────────────────┐
│     Groups      │
│  (groups node)  │
└────────┬────────┘
         │
         │ 1:1
         │
         ▼
    ┌─────────┐
    │  Chat   │
    │Convs.   │
    └────┬────┘
         │
         │ 1:N
         │
         ▼
    ┌─────────┐
    │Messages │
    └─────────┘
```

## Data Flow Diagrams

### User Signup Flow
```
User Signup
    │
    ├──> Firebase Auth (create account)
    │         │
    │         └──> Get userId
    │
    └──> Create user profile
              │
              └──> /users/{userId}
                        │
                        └──> Initialize stats (hours: 0, opportunities: 0, groups: 0)
```

### Group Creation Flow
```
Create Group
    │
    ├──> Generate groupId
    │
    ├──> /groups/{groupId}
    │         │
    │         ├──> Set group data
    │         └──> Add creator to members
    │
    └──> /userGroups/{userId}/{groupId}
              │
              └──> Add group reference with role "admin"
```

### Post Creation Flow
```
Create Post
    │
    ├──> Generate postId
    │
    ├──> /posts/{postId}
    │         │
    │         ├──> Set post data
    │         └──> Initialize counters (likes: 0, comments: 0)
    │
    └──> (Optional) Link to group
```

### Like Post Flow
```
Like Post
    │
    ├──> /posts/{postId}/likes/{userId} = true
    │
    └──> Increment /posts/{postId}/likesCount
              │
              └──> Use transaction for atomicity
```

### Add Comment Flow
```
Add Comment
    │
    ├──> Generate commentId
    │
    ├──> /comments/{postId}/{commentId}
    │         │
    │         └──> Set comment data
    │
    └──> Increment /posts/{postId}/commentsCount
              │
              └──> Use transaction for atomicity
```

### Send Message Flow
```
Send Message
    │
    ├──> Generate messageId
    │
    ├──> /chat/messages/{conversationId}/{messageId}
    │         │
    │         └──> Set message data
    │
    ├──> Update /chat/conversations/{conversationId}/lastMessage
    │
    └──> Update /chat/conversations/{conversationId}/updatedAt
```

### Join Opportunity Flow
```
Join Opportunity
    │
    ├──> /opportunities/{opportunityId}/participants/{userId} = true
    │
    ├──> Increment /opportunities/{opportunityId}/currentVolunteers
    │
    └──> Update user stats
              │
              ├──> Increment /users/{userId}/opportunitiesParticipated
              │
              └──> Update leaderboard
                        │
                        └──> Recalculate score and update /leaderboard/allTime/{userId}
```

## Access Patterns

### Common Queries

1. **Get user's groups**
   - Query: `/userGroups/{userId}`
   - Returns: List of groupIds
   - Then fetch: `/groups/{groupId}` for each

2. **Get recent posts**
   - Query: `/posts` ordered by `timestamp`
   - Limit: Last 20-50 posts
   - Use: `limitToLast(20)`

3. **Get group members**
   - Query: `/groups/{groupId}/members`
   - Returns: Map of userId -> true
   - Then fetch: `/users/{userId}` for each member

4. **Get leaderboard**
   - Query: `/leaderboard/allTime` ordered by `score`
   - Limit: Top 10
   - Use: `limitToLast(10)`

5. **Get unread messages**
   - Query: `/chat/userConversations/{userId}`
   - Filter: Where `unreadCount > 0`
   - Then fetch conversations

6. **Get active opportunities**
   - Query: `/opportunities` ordered by `status`
   - Filter: Where `status == "active"`
   - Secondary sort: By `date`

## Indexing Strategy

### Required Indexes

```json
{
  "posts": {
    ".indexOn": ["timestamp", "groupId"]
  },
  "opportunities": {
    ".indexOn": ["date", "status", "category"]
  },
  "leaderboard": {
    "allTime": {
      ".indexOn": ["score"]
    },
    "monthly": {
      ".indexOn": ["score"]
    },
    "weekly": {
      ".indexOn": ["score"]
    }
  },
  "chat": {
    "messages": {
      ".indexOn": ["timestamp"]
    },
    "conversations": {
      ".indexOn": ["updatedAt"]
    }
  }
}
```

## Data Denormalization Examples

### Example 1: User Info in Posts
Instead of just storing userId in posts, we also store username and profileImageUrl:

**Benefit**: Display posts without extra user lookups  
**Trade-off**: Must update all posts when user changes username/image

### Example 2: Group Info in UserGroups
Store group name along with groupId in userGroups:

**Benefit**: List user's groups without fetching each group  
**Trade-off**: Must update all userGroups when group name changes

### Example 3: Last Message in Conversations
Store the last message details in the conversation object:

**Benefit**: Display chat list without loading all messages  
**Trade-off**: Must update conversation when sending a message

## Security Model

```
User (Authenticated)
    │
    ├──> Can read: All public data (users, groups, posts, opportunities, leaderboard)
    │
    ├──> Can write: Own profile, own posts, own comments
    │
    ├──> Can join: Public groups, opportunities
    │
    └──> Can message: Only in conversations they're part of

Group Member
    │
    └──> Can write: Group data (if admin), group posts

Conversation Participant
    │
    └──> Can write: Messages in conversation
```

## Performance Considerations

1. **Limit data fetched**: Always use `limitToFirst()` or `limitToLast()`
2. **Use indexes**: For any ordered queries
3. **Denormalize reads**: Store computed values (counts, scores)
4. **Batch updates**: Use multi-path updates for atomic writes
5. **Offline support**: Enable persistence for better UX
6. **Clean old data**: Archive or delete old messages, expired opportunities

## Scaling Considerations

When the app grows:

1. **Shard leaderboards**: Split by regions or categories
2. **Archive old data**: Move old posts/messages to separate nodes
3. **Consider Firestore**: Better querying for complex use cases
4. **Add caching**: Use Cloud Functions with Redis for hot data
5. **Implement pagination**: For all lists (posts, opportunities, messages)

---

For detailed information about each node, see [DATABASE_STRUCTURE.md](DATABASE_STRUCTURE.md).  
For setup instructions, see [FIREBASE_SETUP.md](FIREBASE_SETUP.md).  
For code examples, see [DATABASE_QUICK_REFERENCE.md](DATABASE_QUICK_REFERENCE.md).
