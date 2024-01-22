import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeSeek() {
  return fetch(query)({});
}

const query = gql`
  mutation challengeSeek {
    challengeSeek {
      expireAt
      host {
        id
        username
        rating
      }
    }
  }
`;
