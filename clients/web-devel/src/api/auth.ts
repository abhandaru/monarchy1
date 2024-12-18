import Cookies from 'js-cookie';

export type Auth = {
  loggedIn: boolean;
  userId: string;
  user: any;
  bearerToken: string;
};

const AuthorizationKey = 'Authorization';
const UserKey = 'X-M1-User-Id';
const UserDataKey = 'X-M1-User-Data';

export const poll = (): Auth => {
  const userId = Cookies.get(UserKey);
  const bearerToken = Cookies.get(AuthorizationKey);
  const user = JSON.parse(Cookies.get(UserDataKey) || 'null');
  return {
    loggedIn: Boolean(userId),
    userId,
    user,
    bearerToken
  }
};

export const headers = () => {
  const { userId, bearerToken } = poll();
  return {
    [UserKey]: userId,
    [AuthorizationKey]: bearerToken
  };
};

export const apply = (auth: Auth) => {
  const { userId, user, bearerToken } = auth;
  userId && Cookies.set(UserKey, userId);
  user && Cookies.set(UserDataKey, JSON.stringify(user));
  bearerToken && Cookies.set(AuthorizationKey, bearerToken);
};

export default {
  poll,
  headers,
  apply
};
