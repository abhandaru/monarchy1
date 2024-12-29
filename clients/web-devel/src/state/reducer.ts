import * as Types from './types';
import * as State from './state';
import { combineReducers } from 'redux';

// Just take the first one for now. We can do something smarter later, but the
// client should own this logic.
function getPhase(phases: string[]) {
  return phases[0] ?? null;
}

const auth = (state = State.INITIAL_AUTH, action) => {
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

const connection = (state = State.INITIAL_CONNECTION, action) => {
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

const matchmaking = (state = State.INITIAL_MATCHMAKING, action) => {
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

const games = (state = State.INITIAL_GAMES, action) => {
  switch (action.type) {
    case Types.GAMES_SET_RECENT:
      return {
        ...state,
        recent: action.payload
      };
    // This also handles the initial game fetch and initial selection inference.
    // Always prefer what is on the server. Certain fields on `GameSelections`
    // only exist on the client, such `phase`, so we compute it here.
    case Types.GAME_FETCHED:
      return {
        ...state,
        game: action.payload,
        gameSelections: {
          ...state.gameSelections,
          ...action.payload.state.currentSelection,
          effects: [],
          phase: getPhase(action.payload.state.currentSelection.phases)
        }
      };
    case Types.GAME_SET_SELECTIONS:
      return {
        ...state,
        gameSelections: {
          ...state.gameSelections,
          ...action.payload,
          effects: [],
          phase: getPhase(action.payload.phases)
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
    case Types.GAME_EFFECTS_FETCHED:
      return {
        ...state,
        gameSelections: {
          ...state.gameSelections,
          effects: action.payload
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
