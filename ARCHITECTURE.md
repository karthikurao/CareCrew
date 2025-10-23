# Google Maps Integration Architecture

## Component Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                     Care Crew Application                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌────────────────┐          ┌─────────────────────────────┐   │
│  │  MapsActivity  │────────▶ │    MapViewActivity          │   │
│  │  (Tab Layout)  │          │  - Google Maps Display      │   │
│  └────────────────┘          │  - Marker Management        │   │
│         │                     │  - Location Tracking        │   │
│         │                     └─────────────────────────────┘   │
│         │                                ▲                       │
│         │                                │                       │
│         ▼                                │                       │
│  ┌────────────────────────────┐         │                       │
│  │ AddOpportunityActivity     │─────────┘                       │
│  │  - Location Capture        │                                 │
│  │  - Geocoding              │                                 │
│  │  - Form Validation        │                                 │
│  └────────────────────────────┘                                 │
│         │                                                        │
└─────────┼────────────────────────────────────────────────────────┘
          │
          ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Data Layer                                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────────┐         ┌─────────────────────────────┐  │
│  │  Opportunity     │         │      HelperClass            │  │
│  │  Model           │         │      (User Model)           │  │
│  │  ──────────────  │         │      ──────────────         │  │
│  │  - title         │         │      - name                 │  │
│  │  - description   │         │      - email                │  │
│  │  - date          │         │      - location             │  │
│  │  - location      │         │      - latitude ⭐         │  │
│  │  - latitude ⭐   │         │      - longitude ⭐        │  │
│  │  - longitude ⭐  │         │      ... (other fields)     │  │
│  │  - category      │         └─────────────────────────────┘  │
│  └──────────────────┘                                           │
│                                                                   │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                  Firebase Realtime Database                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  users/                           opportunities/                 │
│    {userId}/                        {opportunityId}/            │
│      name: "John Doe"                 title: "Food Drive"       │
│      latitude: 37.7749                latitude: 37.7749          │
│      longitude: -122.4194             longitude: -122.4194      │
│      ...                               ...                       │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│               External Services                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌───────────────────────────┐  ┌─────────────────────────────┐│
│  │  Google Maps SDK          │  │  Location Services          ││
│  │  - Map Display            │  │  - GPS Provider            ││
│  │  - Markers                │  │  - Network Provider        ││
│  │  - Camera Control         │  │  - Fused Location          ││
│  └───────────────────────────┘  └─────────────────────────────┘│
│                                                                   │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │  Geocoding API (Optional)                                  │ │
│  │  - Address ⟷ Coordinates Conversion                      │ │
│  └────────────────────────────────────────────────────────────┘ │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘

⭐ = New fields added in this implementation
```

## Data Flow Diagram

### 1. Loading Map with Markers

```
User Opens MapViewActivity
        │
        ▼
Request Location Permission
        │
        ├─── Granted ───┐
        │               │
        ▼               ▼
Get Current Location  Enable "My Location"
        │               on Map
        ▼
Update User Location
   in Firebase
        │
        ▼
┌───────────────────────────────┐
│  Firebase Real-time Listeners │
├───────────────────────────────┤
│  1. Listen to users/          │
│  2. Listen to opportunities/  │
└───────────────────────────────┘
        │
        ▼
For Each Data Snapshot
        │
        ├─── Volunteers ────▶ Add Blue Marker
        │                      at (lat, lng)
        │
        └─── Opportunities ──▶ Add Red Marker
                               at (lat, lng)
```

### 2. Creating Opportunity with Location

```
User Opens AddOpportunityActivity
        │
        ▼
Fill Opportunity Details
  (Title, Description, Date)
        │
        ▼
Tap "Use Current Location"
        │
        ▼
Request Location Permission
        │
        ├─── Granted ───┐
        │               │
        ▼               ▼
Get GPS Location    Display Coordinates
        │
        ▼
┌─────────────────────┐
│  Geocoding API      │
│  (if enabled)       │
│  Lat,Lng → Address  │
└─────────────────────┘
        │
        ▼
Display Address in Form
        │
        ▼
User Taps "Create Opportunity"
        │
        ▼
Validate All Fields
  (Including Coordinates)
        │
        ▼
Create Opportunity Object
  with lat, lng
        │
        ▼
Save to Firebase
  opportunities/{id}
        │
        ▼
Success! Return to Map
        │
        ▼
New Marker Appears on Map
  (via Real-time Listener)
```

## Marker Legend

| Marker Color | Represents | Icon |
|--------------|------------|------|
| 🔵 Blue | Active Volunteers | DefaultMarker(HUE_BLUE) |
| 🔴 Red | Community Needs/Opportunities | DefaultMarker(HUE_RED) |
| 📍 Blue Dot | Current User Location | MyLocation indicator |

## Permission Flow

```
App Starts MapViewActivity
        │
        ▼
Check if Location Permission Granted?
        │
        ├─── YES ──┐
        │          │
        ▼          ▼
   Request     Enable Location
  Permission   Features
        │          │
        ▼          │
