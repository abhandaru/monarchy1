import * as frags from '~/api/fragments';
import fetch from '~/api/fetch';
import gql from 'graphql-tag';

export default function writeSelect(q) {
  return fetch(query)({ q });
}

const query = gql`
  mutation Select($q: SelectQuery!) {
    select(q: $q) {
      ${frags.Selection}
    }
  }
`;

