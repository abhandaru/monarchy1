import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeLoginStart(q) {
  return fetch(query)({ q });
}

const query = gql`
  mutation LoginStart($q: LoginStartQuery!) {
    loginStart(q: $q)
  }
`;
