import { print } from 'graphql/language/printer';
import { host } from '~/util/host';

const graphqlRoute = `${host}/graphql`;

const fetchGql = (graphqlAst) => {
  const definition = graphqlAst.definitions[0];
  const operationName = definition.name.value;
  const query = print(graphqlAst);
  return (variables) => {
    const data = { query, operationName, variables };
    return fetch(graphqlRoute, {
      method: 'POST',
      mode: 'cors',
      headers: {
        'Content-Type': 'application/json'
      },
      credentials: 'include',
      body: JSON.stringify(data)
    }).then(_ => _.json());
  };
}

export default fetchGql;
