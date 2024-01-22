import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function fetchLobby() {
  return fetch(query)({});
}

const query = gql`
  query lobby {
    lobby {
      usersOnline {
        id
        username
        rating
      }
      challenges {
        host {
          id
          username
          rating
        }
      }
    }
  }
`;
