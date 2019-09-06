import * as Actions from '~/state/actions';
import * as React from 'react';
import streamProxy from '~/api/streamProxy';
import { useDispatch, useSelector } from 'react-redux'

const messageConverter = (message) => {
  switch (message.name) {
    case 'Matchmaking':
      return Actions.matchmakingSet(message.data);
    default:
      return null;
  }
};

const StreamConnection = () => {
  const dispatch = useDispatch();
  const auth = useSelector(_ => _.auth);
  // Open port when auth changes
  React.useEffect(() => {
    if (auth.loggedIn) {
      streamProxy.connect();
      streamProxy.listen((message) => {
        console.log('StreamConnection.component', message);
        const action = messageConverter(message);
        action && dispatch(action);
      });
    }
  }, [auth]);
  // Nothing to render, just need access to store and dispatch.
  return null;
}

export default StreamConnection;
