import { print } from 'graphql/language/printer';
import Auth from './auth';

const graphqlRoute = 'http://localhost:8080/graphql';

const fetchGql = (graphqlAst) => {
  const { userId, bearerToken } = Auth.poll();
  const definition = graphqlAst.definitions[0];
  const operationName = definition.name.value;
  const query = print(graphqlAst);
  return (variables) => {
    const data = { query, operationName, variables };
    return fetch(graphqlRoute, {
      method: 'POST',
      mode: 'cors',
      headers: {
        ...Auth.headers(),
        'Content-Type': 'application/json'
      },
      credentials: 'include',
      body: JSON.stringify(data)
    }).then(_ => _.json());
  };
}

export default fetchGql;
