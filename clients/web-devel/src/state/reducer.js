import * as Types from './types'
import { combineReducers } from 'redux'
import Auth from '~/api/auth';

const INITIAL_AUTH = Auth.poll();
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
  matchmaking
});
