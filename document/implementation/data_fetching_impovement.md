# improve /stock endpoint

# requirement
- Add redis to cache the request from GET /stock. 
- Key will be hash of request
- Value will be list of vehicles.
- Set TTL is 5 mins + offset, offset range from -10s to +10s
- Any vehicle update/add in database will invalidation all caches