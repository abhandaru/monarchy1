import * as frags from '~/api/fragments';
import * as Types from '~/util/types';
import fetch from '~/api/fetch';
import gql from 'graphql-tag';

type Result = { games: Types.Game[] };

export default function fetchGames(q): Promise<Types.GqlFetch<Result>> {
  return fetch(query)({ q });
}

const query = gql`
  query fetchGames($q: GamesQuery!) {
    games(q: $q) {
      id
      status
      players {
        status
        rating
        ratingDelta
        user {
          ${frags.User}
        }
      }
    }
  }
`;
