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
      players {
        status
        user {
          id
          username
          rating
        }
      },
      state {
        currentPlayerId
        currentSelection {
          i
          j
        }
        currentPhases
        tiles {
          point {
            i
            j
          }
          piece {
            id
            order
            name
            playerId
            currentWait
            currentHealth
            currentBlocking
            currentDirection {
              i
              j
            }
            currentFocus
            currentEffects
          }
        }
      }
    }
  }
`;
