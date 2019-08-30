import * as React from 'react';
import { Link } from 'react-router-dom';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import Alert from 'react-bootstrap/Alert';
import styles from './index.css';
import webSocket from '~/api/webSocket';

// TODO (adu): Factor this out into a reducer.
webSocket.onmessage = (m) => console.log(m);

const Dashboard = (props) => {
  const { auth } = props;
  const onChange = React.useCallback((e) => webSocket.send(e.target.value));
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

        <input onChange={onChange} defaultValue='adu' />
      </div>
    </>
  );
};

export default Dashboard;
