import * as frags from '~/api/fragments';
import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function fetchSelf() {
  return fetch(query)({});
}

const query = gql`
  query self {
    self {
      ${frags.User}
    }
  }
`;
