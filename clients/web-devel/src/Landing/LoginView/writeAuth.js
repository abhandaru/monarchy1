import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeAuth(q) {
  return fetch(query)({ q });
}

const query = gql`
  mutation Auth($q: AuthQuery!) {
    auth(q: $q)
  }
`;
