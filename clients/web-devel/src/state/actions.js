import * as Types from './types'
import Auth from '~/api/auth';
import streamProxy from '~/api/streamProxy';
import fetchEffects from '~/api/fetchEffects';
import fetchGame from '~/api/fetchGame';
import writeAttack from '~/api/writeAttack';
import writeMove from '~/api/writeMove';
import writeSelect from '~/api/writeSelect';

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

export const gameSelect = (gameId, point) => (dispatch) => {
  dispatch(createAction(Types.GAME_SELECT));
  return writeSelect({ gameId, point }).then(r => {
    dispatch(gameSetSelections(r.data.select));
    return r.data.select;
  });
};

export const gameMove = (gameId, point) => (dispatch) => {
  dispatch(createAction(Types.GAME_MOVE));
  return writeMove({ gameId, point }).then(r => {
    dispatch(gameSetSelections(r.data.move));
    return r.data.move;
  });
};

export const gameEffectsFetch = (gameId, attack) => (dispatch) => {
  dispatch(createAction(Types.GAME_EFFECTS_FETCH));
  return fetchEffects(gameId, attack).then(r => {
    dispatch(createAction(Types.GAME_EFFECTS_FETCHED, r.data.effects));
    return r.data.effects;
  });
};

export const gameAttack = (gameId, attack) => (dispatch) => {
  dispatch(createAction(Types.GAME_ATTACK));
  return writeAttack(gameId, attack).then(r => {
    dispatch(gameSetSelections(r.data.attack));
    return r.data.attack;
  });
};

export const gameSetSelections = (selections) =>
  createAction(Types.GAME_SET_SELECTIONS, selections);

export const gameSetPhase = (phase) =>
  createAction(Types.GAME_SET_PHASE, phase);
