import Cookies from 'js-cookie';

const BearerKey = 'X-Monarchy-Bearer-Token';
const UserKey = 'X-Monarchy-User-Id';

const poll = () => {
  const userId = Cookies.get(UserKey);
  const bearerTokey = Cookies.get(BearerKey);
  return {
    loggedIn: userId != null,
    userId,
    bearerTokey
  }
};

export default {
  poll
};
