import * as frags from '~/api/fragments';
import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeMove(q) {
  return fetch(query)({ q });
}

const query = gql`
  mutation Move($q: MoveQuery!) {
    move(q: $q) {
      ${frags.Selection}
    }
  }
`;
