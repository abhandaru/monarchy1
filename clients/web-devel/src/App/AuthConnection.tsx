import * as React from 'react';
import fetchSelf from './fetchSelf';
import { useDispatch, useSelector } from 'react-redux';
import { authSet } from '~/state/actions';
import { State } from '~/state/state';

const AuthConnection = () => {
  const dispatch = useDispatch();
  const auth = useSelector<State, State['auth']>(_ => _.auth);
  React.useEffect(() => {
    fetchSelf()
      .catch(_ => console.log('self query failed', _))
      .then(res => {
        const user = res.data.self;
        const nextAuth = { ...auth, loggedIn: true, user, userId: user.id };
        dispatch(authSet(nextAuth));
      });
  }, []);
  return null;
}

export default AuthConnection;