  private class ClientLoginOAuthTask extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... params) {
      try {
        if (oAuthCredentials != null) {

          try {
            at.rpisec.swagger.client.auth.model.TokenResponse res = getAuthClientApi(oAuthCredentials.getUserName(), oAuthCredentials.getPassword()).loginUsingGET(getGeneratedUUID());
            oAuthCredentials.setToken(res.getToken());
            oAuthCredentials.setClientId(res.getClientId());
            oAuthCredentials.setClientSecret(res.getClientSecret());

            Log.v(DEBUG_LOGIN_TAG, "[ClientId] " + res.getClientId());
            Log.v(DEBUG_LOGIN_TAG, "[ClientSecret] " + res.getClientSecret());
            return true;
            // thrown in case of null parameter or if required or 200 > status > 300
          } catch (ApiException e) {
            Log.v(DEBUG_LOGIN_TAG, e.getMessage());
          }
        }
      } catch (Exception e) {
        Log.v(DEBUG_LOGIN_TAG, e.getMessage());
      }
      return false;
    }
  }