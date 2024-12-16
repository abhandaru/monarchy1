import * as Types from './types'
import { combineReducers } from 'redux'
import Auth from '~/api/auth';

const INITIAL_AUTH = Auth.poll();

const INITIAL_CONNECTION = {
  lastPingAt: null,
  lastPongAt: null,
  lastPongServerAt: null
};

const INITIAL_MATCHMAKING = {
  challenges: []
};

const INITIAL_GAMES = {
  recent: [],
  game: null,
  gameSelections: {
    phase: null,
    selection: null,
    piece: null,
    movements: [],
    directions: [],
    attacks: []
  }
};

const auth = (state = INITIAL_AUTH, action) => {
  switch (action.type) {
    case Types.AUTH_SET:
      return {
        ...state,
        ...action.payload
      };
    default:
      return state;
  }
};

const connection = (state = INITIAL_CONNECTION, action) => {
  switch (action.type) {
    case Types.PING:
      return {
        ...state,
        lastPingAt: action.payload.at,
        lastPongAt: null,
        lastPongServerAt: null
      };
    case Types.PONG:
      const { at, serverAt } = action.payload;
      return {
        ...state,
        lastPongAt: at,
        lastPongServerAt: serverAt,
        latency: at - state.lastPingAt,
        skew: serverAt - at
      };
    default:
      return state;
  }
};

const matchmaking = (state = INITIAL_MATCHMAKING, action) => {
  switch (action.type) {
    case Types.MATCHMAKING_SET:
      return {
        ...state,
        challenges: action.payload
      };
    default:
      return state;
  }
};

const games = (state = INITIAL_GAMES, action) => {
  switch (action.type) {
    case Types.GAMES_SET_RECENT:
      return {
        ...state,
        recent: action.payload
      };
    case Types.GAME_FETCHED:
      const phases = action.payload.state.currentPhases ?? [];
      return {
        ...state,
        game: action.payload,
        gameSelections: {
          ...state.gameSelections,
          phase: phases[0] ?? null
        }
      };
    case Types.GAME_SET_SELECTIONS:
      return {
        ...state,
        gameSelections: {
          ...state.gameSelections,
          ...action.payload
        }
      };
    case Types.GAME_SET_PHASE:
      return {
        ...state,
        gameSelections: {
          ...state.gameSelections,
          phase: action.payload
        }
      };
    default:
      return state;
  }
};

export default combineReducers({
  auth,
  connection,
  matchmaking,
  games
});
