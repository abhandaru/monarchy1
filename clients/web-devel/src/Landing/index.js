import * as React from 'react';
import Auth from '~/api/auth';
import Dashboard from '~/Dashboard';
import LoginView from './LoginView';

const Landing = (props) => {
  // State
  const [auth, setAuth] = React.useState(Auth.poll());

  // Effects
  React.useEffect(() => Auth.apply(auth));

  return auth.loggedIn ? <Dashboard auth={auth} /> : <LoginView onLogin={setAuth} />;
}

export default Landing;
