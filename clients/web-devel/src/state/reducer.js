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

export default combineReducers({
  auth,
  connection,
  matchmaking
});
