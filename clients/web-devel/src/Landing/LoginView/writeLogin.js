import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeAuth(q) {
  return fetch(query)({ q });
}

const query = gql`
  mutation Login($q: LoginQuery!) {
    login(q: $q) {
      loggedIn
      bearerToken
      userId
      user {
        id
        username
      }
    }
  }
`;
