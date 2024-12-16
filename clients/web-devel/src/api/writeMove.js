import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeMove(q) {
  return fetch(query)({ q });
}

const vecFrag = '{ i j }';
const query = gql`
  mutation Move($q: MoveQuery!) {
    move(q: $q) {
      selection ${vecFrag}
      movements ${vecFrag}
      directions ${vecFrag}
      attacks ${vecFrag}
      piece {
        id
        playerId
      }
    }
  }
`;
