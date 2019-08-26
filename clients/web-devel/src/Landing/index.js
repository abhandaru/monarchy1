import Auth from '../lib/auth';
import React from 'react';
import Dashboard from '../Dashboard';
import LoginView from './LoginView';

export default class Landing extends React.Component {
  state = {
    auth: Auth.poll()
  };

  render() {
    const { auth } = this.state;
    return auth.loggedIn ? <Dashboard /> : <LoginView />;
  }
}
