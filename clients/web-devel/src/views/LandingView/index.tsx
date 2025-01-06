import * as React from 'react';
import * as Auth from '~/api/auth';
import DashboardView from '~/views/DashboardView';
import LoginView from './LoginView';
import { useDispatch, useSelector } from 'react-redux'
import { authSet } from '~/state/actions';
import { State } from '~/state/state';

type Props = {};

const LandingView = (props: Props) => {
  // State
  const dispatch = useDispatch();
  const auth = useSelector<State, Auth.Auth>(_ => _.auth);
  const onLogin = React.useCallback(_ => dispatch(authSet(_)), [dispatch]);

  return auth.loggedIn ?
    <DashboardView auth={auth} /> : <LoginView />;
}

export default LandingView;
