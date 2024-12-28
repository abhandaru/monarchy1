import * as frags from '~/api/fragments';
import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeLogin(q) {
  return fetch(query)({ q });
}

const query = gql`
  mutation Login($q: LoginQuery!) {
    login(q: $q) {
      loggedIn
      bearerToken
      userId
      user {
        ${frags.User}
      }
    }
  }
`;
