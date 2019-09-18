import * as Types from './types'
import Auth from '~/api/auth';
import streamProxy from '~/api/streamProxy';
import fetchGame from '~/api/fetchGame';

// Utilities
const createAction = (type, payload) => ({ type, payload });
const clockAt = () => (new Date).getTime();

// Actions below
export const authSet = (auth) => {
  Auth.apply(auth);
  return createAction(Types.AUTH_SET, auth);
};

export const matchmakingSet = (challenges) =>
  createAction(Types.MATCHMAKING_SET, challenges);

export const ping = () => {
  streamProxy.send('Ping');
  return createAction(Types.PING, { at: clockAt() });
};

export const pong = (serverAt) =>
  createAction(Types.PONG, { at: clockAt(), serverAt });

export const gamesSetRecent = (games) =>
  createAction(Types.GAMES_SET_RECENT, games);

export const gameFetch = (id) => (dispatch) => {
  dispatch(createAction(Types.GAME_FETCH));
  return fetchGame(id).then(r => {
    dispatch(createAction(Types.GAME_FETCHED, r.data.game));
    return r.data.game;
  });
};

export const gameSetSelections = (selections) =>
  createAction(Types.GAME_SET_SELECTIONS, selections);
