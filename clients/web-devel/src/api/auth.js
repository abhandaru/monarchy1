import Cookies from 'js-cookie';

const BearerKey = 'X-Monarchy-Bearer-Token';
const UserKey = 'X-Monarchy-User-Id';
const UserDataKey = 'X-Monarchy-User-Data';

const poll = () => {
  const userId = Cookies.get(UserKey);
  const bearerTokey = Cookies.get(BearerKey);
  const user = JSON.parse(Cookies.get(UserDataKey) || null);

  console.log('poll', userId, bearerTokey, user, Boolean(userId));

  return {
    loggedIn: Boolean(userId),
    userId,
    user,
    bearerTokey
  }
};

const headers = () => {
  const { userId, bearerToken } = poll();
  return {
    [UserKey]: userId,
    [BearerKey]: bearerToken
  };
};

const apply = (auth) => {
  const { userId, user, bearerToken } = auth;
  userId && Cookies.set(UserKey, userId);
  user && Cookies.set(UserDataKey, JSON.stringify(user));
  bearerToken && Cookies.set(BearerKey, bearerToken);
};

export default {
  poll,
  headers,
  apply
};
