import * as frags from '~/api/fragments';
import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function fetchLobby() {
  return fetch(query)({});
}

const query = gql`
  query lobby {
    lobby {
      usersOnline {
        ${frags.User}
      }
      challenges {
        host {
          ${frags.User}
        }
      }
    }
  }
`;
