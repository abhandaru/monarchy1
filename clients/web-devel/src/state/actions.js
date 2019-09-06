import * as Types from './types'
import Auth from '~/api/auth';

export const authSet = (auth) => {
  Auth.apply(auth);
  return { type: Types.AUTH_SET, payload: auth };
};

export const matchmakingSet = (challenges) => ({
  type: Types.MATCHMAKING_SET,
  payload: challenges
});
