import * as Actions from '~/state/actions';
import * as React from 'react';
import streamProxy from '~/api/streamProxy';
import { State } from '~/state/state';
import { useDispatch, useSelector } from 'react-redux'
import { withRouter } from 'react-router-dom';

type Message = (
  { name: 'Pong', data: any} |
  { name: 'Matchmaking', data: any } |
  { name: 'GameCreate', data: { gameId: string } } |
  { name: 'GameChange', data: { gameId: string } }
);

type MessageAction = {
  action?: any,
  route?: string
};

const PingPeriod = 15000;
const messageConverter = (message: Message): MessageAction => {
  switch (message.name) {
    case 'Pong':
      return { action: Actions.pong(message.data) };
    case 'Matchmaking':
      return { action: Actions.matchmakingSet(message.data) };
    case 'GameCreate':
      return { route: `/games/${message.data.gameId}` };
    case 'GameChange':
      return { action: Actions.gameFetch(message.data.gameId) };
    default:
      return null;
  }
};

const StreamConnection = withRouter((props) => {
  const { history } = props;
  const dispatch = useDispatch();
  const auth = useSelector<State, State['auth']>(_ => _.auth);

  // Equivalent of componentDidMount
  // React.useEffect(() => {
  //   setInterval(() => dispatch(Actions.ping()), PingPeriod);
  // }, []);

  // Open port when auth changes
  React.useEffect(() => {
    if (auth.loggedIn) {
      streamProxy.connect();
      streamProxy.listen((message) => {
        const { action, route } = messageConverter(message);
        action && dispatch(action);
        route && history.push(route);
      });
    }
  }, [auth, history]);
  // Nothing to render, just need access to store and dispatch.
  return null;
});

export default StreamConnection;
