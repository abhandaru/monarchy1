import React from 'react';
import { Link } from 'react-router-dom';

export default class Dashboard extends React.Component {
  render() {
    return (
      <div>
        <div>This is the dashboard</div>
        <Link to='/matchmaking'>Enter matchmaking</Link>
      </div>
    );
  }
}
