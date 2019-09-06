import * as React from 'react';
import Alert from 'react-bootstrap/Alert';
import MatchmakingView from '~/views/MatchmakingView';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import styles from './index.css';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux'

const Dashboard = (props) => {
  const auth = useSelector(_ => _.auth);
  return (
    <>
      <Navbar bg='light' variant='light'>
        <Navbar.Brand href='/'>Monarchy</Navbar.Brand>
        <Nav className='mr-auto'>
          <Link to='/matchmaking'>Enter matchmaking</Link>
        </Nav>
      </Navbar>
      <div className={styles.root}>
        <Alert variant='primary'>
          Welcome, you are logged in as <b>{auth.user.username}</b>
        </Alert>
        <MatchmakingView />
      </div>
    </>
  );
};

export default Dashboard;
