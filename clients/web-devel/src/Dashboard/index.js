import React from 'react';
import { Link } from 'react-router-dom';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import Alert from 'react-bootstrap/Alert';
import styles from './index.css';

const Dashboard = (props) => {
  const { auth } = props;
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
      </div>
    </>
  );
};

export default Dashboard;
