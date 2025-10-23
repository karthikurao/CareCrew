# Firebase Realtime Database Structure

This document describes the database structure for the Care Crew volunteer application.

## Overview

The database is organized into several top-level nodes, each serving a specific purpose:

```
carecrew-database/
├── users/
├── groups/
├── posts/
├── comments/
├── opportunities/
├── chat/
├── leaderboard/
└── userGroups/
```

## Detailed Structure

### 1. Users Node (`/users`)

Stores user profile and activity information.

```json
{
  "users": {
    "{userId}": {
      "name": "John Doe",
      "email": "john@example.com",
      "username": "johndoe",
      "profileImageUrl": "https://...",
      "coverImageUrl": "https://...",
      "bio": "Passionate volunteer",
      "aboutMe": "I love helping my community",
      "location": "San Francisco, CA",
      "hoursVolunteered": 150,
      "opportunitiesParticipated": 12,
      "groupsJoined": 3,
      "skills": ["First Aid", "Teaching", "Cooking"],
      "interests": ["Education", "Environment", "Healthcare"],
      "causes": ["Environmental Conservation", "Education"],
      "availability": {
        "weekdays": true,
        "weekends": true,
        "mornings": true,
        "afternoons": false,
        "evenings": true
      },
      "volunteerExperience": [
        {
          "organization": "Red Cross",
          "role": "Volunteer Coordinator",
          "duration": "2020-2023",
          "description": "Coordinated disaster relief efforts"
        }
      ],
      "socialLinks": {
        "linkedin": "https://linkedin.com/in/johndoe",
        "twitter": "https://twitter.com/johndoe"
      },
      "createdAt": 1234567890,
      "lastActive": 1234567890
    }
  }
}
```

**Note:** Passwords are managed by Firebase Authentication and should NOT be stored in the database.

### 2. Groups Node (`/groups`)

Stores volunteer group information.

```json
{
  "groups": {
    "{groupId}": {
      "groupId": "{groupId}",
      "name": "Beach Cleanup Crew",
      "description": "Weekly beach cleanup activities",
      "createdBy": "{userId}",
      "createdAt": 1234567890,
      "members": {
        "{userId1}": true,
        "{userId2}": true
      },
      "memberCount": 2,
      "category": "Environment",
      "location": "Santa Monica Beach",
      "imageUrl": "https://..."
    }
  }
}
```

### 3. Posts Node (`/posts`)

Stores community posts and updates.

```json
{
  "posts": {
    "{postId}": {
      "postId": "{postId}",
      "uid": "{userId}",
      "username": "johndoe",
      "caption": "Great volunteering session today!",
      "imageUrl": "https://...",
      "timestamp": 1234567890,
      "likesCount": 15,
      "commentsCount": 3,
      "groupId": "{groupId}",
      "likes": {
        "{userId1}": true,
        "{userId2}": true
      }
    }
  }
}
```

### 4. Comments Node (`/comments`)

Stores comments on posts.

```json
{
  "comments": {
    "{postId}": {
      "{commentId}": {
        "commentId": "{commentId}",
        "postId": "{postId}",
        "userId": "{userId}",
        "username": "janedoe",
        "profileImageUrl": "https://...",
        "commentText": "Amazing work!",
        "timestamp": 1234567890
      }
    }
  }
}
```

### 5. Opportunities Node (`/opportunities`)

Stores volunteer opportunities.

```json
{
  "opportunities": {
    "{opportunityId}": {
      "opportunityId": "{opportunityId}",
      "title": "Food Bank Distribution",
      "description": "Help distribute food to families in need",
      "date": "2024-03-15",
      "startTime": "09:00",
      "endTime": "17:00",
      "location": "Community Center, 123 Main St",
      "category": "Food & Hunger",
      "createdBy": "{userId}",
      "groupId": "{groupId}",
      "maxVolunteers": 20,
      "currentVolunteers": 8,
      "participants": {
        "{userId1}": true,
        "{userId2}": true
      },
      "status": "active",
      "imageUrl": "https://...",
      "contactPerson": "John Doe",
      "contactEmail": "john@example.com",
      "contactPhone": "+1234567890",
      "createdAt": 1234567890
    }
  }
}
```

### 6. Chat Node (`/chat`)

Stores chat conversations and messages.

```json
{
  "chat": {
    "conversations": {
      "{conversationId}": {
        "conversationId": "{conversationId}",
        "type": "group",
        "groupId": "{groupId}",
        "name": "Beach Cleanup Crew Chat",
        "participants": {
          "{userId1}": true,
          "{userId2}": true
        },
        "lastMessage": {
          "text": "See you tomorrow!",
          "senderId": "{userId}",
          "timestamp": 1234567890
        },
        "createdAt": 1234567890,
        "updatedAt": 1234567890
      }
    },
    "messages": {
      "{conversationId}": {
        "{messageId}": {
          "messageId": "{messageId}",
          "conversationId": "{conversationId}",
          "senderId": "{userId}",
          "senderName": "John Doe",
          "senderImageUrl": "https://...",
          "text": "Hello everyone!",
          "timestamp": 1234567890,
          "type": "text",
          "imageUrl": "",
          "readBy": {
            "{userId1}": 1234567890,
            "{userId2}": 1234567891
          }
        }
      }
    },
    "userConversations": {
      "{userId}": {
        "{conversationId}": {
          "lastRead": 1234567890,
          "unreadCount": 2
        }
      }
    }
  }
}
```

