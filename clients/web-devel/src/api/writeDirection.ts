import * as frags from '~/api/fragments';
import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeDirection(gameId, direction) {
  return fetch(query)({q: { gameId, direction }});
}

const query = gql`
  mutation direct($q: DirectionQuery!) {
    direct(q: $q) {
      ${frags.Selection}
    }
  }
`;

