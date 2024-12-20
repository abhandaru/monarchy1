import * as frags from '~/api/fragments';
import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeAttack(gameId, attack) {
  return fetch(query)({q: { gameId, attack }});
}

const query = gql`
  mutation attack($q: AttackQuery!) {
    attack(q: $q) {
      ${frags.Selection}
    }
  }
`;

