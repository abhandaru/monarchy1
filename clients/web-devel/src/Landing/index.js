import Auth from '../lib/auth';
import React from 'react';
import { Link } from 'react-router-dom';
import Dashboard from '../Dashboard';

export default class Landing extends React.Component {
  state = {
    auth: Auth.poll()
  };

  renderLogin() {
    return <div>Login form</div>;
  }

  render() {
    const { auth } = this.state;
    return auth.loggedIn ? <Dashboard /> : this.renderLogin();
  }
}
