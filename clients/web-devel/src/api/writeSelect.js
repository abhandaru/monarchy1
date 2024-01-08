import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeSelect(q) {
  return fetch(query)({ q });
}

const vecFrag = '{ i j }';
const query = gql`
  mutation Select($q: SelectQuery!) {
    select(q: $q) {
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
