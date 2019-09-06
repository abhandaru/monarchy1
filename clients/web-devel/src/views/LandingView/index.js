import * as React from 'react';
import Auth from '~/api/auth';
import DashboardView from '~/views/DashboardView';
import LoginView from './LoginView';
import { useDispatch, useSelector } from 'react-redux'
import { authSet } from '~/state/actions';

const LandingView = (props) => {
  // State
  const dispatch = useDispatch();
  const auth = useSelector(_ => _.auth);
  const onLogin = React.useCallback(_ => dispatch(authSet(_)))

  return auth.loggedIn ?
    <DashboardView auth={auth} /> : <LoginView onLogin={onLogin} />;
}

export default LandingView;