User Response      │
        │          │
  ├─Granted───────┘
  │
  └─Denied────▶ Show Toast
                "Location permission denied"
                Map still visible but no location
```

## File Structure

```
CareCrew/
├── app/
│   ├── src/main/
│   │   ├── java/com/societal/carecrew/
│   │   │   ├── MapViewActivity.java ⭐ (Enhanced)
│   │   │   ├── MapsActivity.java (Navigation container)
│   │   │   ├── AddOpportunityActivity.java ⭐ (Enhanced)
│   │   │   ├── Opportunity.java ⭐ (Enhanced with lat/lng)
│   │   │   └── HelperClass.java ⭐ (Enhanced with lat/lng)
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_map_view.xml ⭐ (Map fragment)
│   │   │   │   └── activity_add_opportunity.xml ⭐ (Location UI)
│   │   │   └── values/
│   │   │       └── strings.xml ⭐ (API key)
│   │   └── AndroidManifest.xml ⭐ (Permissions, API key)
│   └── build.gradle.kts ⭐ (Dependencies)
├── MAPS_SETUP.md ⭐ (New)
├── IMPLEMENTATION_SUMMARY.md ⭐ (New)
└── README.md ⭐ (Updated)

⭐ = Modified or new in this implementation
```

## Security Architecture

```
┌─────────────────────────────────────┐
│  Application                         │
│  ┌─────────────────────────────┐   │
│  │  Google Maps API Key        │   │
│  │  (Restricted)               │   │
│  │  ─────────────────          │   │
│  │  - Package Name Filter      │   │
│  │  - SHA-1 Fingerprint        │   │
│  │  - API Usage Quotas         │   │
│  └─────────────────────────────┘   │
│                                      │
│  ┌─────────────────────────────┐   │
│  │  Location Permissions       │   │
│  │  ─────────────────          │   │
│  │  - Runtime Requests         │   │
│  │  - Permission Checks        │   │
│  │  - Graceful Degradation     │   │
│  └─────────────────────────────┘   │
│                                      │
│  ┌─────────────────────────────┐   │
│  │  Firebase Security          │   │
│  │  ─────────────────          │   │
│  │  - Auth Required            │   │
│  │  - Input Validation         │   │
│  │  - Data Sanitization        │   │
│  └─────────────────────────────┘   │
└─────────────────────────────────────┘
```

## Integration Points

### 1. Firebase Realtime Database
- **Read**: Real-time listeners for users and opportunities
- **Write**: Update user location, create opportunities
- **Structure**: Denormalized for efficient reads

### 2. Google Maps SDK
- **Display**: Interactive map with markers
- **Interaction**: Tap markers for info windows
- **Camera**: Auto-position to user location

### 3. Google Play Services Location
- **Provider**: Fused Location Provider (best accuracy)
- **Updates**: One-time location fetch
- **Permissions**: Runtime permission model

### 4. Geocoding API (Optional)
- **Forward**: Address → Coordinates
- **Reverse**: Coordinates → Address
- **Usage**: Opportunity creation flow

## Future Enhancements Architecture

```
┌────────────────────────────────────────────┐
│  Phase 2: Enhanced Features                │
├────────────────────────────────────────────┤
│                                             │
│  ┌─────────────────────────────────────┐  │
│  │  Geofencing Service                 │  │
│  │  - Monitor location changes         │  │
│  │  - Trigger notifications            │  │
│  │  - Background location updates      │  │
│  └─────────────────────────────────────┘  │
│                                             │
│  ┌─────────────────────────────────────┐  │
│  │  Marker Clustering                  │  │
│  │  - Group nearby markers             │  │
│  │  - Improve performance              │  │
│  │  - Better UX for dense areas        │  │
│  └─────────────────────────────────────┘  │
│                                             │
│  ┌─────────────────────────────────────┐  │
│  │  Search & Filter                    │  │
│  │  - Search by distance               │  │
│  │  - Filter by category               │  │
│  │  - Sort by proximity                │  │
│  └─────────────────────────────────────┘  │
│                                             │
└────────────────────────────────────────────┘
```

## Performance Considerations

### Optimization Strategies
1. **Firebase Listeners**: Attach/detach based on lifecycle
2. **Marker Updates**: Clear and redraw only when needed
3. **Location Updates**: Use one-time fetch (not continuous)
4. **Map Rendering**: Use appropriate zoom levels
5. **Data Loading**: Implement pagination for large datasets

### Resource Management
- Battery: Minimal impact (one-time location)
- Network: Firebase real-time updates efficient
- Memory: Markers loaded on-demand
- Storage: Minimal local data caching

## Testing Strategy

### Unit Tests
- Opportunity model with coordinates
- HelperClass model with coordinates
- Input validation logic

### Integration Tests
- Firebase read/write operations
- Location permission flow
- Geocoding integration

### UI Tests
- Map display and interaction
- Marker placement
- Form submission flow

### Manual Tests
- Different devices and screen sizes
- Various GPS signal strengths
- Network connectivity variations
- Permission grant/deny scenarios

---

*This architecture supports the current implementation and provides foundation for future enhancements.*
