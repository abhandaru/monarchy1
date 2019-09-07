import * as Types from './types'
import Auth from '~/api/auth';
import streamProxy from '~/api/streamProxy';

const createAction = (type, payload) => ({ type, payload });
const clockAt = () => (new Date).getTime();

export const authSet = (auth) => {
  Auth.apply(auth);
  return createAction(Types.AUTH_SET, auth);
};

export const matchmakingSet = (challenges) =>
  createAction(Types.MATCHMAKING_SET, challenges);

export const ping = () => {
  streamProxy.send({ name: 'Ping' });
  return createAction(Types.PING, { at: clockAt() });
};

export const pong = (serverAt) =>
  createAction(Types.PONG, { at: clockAt(), serverAt });
