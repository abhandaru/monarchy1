import * as frags from '~/api/fragments';
import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeEndTurn(gameId: string) {
  return fetch(query)({ q: { gameId } });
}

const query = gql`
  mutation endTurn($q: EndTurnQuery!) {
    endTurn(q: $q) {
      ${frags.Selection}
    }
  }
`;
