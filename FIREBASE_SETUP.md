# Firebase Realtime Database Setup Guide

This guide will help you set up the Firebase Realtime Database for the Care Crew application.

## Prerequisites

1. Firebase account (create one at [https://firebase.google.com/](https://firebase.google.com/))
2. Firebase project created for Care Crew
3. `google-services.json` file already placed in the `app/` directory

## Database Structure

The complete database structure is documented in [DATABASE_STRUCTURE.md](DATABASE_STRUCTURE.md). Please review it to understand how data is organized.

## Step 1: Enable Realtime Database

1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Select your Care Crew project
3. Navigate to **Build** → **Realtime Database**
4. Click **Create Database**
5. Choose a database location (select the closest to your users)
6. Start in **Test mode** for development (we'll secure it in the next step)

## Step 2: Apply Security Rules

Security rules protect your database from unauthorized access. Follow these steps:

1. In the Firebase Console, go to **Realtime Database** → **Rules** tab
2. Copy the contents of `database.rules.json` from this repository
3. Paste them into the Rules editor in Firebase Console
4. Click **Publish** to apply the rules

### Security Rules Summary

The rules implement:

- **Read Access**: 
  - Users can read all user profiles, groups, posts, comments, opportunities, and leaderboard
  - Chat messages are only readable by conversation participants
  
- **Write Access**:
  - Users can only update their own profile data
  - Group members can update group information
  - Post/comment authors can edit their own content
  - Chat participants can send messages to their conversations
  - Users can join/leave groups and opportunities

- **Validation**:
  - Required fields are enforced
  - Data types are validated
  - String lengths are limited to prevent abuse
  - Email format is validated

## Step 3: Initialize Database Structure (Optional)

For development/testing, you can pre-populate some data:

### Using Firebase Console

1. Go to **Realtime Database** → **Data** tab
2. Click on the database root node
3. Add child nodes for: `users`, `groups`, `posts`, `comments`, `opportunities`, `chat`, `leaderboard`, `userGroups`

### Using the REST API

You can use the Firebase REST API to initialize data programmatically.

## Step 4: Configure Indexing (Recommended)

For optimal performance, add these indexes to your database:

1. Go to **Realtime Database** → **Rules** tab
2. Add these index definitions at the root level:

```json
{
  "rules": {
    // ... existing rules ...
  },
  ".indexOn": {
    "posts": ["timestamp"],
    "opportunities": ["date", "status"],
    "leaderboard/allTime": ["score"],
    "leaderboard/monthly": ["score"],
    "leaderboard/weekly": ["score"]
  }
}
```

Or add indexes on-demand when Firebase suggests them in your app logs.

## Step 5: Set Database URL in App

The database URL is automatically configured through the `google-services.json` file. No additional configuration needed in the app code.

## Testing the Database

### Test User Creation

After signup, verify that user data is stored correctly:

1. Go to Firebase Console → Realtime Database → Data
2. Navigate to `users/{userId}`
3. Verify all user fields are present

### Test Group Creation

1. Create a group in the app
2. Check `groups/{groupId}` in Firebase Console
3. Verify the creator is in the `members` list
4. Check that `userGroups/{userId}/{groupId}` was created

### Test Post Creation

1. Create a post in the app
2. Check `posts/{postId}` in Firebase Console
3. Verify all fields are populated correctly

## Database Backup

### Automated Backups

Firebase automatically backs up Realtime Database data. To configure automated daily backups:

1. Go to Firebase Console → Realtime Database
2. Click the three-dot menu → **Backups**
3. Enable automated backups

### Manual Export

To manually export database data:

1. Go to Firebase Console → Realtime Database → Data
2. Click the three-dot menu on the root node
3. Select **Export JSON**
4. Save the file for backup

## Monitoring

### View Usage

Monitor database usage at Firebase Console → Realtime Database → Usage tab:

- Storage size
- Downloads (read operations)
- Connections
- Bandwidth

### Set Up Alerts

1. Go to Firebase Console → Project Settings → Integrations
2. Enable alerts for:
   - High database usage
   - Bandwidth limits
   - Connection limits

## Common Issues and Solutions

### Issue: Permission Denied Error

**Cause**: Security rules are too restrictive or user is not authenticated.

**Solution**: 
1. Verify the user is logged in (Firebase Authentication)
2. Check the security rules match the expected user ID
3. Review the specific rule that's failing in the error message

### Issue: Data Not Syncing

**Cause**: Offline mode or network issues.

**Solution**:
1. Check internet connection
2. Verify Firebase is initialized in the app
3. Enable Firebase persistence for offline support:

```java
FirebaseDatabase.getInstance().setPersistenceEnabled(true);
```

### Issue: Slow Queries

**Cause**: Missing indexes or inefficient data structure.

**Solution**:
1. Add indexes as suggested by Firebase
2. Review [DATABASE_STRUCTURE.md](DATABASE_STRUCTURE.md) for denormalization patterns
3. Use Firebase Performance Monitoring to identify bottlenecks

## Data Migration

If you need to migrate existing data to this new structure:

1. **Backup**: Export current database
2. **Create Migration Script**: Transform data to match new structure
3. **Test**: Run migration on a test database first
4. **Execute**: Apply migration to production database
5. **Verify**: Check data integrity after migration

## Best Practices

1. **Never Store Passwords**: Use Firebase Authentication only
2. **Validate Input**: Always validate data before writing to database
3. **Use Transactions**: For operations that update multiple fields (like likes count)
4. **Denormalize Wisely**: Balance between read performance and data consistency
5. **Monitor Costs**: Keep an eye on read/write operations and bandwidth
6. **Regular Backups**: Set up automated daily backups
7. **Test Rules**: Use the Rules Simulator in Firebase Console to test security rules

## Firebase Realtime Database Limits

Be aware of these limits:

- Maximum depth: 32 levels
- Maximum size of a single write: 256 MB
- Maximum size of a database: 1 GB (Spark plan), unlimited (Blaze plan)
- Simultaneous connections: 100,000 (Blaze plan)

## Upgrading to Firestore (Future)

If the app grows significantly, consider migrating to Cloud Firestore for:

- Better querying capabilities
- Automatic scaling
- Better offline support
- More flexible data model

## Support

For Firebase-specific issues:

- [Firebase Documentation](https://firebase.google.com/docs/database)
- [Firebase Support](https://firebase.google.com/support)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/firebase-realtime-database)

For Care Crew app-specific issues, refer to the main [README.md](README.md).

## Security Checklist

Before going to production:

- [ ] Security rules are applied and tested
- [ ] All sensitive data (passwords) is handled by Firebase Authentication
- [ ] Indexes are configured for frequently queried data
- [ ] Automated backups are enabled
- [ ] Usage alerts are configured
- [ ] SSL/TLS is enforced (enabled by default in Firebase)
- [ ] Database URL is not exposed in public repositories
- [ ] Service account credentials are secure

## Reference Files

- [DATABASE_STRUCTURE.md](DATABASE_STRUCTURE.md) - Complete database schema documentation
- [database.rules.json](database.rules.json) - Firebase security rules
- [README.md](README.md) - Main application README
