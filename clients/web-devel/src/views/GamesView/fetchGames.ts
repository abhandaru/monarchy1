import * as frags from '~/api/fragments';
import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function fetchGames(q) {
  return fetch(query)({ q });
}

const query = gql`
  query fetchGames($q: GamesQuery!) {
    games(q: $q) {
      id
      status
      players {
        status
        user {
          ${frags.User}
        }
      }
    }
  }
`;
