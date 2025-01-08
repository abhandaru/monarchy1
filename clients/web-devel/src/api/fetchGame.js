import * as frags from '~/api/fragments';
import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function fetchGame(id) {
  return fetch(query)({ id });
}

const query = gql`
  query game($id: String!) {
    game(id: $id) {
      id
      status
      createdAt
      players {
        id
        status
        rating
        ratingDelta
        user {
          ${frags.User}
        }
      },
      state {
        currentPlayerId
        currentSelection {
          ${frags.Selection}
        }
        tiles {
          point {
            ${frags.Vector}
          }
          piece {
            ${frags.Piece}
          }
        }
      }
    }
  }
`;
