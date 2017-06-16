  // incomming messages from firebase messaging service
  public void onMessageReceived(RemoteMessage remoteMessage) {
    if (remoteMessage.getData().size() > 0 && !lastIncidentKey.equals(remoteMessage.getData().get(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_ID))) {
      lastIncidentKey = remoteMessage.getData().get(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_ID);
      processData(remoteMessage.getData());
    }
  }

  private void processData(final Map<String, String> data) {
    if (data.containsKey(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_ID)) {
      // get database instance
      FirebaseDatabase database = FirebaseDatabase.getInstance();
      
      if (database != null) {
        DatabaseReference incidentDBRef = database.getReference(FirebaseConstants.DB_ITEM_INCIDENT);
        
        if (incidentDBRef != null) {
          // get item data
          Query incidentQuerry = incidentDBRef.child(data.get(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_ID));
          
          if (incidentQuerry != null) {
            // async listener - wait for query finished
            incidentQuerry.addListenerForSingleValueEvent(new ValueEventListener() {
              
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseDatabaseItem item = dataSnapshot.getValue(FirebaseDatabaseItem.class);
                if (item != null) {

                  String imageFileName = data.get(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_ID) + "." + item.getDataType();
                  File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                  byte[] imageAsBytes = Base64.decode(item.getBase64Data().getBytes(), Base64.DEFAULT);
                  Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                  // ...
                  // save imagge into directory
                  // ...

                  // send notification
                  sendNotification(msgTitle, msgBody);
                }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
            });
          }
        }
      }
    }
  }