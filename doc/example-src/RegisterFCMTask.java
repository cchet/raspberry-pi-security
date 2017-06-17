  private class RegisterFCMTask extends AsyncTask<Void, Void, Boolean> {
    private final String fcmToken = FirebaseInstanceId.getInstance().getToken();

    @Override
    protected Boolean doInBackground(Void... params) {
      try {
        getAuthClientApi(oAuthCredentials.getUserName(), oAuthCredentials.getPassword()).registerFCMTokenUsingPUT(getGeneratedUUID(), fcmToken);
        return true;
      } catch (ApiException e) {
        // thrown in case of null parameter or if required or 200 > status > 300
        return false;
      }
    }
  }