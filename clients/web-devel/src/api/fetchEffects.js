import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function fetchEffects(gameId, attack) {
  return fetch(query)({ q: { gameId, attack }});
}

const query = gql`
  query fetchEffects($q: AttackQuery!) {
    effects(q: $q) {
      point {
        i
        j
      }
    }
  }
`;
