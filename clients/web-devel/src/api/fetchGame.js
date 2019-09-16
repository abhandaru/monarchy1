import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function fetchGame(id) {
  return fetch(query)({ id });
}

const query = gql`
  query fetchGame($id: String!) {
    game(id: $id) {
      id
      status
      players {
        status
        user {
          id
          username
          rating
        }
      }
    }
  }
`;
