# Firebase Security Rules for Groups Feature

## Recommended Realtime Database Rules

To secure the groups feature, add these rules to your Firebase Realtime Database:

```json
{
  "rules": {
    "groups": {
      ".read": "auth != null",
      "$groupId": {
        ".write": "!data.exists() || data.child('createdBy').val() == auth.uid",
        "members": {
          "$memberId": {
            ".write": "auth != null && ($memberId == auth.uid || root.child('groups').child($groupId).child('createdBy').val() == auth.uid)"
          }
        },
        "createdBy": {
          ".validate": "newData.val() == auth.uid || data.val() == newData.val()"
        }
      }
    },
    "users": {
      "$userId": {
        ".read": "auth != null",
        ".write": "auth != null && auth.uid == $userId"
      }
    }
  }
}
```

## Rule Explanations

### Groups Read Access
```json
".read": "auth != null"
```
- Any authenticated user can read all groups
- This allows browsing all available groups

### Groups Write Access
```json
".write": "!data.exists() || data.child('createdBy').val() == auth.uid"
```
- New groups can be created by any authenticated user
- Existing groups can only be modified by their creator
- Prevents unauthorized group modifications

### Members Management
```json
"$memberId": {
  ".write": "auth != null && ($memberId == auth.uid || root.child('groups').child($groupId).child('createdBy').val() == auth.uid)"
}
```
- Users can add/remove themselves as members
- Group creators can add/remove any member
- Prevents unauthorized member list manipulation

### Creator Protection
```json
"createdBy": {
  ".validate": "newData.val() == auth.uid || data.val() == newData.val()"
}
```
- Only the authenticated user can set themselves as creator
- Creator cannot be changed after group creation
- Prevents creator impersonation

### Users Access
```json
".read": "auth != null",
".write": "auth != null && auth.uid == $userId"
```
- All authenticated users can read user profiles (for member list)
- Users can only modify their own profile
- Protects user data privacy

## Testing the Rules

### Test Read Access
1. Try to read groups while logged out (should fail)
2. Try to read groups while logged in (should succeed)

### Test Write Access
1. Create a new group (should succeed as creator)
2. Try to edit another user's group (should fail)
3. Try to edit your own group (should succeed)

### Test Member Management
1. Join a group (should succeed)
2. Leave a group (should succeed)
3. Try to remove another user from a group you didn't create (should fail)

## Additional Security Considerations

### Prevent Creator from Leaving
The app implements this check, but for extra security, you could add:
```json
"members": {
  ".validate": "root.child('groups').child($groupId).child('createdBy').val() != newData.key() || newData.exists()"
}
```

### Limit Group Creation
To prevent spam, you could add rate limiting or require certain user permissions:
```json
".write": "auth != null && (!data.exists() && auth.token.email_verified || data.child('createdBy').val() == auth.uid)"
```

## Deployment

1. Go to Firebase Console
2. Navigate to Realtime Database
3. Click on "Rules" tab
4. Paste the rules
5. Click "Publish"
6. Test thoroughly before production deployment

## Important Notes

- These rules assume you're using Firebase Authentication
- Test all rules in the Firebase Console's rules simulator
- Monitor security rule violations in Firebase Console
- Consider adding rules for data validation (group name length, etc.)
- Update rules as new features are added
