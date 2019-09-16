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
          username
          rating
        }
      },
      state {
        currentPlayerId
        tiles {
          point {
            i
            j
          }
          piece {
            id
            playerId
            currentWait
            currentHealth
            currentDirection {
              i
              j
            }
            currentFocus
            currentEffects
            blockingAjustment
          }
        }
      }
    }
  }
`;
