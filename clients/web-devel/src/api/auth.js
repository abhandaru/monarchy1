import Cookies from 'js-cookie';

const BearerKey = 'Authorization';
const UserKey = 'X-M1-User-Id';
const UserDataKey = 'X-M1-User-Data';

const poll = () => {
  const userId = Cookies.get(UserKey);
  const bearerToken = Cookies.get(BearerKey);
  const user = JSON.parse(Cookies.get(UserDataKey) || 'null');
  return {
    loggedIn: Boolean(userId),
    userId,
    user,
    bearerToken
  }
};

const headers = () => {
  const { userId, bearerToken } = poll();
  return {
    [UserKey]: userId,
    [BearerKey]: `Bearer ${bearerToken}`
  };
};

const apply = (auth) => {
  const { userId, user, bearerToken } = auth;
  userId && Cookies.set(UserKey, userId);
  user && Cookies.set(UserDataKey, JSON.stringify(user));
  bearerToken && Cookies.set(BearerKey, `Bearer ${bearerToken}`);
};

export default {
  poll,
  headers,
  apply
};