### 7. Leaderboard Node (`/leaderboard`)

Stores user rankings and achievements.

```json
{
  "leaderboard": {
    "allTime": {
      "{userId}": {
        "userId": "{userId}",
        "username": "johndoe",
        "name": "John Doe",
        "profileImageUrl": "https://...",
        "hoursVolunteered": 150,
        "opportunitiesParticipated": 12,
        "groupsJoined": 3,
        "score": 500,
        "rank": 1,
        "badges": ["Super Volunteer", "Team Player", "Early Adopter"],
        "updatedAt": 1234567890
      }
    },
    "monthly": {
      "2024-03": {
        "{userId}": {
          "userId": "{userId}",
          "username": "johndoe",
          "name": "John Doe",
          "profileImageUrl": "https://...",
          "hoursVolunteered": 25,
          "opportunitiesParticipated": 3,
          "score": 85,
          "rank": 1
        }
      }
    },
    "weekly": {
      "2024-W11": {
        "{userId}": {
          "userId": "{userId}",
          "username": "johndoe",
          "name": "John Doe",
          "profileImageUrl": "https://...",
          "hoursVolunteered": 8,
          "opportunitiesParticipated": 2,
          "score": 28,
          "rank": 1
        }
      }
    }
  }
}
```

### 8. UserGroups Node (`/userGroups`)

Maintains a denormalized index for efficient querying of user's groups.

```json
{
  "userGroups": {
    "{userId}": {
      "{groupId}": {
        "groupId": "{groupId}",
        "name": "Beach Cleanup Crew",
        "role": "member",
        "joinedAt": 1234567890
      }
    }
  }
}
```

## Data Relationships

### Many-to-Many Relationships

1. **Users ↔ Groups**: Managed through `groups/{groupId}/members` and `userGroups/{userId}`
2. **Users ↔ Opportunities**: Managed through `opportunities/{opportunityId}/participants`
3. **Users ↔ Posts (Likes)**: Managed through `posts/{postId}/likes`

### One-to-Many Relationships

1. **Users → Posts**: One user can create many posts
2. **Posts → Comments**: One post can have many comments
3. **Groups → Posts**: One group can have many posts
4. **Users → Volunteer Experiences**: One user can have many volunteer experiences

## Indexing Recommendations

For optimal performance, create indexes on:

1. **Posts**: Index on `timestamp` for recent posts
2. **Opportunities**: Index on `date` and `status` for active opportunities
3. **Leaderboard**: Index on `score` for ranking queries
4. **Chat Messages**: Index on `timestamp` for chronological ordering

## Best Practices

### Data Denormalization

To improve read performance, certain data is duplicated:

1. **User Info in Posts**: Store `username` and user profile data with posts to avoid extra lookups
2. **Group Info in UserGroups**: Cache group name for quick access
3. **Last Message in Conversations**: Store last message details for chat list display
4. **Counts**: Store `memberCount`, `likesCount`, `commentsCount` for efficient display

### Data Consistency

When updating denormalized data, ensure updates are made to all locations:

1. When a user updates their username, update it in:
   - `/users/{userId}/username`
   - All posts by that user
   - All comments by that user
   - Leaderboard entries

2. When a post receives a like:
   - Add to `/posts/{postId}/likes/{userId}`
   - Increment `/posts/{postId}/likesCount`

### Security Considerations

1. Never store passwords in the database (use Firebase Authentication)
2. Validate all user inputs before writing to the database
3. Use Firebase Security Rules to enforce access control
4. Implement rate limiting for write operations
5. Sanitize user-generated content before display

## Scoring System

The leaderboard score is calculated as:

```
score = (hoursVolunteered * 10) + (opportunitiesParticipated * 20) + (groupsJoined * 15)
```

This can be adjusted based on application requirements.

## Data Cleanup

Consider implementing automated cleanup for:

1. Old chat messages (archive messages older than 1 year)
2. Expired opportunities (archive opportunities past their date)
3. Inactive user data (flag users inactive for more than 1 year)

## Migration Notes

If migrating from an existing database structure:

1. Back up all existing data
2. Create migration scripts to transform data to new structure
3. Test migration on a clone of the database
4. Plan for minimal downtime during migration
5. Verify data integrity after migration

## Future Enhancements

Potential additions to the database structure:

1. **Notifications Node**: Store user notifications
2. **Events Node**: Separate events from opportunities for recurring activities
3. **Achievements Node**: Detailed achievement tracking system
4. **Reports Node**: User-generated reports for issues
5. **Feedback Node**: User feedback on opportunities and groups
