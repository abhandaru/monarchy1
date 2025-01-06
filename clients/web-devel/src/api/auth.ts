import Cookies from 'js-cookie';
import * as Types from '~/util/types';

export type Auth = {
  loggedIn: boolean;
  userId: null | string;
  user: null | Types.User;
};

const UserDataKey = 'X-M1-User-Data';

export const poll = (): Auth => {
  const user = JSON.parse(Cookies.get(UserDataKey) || 'null');
  return {
    loggedIn: Boolean(user?.id),
    userId: user?.id,
    user,
  }
};

export const apply = (auth: Auth) => {
  const { user } = auth;
  user && Cookies.set(UserDataKey, JSON.stringify(user));
};
