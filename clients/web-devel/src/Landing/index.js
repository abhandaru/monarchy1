import * as React from 'react';
import Auth from '~/api/auth';
import DashboardView from '~/views/DashboardView';
import LoginView from './LoginView';

const Landing = (props) => {
  // State
  const [auth, setAuth] = React.useState(Auth.poll());

  // Effects
  React.useEffect(() => Auth.apply(auth));

  return auth.loggedIn ? <DashboardView auth={auth} /> : <LoginView onLogin={setAuth} />;
}

export default Landing;
