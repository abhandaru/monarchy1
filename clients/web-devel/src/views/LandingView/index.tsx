import * as React from 'react';
import * as Auth from '~/api/auth';
import DashboardView from '~/views/DashboardView';
import LoginView from './LoginView';
import { useSelector } from 'react-redux'
import { State } from '~/state/state';

type Props = {};

const LandingView = (props: Props) => {
  // State
  const auth = useSelector<State, Auth.Auth>(_ => _.auth);

  return auth.loggedIn ?
    <DashboardView auth={auth} /> : <LoginView />;
}

export default LandingView;
